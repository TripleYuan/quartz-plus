package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.RoleInfo;
import redcoder.quartzplus.schedcenter.dto.system.UserPermissionInfo;
import redcoder.quartzplus.schedcenter.service.system.UserPermissionService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "用户权限")
public class UserPermissionController {

    @Resource
    private UserPermissionService userPermissionService;

    @GetMapping("/api/user/permission/{roleId}")
    @ApiOperation("获取角色权限")
    public ApiResult<List<RoleInfo>> getList(@PathVariable int roleId) {
        List<RoleInfo> data = userPermissionService.getUserPermission(roleId);
        return ApiResult.success(data);
    }

    @PostMapping("/api/user/permission")
    @ApiOperation("新增|更新用户权限")
    public ApiResult<String> addOrUpdate(@RequestBody @Valid UserPermissionInfo info) {
        return userPermissionService.addOrUpdate(info);
    }
}
