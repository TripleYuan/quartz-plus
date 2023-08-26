package redcoder.quartzplus.schedcenter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.sys.MenuDto;
import redcoder.quartzplus.schedcenter.dto.sys.RolePermissionDto;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerRoleMenuRel;
import redcoder.quartzplus.schedcenter.repository.MenuRepository;
import redcoder.quartzplus.schedcenter.repository.RoleMenuRelRepository;
import redcoder.quartzplus.schedcenter.service.RolePermissionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private RoleMenuRelRepository roleMenuRelRepository;
    @Resource
    private MenuRepository menuRepository;

    @Override
    public List<MenuDto> getPermission(int roleId) {
        List<MenuDto> dtos = new ArrayList<>();
        roleMenuRelRepository.findByRoleId(roleId)
                .forEach(t -> menuRepository.findById(t.getMenuId())
                        .ifPresent(menu -> dtos.add(new MenuDto(menu.getMenuId(), menu.getMenuCode(), menu.getMenuName(), menu.getMenuType()))));
        return dtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> addOrUpdatePermission(RolePermissionDto permission) {
        int roleId = permission.getRoleId();
        List<Integer> menuIds = permission.getMenuIds();

        List<QuartzSchedulerRoleMenuRel> relList = menuIds.stream()
                .map(menuId -> {
                    QuartzSchedulerRoleMenuRel rel = new QuartzSchedulerRoleMenuRel();
                    rel.setRoleId(roleId);
                    rel.setMenuId(menuId);
                    return rel;
                })
                .collect(Collectors.toList());

        if (roleMenuRelRepository.existsByRoleId(roleId)) {
            // update
            roleMenuRelRepository.deleteByRoleId(roleId);
            roleMenuRelRepository.saveAll(relList);
        } else {
            // add
            roleMenuRelRepository.saveAll(relList);
        }

        return ApiResult.success();
    }

    @Override
    public void deletePermission(int roleId) {
        roleMenuRelRepository.deleteByRoleId(roleId);
    }
}
