package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.UserDto;

import java.util.List;

/**
 * 用户管理
 */
public interface UserService {

    /**
     * 默认密码：123456
     */
    String DEFAULT_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

    List<UserDto> getList();

    ApiResult<String> addOrUpdate(UserDto dto);

    ApiResult<String> delete(int userid);
}
