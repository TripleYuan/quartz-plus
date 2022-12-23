package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;
import redcoder.quartzextendschedulercenter.dto.sys.UserPermissionDto;

import java.util.List;

/**
 * 用户权限管理
 */
public interface UserPermissionService {

    /**
     * 获取用户权限
     */
    List<RoleDto> getList(int userid);

    /**
     * 添加或更新用户权限
     */
    ApiResult<String> addOrUpdate(UserPermissionDto dto);

    /**
     * 删除用户权限
     */
    void delete(int userid);
}
