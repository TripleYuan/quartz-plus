package redcoder.quartzextendschedulercenter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;
import redcoder.quartzextendschedulercenter.dto.sys.UserPermissionDto;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRole;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzextendschedulercenter.repository.RoleRepository;
import redcoder.quartzextendschedulercenter.repository.UserRoleRelRepository;
import redcoder.quartzextendschedulercenter.service.UserPermissionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Resource
    private UserRoleRelRepository userRoleRelRepository;
    @Resource
    private RoleRepository roleRepository;

    @Override
    public List<RoleDto> getList(int userid) {
        List<RoleDto> data = new ArrayList<>();
        userRoleRelRepository.findByUserid(userid).forEach(t -> {
            QuartzSchedulerRole role = roleRepository.findById(t.getRoleId()).get();
            data.add(new RoleDto(role.getRoleId(), role.getRoleName(), role.getRoleDesc()));
        });
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> addOrUpdate(UserPermissionDto dto) {
        Integer userid = dto.getUserid();
        List<QuartzSchedulerUserRoleRel> userRoleRels = dto.getRoleIds()
                .stream()
                .map(roleId -> {
                    QuartzSchedulerUserRoleRel rel = new QuartzSchedulerUserRoleRel();
                    rel.setUserid(userid);
                    rel.setRoleId(roleId);
                    return rel;
                })
                .collect(Collectors.toList());
        if (userRoleRelRepository.existsByUserid(userid)) {
            // update
            userRoleRelRepository.deleteByUserid(userid);
            userRoleRelRepository.saveAll(userRoleRels);
        } else {
            // add
            userRoleRelRepository.saveAll(userRoleRels);
        }
        return ApiResult.success();
    }

    @Override
    public void delete(int userid) {
        userRoleRelRepository.deleteByUserid(userid);
    }
}
