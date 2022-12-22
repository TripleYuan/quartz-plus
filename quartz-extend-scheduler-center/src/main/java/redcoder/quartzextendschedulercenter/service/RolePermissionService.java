package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.MenuDto;
import redcoder.quartzextendschedulercenter.dto.sys.RolePermissionDto;

import java.util.List;

/**
 * 角色权限管理
 */
public interface RolePermissionService {

    /**
     * 获取角色下的菜单权限
     */
    List<MenuDto> getPermission(int roleId);

    /**
     * 添加或更新权限
     */
    ApiResult<String> addOrUpdatePermission(RolePermissionDto permission);

    /**
     * 删除权限
     */
    void deletePermission(int roleId);
}
