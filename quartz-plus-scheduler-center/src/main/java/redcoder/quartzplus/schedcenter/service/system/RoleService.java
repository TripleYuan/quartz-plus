package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.sys.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getList();

    ApiResult<String> addOrUpdate(RoleDto dto);

    ApiResult<String> delete(int roleId);
}
