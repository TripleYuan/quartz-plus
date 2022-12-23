package redcoder.quartzextendschedulercenter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzextendschedulercenter.constant.ApiStatus;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRole;
import redcoder.quartzextendschedulercenter.repository.RoleRepository;
import redcoder.quartzextendschedulercenter.service.RolePermissionService;
import redcoder.quartzextendschedulercenter.service.RoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;
    @Resource
    private RolePermissionService relService;

    @Override
    public List<RoleDto> getList() {
        List<RoleDto> roleDtos = new ArrayList<>();
        roleRepository.findAll().forEach(t -> {
            RoleDto dto = new RoleDto(t.getRoleId(), t.getRoleName(), t.getRoleDesc());
            roleDtos.add(dto);
        });
        return roleDtos;
    }

    @Override
    public ApiResult<String> addOrUpdate(RoleDto dto) {
        Integer roleId = dto.getRoleId();
        if (roleId == null) {
            // add
            QuartzSchedulerRole role = new QuartzSchedulerRole();
            role.setRoleName(dto.getRoleName());
            role.setRoleDesc(dto.getRoleDesc());
            roleRepository.save(role);
        } else {
            // update
            Optional<QuartzSchedulerRole> optional = roleRepository.findById(dto.getRoleId());
            if (optional.isPresent()) {
                QuartzSchedulerRole role = optional.get();
                role.setRoleDesc(dto.getRoleDesc());
                role.setRoleName(dto.getRoleName());
                role.setUpdateTime(new Date());
                roleRepository.save(role);
            } else {
                return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "角色不存在！");
            }
        }
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> delete(int roleId) {
        roleRepository.deleteById(roleId);
        relService.deletePermission(roleId);
        return ApiResult.success();
    }
}
