package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.sys.MenuDto;
import redcoder.quartzplus.schedcenter.dto.sys.RolePermissionDto;
import redcoder.quartzplus.schedcenter.service.RolePermissionService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role/permission")
@Api(tags = "角色")
public class RolePermissionController {

    @Resource
    private RolePermissionService rolePermissionService;

    @GetMapping("/list/{roleId}")
    @ApiOperation("获取角色权限")
    public ApiResult<List<MenuDto>> getList(@PathVariable int roleId) {
        List<MenuDto> data = rolePermissionService.getPermission(roleId);
        return ApiResult.success(data);
    }

    @PostMapping("/addOrUpdate")
    @ApiOperation("新增|更新角色权限")
    public ApiResult<String> addOrUpdatePermission(@RequestBody @Valid RolePermissionDto dto) {
        return rolePermissionService.addOrUpdatePermission(dto);
    }
}
