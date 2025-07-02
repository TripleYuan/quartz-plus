package redcoder.quartzplus.schedcenter.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUser;
import redcoder.quartzplus.schedcenter.repository.UserRepository;

/**
 * 自定义的Realm实现类，根据username从用户表中查询用户信息，将查询到用户信息作为身份验证信息返回。
 * 如果未查询到用户信息，抛出异常。
 *
 * @author redcoder
 * @since 2022-12-20
 */
@Service
public class QuartzRealm extends AuthenticatingRealm {

    @Autowired
    private UserRepository userRepository;

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
        QuartzPlusUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户" + username + "不存在");
        }
        return new SimpleAccount(user, user.getPassword(), getName());
    }
}
