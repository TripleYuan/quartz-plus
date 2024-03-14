package redcoder.quartzplus.schedcenter.service.system;

import org.springframework.stereotype.Service;
import redcoder.quartzplus.schedcenter.dto.sys.MenuDto;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusMenu;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRoleMenuRel;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUser;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUserRoleRel;
import redcoder.quartzplus.schedcenter.repository.MenuRepository;
import redcoder.quartzplus.schedcenter.repository.RoleMenuRelRepository;
import redcoder.quartzplus.schedcenter.repository.UserRoleRelRepository;
import redcoder.quartzplus.schedcenter.shiro.ShiroUtils;

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
        QuartzPlusUser user = ShiroUtils.getUser();
        if (user.getUserType() == 1) {
            // 管理员看到所有菜单
            return menuRepository.findByMenuStatus(1)
                    .stream()
                    .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                    .collect(Collectors.toList());
        }

        // 非管理员只能看到分配的菜单
        List<QuartzPlusMenu> menus = new ArrayList<>();
        List<QuartzPlusUserRoleRel> userRoleRels = userRoleRelRepository.findByUserid(user.getUserid());
        userRoleRels.forEach(t -> {
            Integer roleId = t.getRoleId();
            List<QuartzPlusRoleMenuRel> roleMenuRels = roleMenuRelRepository.findByRoleId(roleId);
            roleMenuRels.forEach(rel -> {
                Integer menuId = rel.getMenuId();
                menuRepository.findById(menuId).ifPresent(m -> menus.add(m));
            });
        });
        return menus.stream()
                .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuDto> getList() {
        return menuRepository.findByMenuStatus(1)
                .stream()
                .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                .collect(Collectors.toList());
    }
}
