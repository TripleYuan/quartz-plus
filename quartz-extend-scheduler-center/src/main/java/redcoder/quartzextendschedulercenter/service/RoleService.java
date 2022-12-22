package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getList();

    ApiResult<String> addOrUpdateRole(RoleDto dto);

    ApiResult<String> deleteRole(int roleId);
}
