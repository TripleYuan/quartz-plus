package redcoder.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.RoleDto;
import redcoder.quartzextendschedulercenter.service.RoleService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role")
@Api(tags = "角色")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    @ApiOperation("获取所有角色")
    public ApiResult<List<RoleDto>> getList() {
        List<RoleDto> data = roleService.getList();
        return ApiResult.success(data);
    }

    @PostMapping("/addOrUpdate")
    @ApiOperation("新增|更新角色")
    public ApiResult<String> addOrUpdate(@RequestBody @Valid RoleDto dto) {
        return roleService.addOrUpdate(dto);
    }

    @DeleteMapping("/delete/{roleId}")
    @ApiOperation("删除角色")
    public ApiResult<String> delete(@PathVariable int roleId) {
        return roleService.delete(roleId);
    }
}
