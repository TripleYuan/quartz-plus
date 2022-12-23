package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getList();

    ApiResult<String> addOrUpdate(RoleDto dto);

    ApiResult<String> delete(int roleId);
}
