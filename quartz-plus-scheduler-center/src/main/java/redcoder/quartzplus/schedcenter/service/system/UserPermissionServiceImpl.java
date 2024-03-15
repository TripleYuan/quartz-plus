package redcoder.quartzplus.schedcenter.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;
import redcoder.quartzplus.schedcenter.dto.system.UserPermissionInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRole;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUserRoleRel;
import redcoder.quartzplus.schedcenter.repository.RoleRepository;
import redcoder.quartzplus.schedcenter.repository.UserRoleRelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    private UserRoleRelRepository userRoleRelRepository;
    private RoleRepository roleRepository;

    public UserPermissionServiceImpl(UserRoleRelRepository userRoleRelRepository, RoleRepository roleRepository) {
        this.userRoleRelRepository = userRoleRelRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleInfo> getUserPermission(int userid) {
        List<RoleInfo> data = new ArrayList<>();
        userRoleRelRepository.findByUserid(userid).forEach(t -> {
            QuartzPlusRole role = roleRepository.findById(t.getRoleId()).get();
            data.add(new RoleInfo(role.getRoleId(), role.getRoleName(), role.getRoleDesc()));
        });
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> addOrUpdate(UserPermissionInfo info) {
        Integer userid = info.getUserid();
        List<QuartzPlusUserRoleRel> userRoleRels = info.getRoleIds()
                .stream()
                .map(roleId -> {
                    QuartzPlusUserRoleRel rel = new QuartzPlusUserRoleRel();
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
