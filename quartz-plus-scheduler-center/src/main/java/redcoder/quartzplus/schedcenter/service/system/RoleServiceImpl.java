package redcoder.quartzplus.schedcenter.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.constant.ApiStatus;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRole;
import redcoder.quartzplus.schedcenter.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    private RolePermissionService relService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RolePermissionService relService) {
        this.roleRepository = roleRepository;
        this.relService = relService;
    }

    @Override
    public List<RoleInfo> getRoles() {
        List<RoleInfo> roleInfos = new ArrayList<>();
        roleRepository.findAll().forEach(t -> {
            RoleInfo dto = new RoleInfo(t.getRoleId(), t.getRoleName(), t.getRoleDesc());
            roleInfos.add(dto);
        });
        return roleInfos;
    }

    @Override
    public ApiResult<String> addOrUpdate(RoleInfo dto) {
        Integer roleId = dto.getRoleId();
        if (roleId == null) {
            // add
            QuartzPlusRole role = new QuartzPlusRole();
            role.setRoleName(dto.getRoleName());
            role.setRoleDesc(dto.getRoleDesc());
            roleRepository.save(role);
        } else {
            // update
            Optional<QuartzPlusRole> optional = roleRepository.findById(dto.getRoleId());
            if (optional.isPresent()) {
                QuartzPlusRole role = optional.get();
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
