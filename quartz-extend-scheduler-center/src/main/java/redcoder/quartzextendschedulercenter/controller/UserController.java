package redcoder.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.UserDto;
import redcoder.quartzextendschedulercenter.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api(tags = "用户")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @ApiOperation("获取所有用户")
    public ApiResult<List<UserDto>> getList() {
        List<UserDto> data = userService.getList();
        return ApiResult.success(data);
    }

    @PostMapping("/addOrUpdate")
    @ApiOperation("新增|更新用户")
    public ApiResult<String> addOrUpdate(@RequestBody @Valid UserDto dto) {
        return userService.addOrUpdate(dto);
    }

    @DeleteMapping("/delete/{userid}")
    @ApiOperation("删除角色")
    public ApiResult<String> delete(@PathVariable int userid) {
        return userService.delete(userid);
    }
}
