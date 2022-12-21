package redcoder.quartzextendschedulercenter.service.impl;

import org.springframework.stereotype.Service;
import redcoder.quartzextendschedulercenter.dto.sys.MenuDto;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerMenu;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRoleMenuRel;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzextendschedulercenter.repository.MenuRepository;
import redcoder.quartzextendschedulercenter.repository.RoleMenuRelRepository;
import redcoder.quartzextendschedulercenter.repository.UserRoleRelRepository;
import redcoder.quartzextendschedulercenter.service.MenuService;
import redcoder.quartzextendschedulercenter.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private UserRoleRelRepository userRoleRelRepository;
    @Resource
    private MenuRepository menuRepository;
    @Resource
    private RoleMenuRelRepository roleMenuRelRepository;

    @Override
    public List<MenuDto> getUserMenu() {
        QuartzSchedulerUser user = ShiroUtils.getUser();
        if (user.getUserType() == 1) {
            // 管理员看到所有菜单
            List<QuartzSchedulerMenu> list = menuRepository.findByMenuStatus(1);
            return list.stream()
                    .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                    .collect(Collectors.toList());
        }

        // 非管理员只能看到分配的菜单
        List<QuartzSchedulerMenu> menus = new ArrayList<>();
        List<QuartzSchedulerUserRoleRel> userRoleRels = userRoleRelRepository.findByUserid(user.getUserid());
        userRoleRels.forEach(t -> {
            Integer roleId = t.getRoleId();
            List<QuartzSchedulerRoleMenuRel> roleMenuRels = roleMenuRelRepository.findByRoleId(roleId);
            roleMenuRels.forEach(rel -> {
                Integer menuId = rel.getMenuId();
                menuRepository.findById(menuId).ifPresent(m -> menus.add(m));
            });
        });
        return menus.stream()
                .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                .collect(Collectors.toList());
    }
}
