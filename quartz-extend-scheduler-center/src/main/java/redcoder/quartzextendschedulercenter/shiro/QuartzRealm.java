package redcoder.quartzextendschedulercenter.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.stereotype.Service;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;
import redcoder.quartzextendschedulercenter.mapper.UserMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;

/**
 * 继承{@link AuthenticatingRealm}，根据手机号从用户表中查询用户信息，将查询到用户信息作为身份验证信息返回。
 * 如果未查询到用户信息，或者用户已被禁用，抛出相应的异常。
 *
 * @author redcoder
 * @since 2022-12-20
 */
@Service
public class QuartzRealm extends AuthenticatingRealm {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据手机号从用户表中查询用户信息，将查询到用户信息作为身份验证信息返回。
     * 如果未查询到用户信息，或者用户已被禁用，抛出相应的异常。
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return 包含用户身份信息的 {@link AuthenticationInfo}对象
     * @throws UnknownAccountException  用户不存在
     * @throws DisabledAccountException 用户已被禁用，无法登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof UsernamePasswordToken)) {
            throw new UnsupportedTokenException("无法识别的token，目前仅支持AuthenticationToken类型的token");
        }

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        Example qex = Example.builder(QuartzSchedulerUser.class)
                .where(Sqls.custom().andEqualTo("username", username))
                .build();
        QuartzSchedulerUser user = userMapper.selectOneByExample(qex);
        if (user == null) {
            throw new UnknownAccountException("用户" + username + "不存在");
        }
        return new SimpleAccount(user, user.getPassword(), getName());
    }
}
