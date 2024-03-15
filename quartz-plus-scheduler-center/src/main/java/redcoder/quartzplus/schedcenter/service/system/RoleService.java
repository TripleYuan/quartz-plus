package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;

import java.util.List;

public interface RoleService {

    List<RoleInfo> getRoles();

    ApiResult<String> addOrUpdate(RoleInfo dto);

    ApiResult<String> delete(int roleId);
}
