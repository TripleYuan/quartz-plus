package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.PasswordUpdate;
import redcoder.quartzplus.schedcenter.dto.system.UserInfo;

import java.util.List;

/**
 * 用户管理
 */
public interface UserService {

    /**
     * 默认密码：123456
     */
    String DEFAULT_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

    List<UserInfo> getUsers();

    ApiResult<String> addOrUpdate(UserInfo info);

    ApiResult<String> delete(int userid);

    ApiResult<String> updatePassword(PasswordUpdate passwordUpdate);
}
