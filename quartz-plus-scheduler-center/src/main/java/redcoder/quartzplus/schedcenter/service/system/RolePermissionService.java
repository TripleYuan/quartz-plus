package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.MenuDto;
import redcoder.quartzplus.schedcenter.dto.system.RolePermissionInfo;

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
    ApiResult<String> addOrUpdatePermission(RolePermissionInfo info);

    /**
     * 删除权限
     */
    void deletePermission(int roleId);
}
