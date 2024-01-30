package redcoder.quartzplus.schedcenter.shiro;

import org.apache.shiro.SecurityUtils;
import org.springframework.util.Assert;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUser;

public class ShiroUtils {

    /**
     * 获取已登录的用户id
     *
     * @throws IllegalStateException 如果用户未登录
     */
    public static int getUserId() {
        return getUser().getUserid();
    }

    /**
     * 获取已登录的用户信息
     *
     * @throws IllegalStateException 如果用户未登录
     */
    public static QuartzPlusUser getUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        Assert.state(principal != null, "用户未登录");
        return (QuartzPlusUser) principal;
    }
}
