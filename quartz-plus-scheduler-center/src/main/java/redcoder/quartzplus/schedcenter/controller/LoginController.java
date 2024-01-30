package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.login.LoginDTO;
import redcoder.quartzplus.schedcenter.service.system.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static redcoder.quartzplus.schedcenter.constant.ApiStatus.UNKNOWN_USER;

/**
 * @author redcoder54
 * @since 2022-01-11
 */
@RestController
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/login")
    @ApiOperation("登录")
    public ApiResult<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        if (loginService.login(loginDTO)) {
            return ApiResult.success("ok");
        } else {
            return ApiResult.failure(UNKNOWN_USER);
        }
    }

    @PostMapping("/api/logout")
    @ApiOperation("登出")
    public ApiResult<String> logout() {
        loginService.logout();
        return ApiResult.success("ok");
    }
}
