package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.PasswordUpdate;
import redcoder.quartzplus.schedcenter.dto.system.UserInfo;
import redcoder.quartzplus.schedcenter.service.system.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "用户")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    @ApiOperation("获取所有用户")
    public ApiResult<List<UserInfo>> getUsers() {
        List<UserInfo> data = userService.getUsers();
        return ApiResult.success(data);
    }

    @PostMapping("/api/user")
    @ApiOperation("新增|更新用户")
    public ApiResult<String> addOrUpdate(@RequestBody @Valid UserInfo info) {
        return userService.addOrUpdate(info);
    }

    @DeleteMapping("/api/user/{userid}")
    @ApiOperation("删除角色")
    public ApiResult<String> delete(@PathVariable int userid) {
        return userService.delete(userid);
    }

    @PostMapping("/password")
    @ApiOperation("修改密码")
    public ApiResult<String> updatePassword(@RequestBody @Valid PasswordUpdate passwordUpdate) {
        return userService.updatePassword(passwordUpdate);
    }
}
