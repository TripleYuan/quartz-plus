package redcoder.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.MenuDto;
import redcoder.quartzextendschedulercenter.service.MenuService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Api(tags = "菜单")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/getUserMenu")
    @ApiOperation("获取用户菜单")
    public ApiResult<List<MenuDto>> getUserMenu() {
        List<MenuDto> menuDtos = menuService.getUserMenu();
        return ApiResult.success(menuDtos);
    }

    @GetMapping("/list")
    @ApiOperation("获取所有菜单")
    public ApiResult<List<MenuDto>> getList() {
        List<MenuDto> menuDtos = menuService.getList();
        return ApiResult.success(menuDtos);
    }
}
