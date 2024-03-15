package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.system.MenuDto;

import java.util.List;

public interface MenuService {

    /**
     * 获取用户可见的菜单
     */
    List<MenuDto> getUserMenu();

    /**
     * 获取所有的菜单
     */
    List<MenuDto> getList();
}
