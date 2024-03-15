package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;
import redcoder.quartzplus.schedcenter.dto.system.UserPermissionInfo;

import java.util.List;

/**
 * 用户权限管理
 */
public interface UserPermissionService {

    /**
     * 获取用户权限
     */
    List<RoleInfo> getUserPermission(int userid);

    /**
     * 添加或更新用户权限
     */
    ApiResult<String> addOrUpdate(UserPermissionInfo info);

    /**
     * 删除用户权限
     */
    void delete(int userid);
}
