package redcoder.quartzplus.schedcenter.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.MenuDto;
import redcoder.quartzplus.schedcenter.dto.system.RolePermissionInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRoleMenuRel;
import redcoder.quartzplus.schedcenter.repository.MenuRepository;
import redcoder.quartzplus.schedcenter.repository.RoleMenuRelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private RoleMenuRelRepository roleMenuRelRepository;
    private MenuRepository menuRepository;

    public RolePermissionServiceImpl(RoleMenuRelRepository roleMenuRelRepository, MenuRepository menuRepository) {
        this.roleMenuRelRepository = roleMenuRelRepository;
        this.menuRepository = menuRepository;
    }

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
    public ApiResult<String> addOrUpdatePermission(RolePermissionInfo info) {
        int roleId = info.getRoleId();
        List<Integer> menuIds = info.getMenuIds();

        List<QuartzPlusRoleMenuRel> relList = menuIds.stream()
                .map(menuId -> {
                    QuartzPlusRoleMenuRel rel = new QuartzPlusRoleMenuRel();
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
