package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.sys.MenuDto;

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
