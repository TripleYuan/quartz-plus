package redcoder.quartzextendschedulercenter.service.impl;

import org.springframework.stereotype.Service;
import redcoder.quartzextendschedulercenter.dto.sys.MenuDto;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerMenu;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRoleMenuRel;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzextendschedulercenter.mapper.MenuMapper;
import redcoder.quartzextendschedulercenter.mapper.RoleMenuRelMapper;
import redcoder.quartzextendschedulercenter.mapper.UserRoleRelMapper;
import redcoder.quartzextendschedulercenter.service.MenuService;
import redcoder.quartzextendschedulercenter.shiro.ShiroUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private UserRoleRelMapper userRoleRelMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMenuRelMapper roleMenuRelMapper;

    @Override
    public List<MenuDto> getUserMenu() {
        QuartzSchedulerUser user = ShiroUtils.getUser();
        if (user.getUserType() == 1) {
            // 管理员看到所有菜单
            Example example = Example.builder(QuartzSchedulerMenu.class)
                    .where(Sqls.custom().andEqualTo("menuStatus", 1))
                    .build();
            List<QuartzSchedulerMenu> list = menuMapper.selectByExample(example);
            return list.stream()
                    .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                    .collect(Collectors.toList());
        }

        // 非管理员只能看到分配的菜单
        List<QuartzSchedulerMenu> menus = new ArrayList<>();
        Example example = Example.builder(QuartzSchedulerUserRoleRel.class)
                .where(Sqls.custom().andEqualTo("userid", user.getUserid()))
                .build();
        List<QuartzSchedulerUserRoleRel> userRoleRels = userRoleRelMapper.selectByExample(example);
        userRoleRels.forEach(t -> {
            Integer roleId = t.getRoleId();
            Example exp = Example.builder(QuartzSchedulerRoleMenuRel.class)
                    .where(Sqls.custom().andEqualTo("roleId", roleId))
                    .build();
            List<QuartzSchedulerRoleMenuRel> roleMenuRels = roleMenuRelMapper.selectByExample(exp);
            roleMenuRels.forEach(rel->{
                Integer menuId = rel.getMenuId();
                Example ex = Example.builder(QuartzSchedulerMenu.class)
                        .where(Sqls.custom().andEqualTo("menuStatus", 1))
                        .andWhere(Sqls.custom().andEqualTo("menuId", menuId))
                        .build();
                Optional.ofNullable(menuMapper.selectOneByExample(ex)).ifPresent(m -> menus.add(m));
            });
        });
        return menus.stream()
                .map(t -> new MenuDto(t.getMenuId(), t.getMenuCode(), t.getMenuName(), t.getMenuType()))
                .collect(Collectors.toList());
    }
}
