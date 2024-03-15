package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.MenuDto;
import redcoder.quartzplus.schedcenter.dto.system.RolePermissionInfo;
import redcoder.quartzplus.schedcenter.service.system.RolePermissionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "角色")
public class RolePermissionController {

    private RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping("/api/role/permission/{roleId}")
    @ApiOperation("获取角色权限")
    public ApiResult<List<MenuDto>> getPermission(@PathVariable int roleId) {
        List<MenuDto> data = rolePermissionService.getPermission(roleId);
        return ApiResult.success(data);
    }

    @PostMapping("/api/role/permission")
    @ApiOperation("新增|更新角色权限")
    public ApiResult<String> addOrUpdatePermission(@RequestBody @Valid RolePermissionInfo info) {
        return rolePermissionService.addOrUpdatePermission(info);
    }
}
