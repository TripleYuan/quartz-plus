package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;
import redcoder.quartzplus.schedcenter.service.system.RoleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "角色")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/api/roles")
    @ApiOperation("获取所有角色")
    public ApiResult<List<RoleInfo>> getRoles() {
        List<RoleInfo> data = roleService.getRoles();
        return ApiResult.success(data);
    }

    @PostMapping("/api/role")
    @ApiOperation("新增|更新角色")
    public ApiResult<String> addOrUpdate(@RequestBody @Valid RoleInfo roleInfo) {
        return roleService.addOrUpdate(roleInfo);
    }

    @DeleteMapping("/api/role/{roleId}")
    @ApiOperation("删除角色")
    public ApiResult<String> delete(@PathVariable int roleId) {
        return roleService.delete(roleId);
    }
}
