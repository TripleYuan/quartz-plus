package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.MenuDto;
import redcoder.quartzplus.schedcenter.service.system.MenuService;

import java.util.List;

@RestController
@Api(tags = "菜单")
public class MenuController {

    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/api/user/menus")
    @ApiOperation("获取用户菜单")
    public ApiResult<List<MenuDto>> getUserMenu() {
        List<MenuDto> menuDtos = menuService.getUserMenu();
        return ApiResult.success(menuDtos);
    }

    @GetMapping("/api/menus")
    @ApiOperation("获取所有菜单")
    public ApiResult<List<MenuDto>> getList() {
        List<MenuDto> menuDtos = menuService.getList();
        return ApiResult.success(menuDtos);
    }
}
