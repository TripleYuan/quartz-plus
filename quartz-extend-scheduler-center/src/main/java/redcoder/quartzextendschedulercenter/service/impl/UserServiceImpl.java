package redcoder.quartzextendschedulercenter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzextendschedulercenter.constant.ApiStatus;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.sys.ModifyPasswordReq;
import redcoder.quartzextendschedulercenter.dto.sys.UserDto;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;
import redcoder.quartzextendschedulercenter.repository.UserRepository;
import redcoder.quartzextendschedulercenter.service.UserPermissionService;
import redcoder.quartzextendschedulercenter.service.UserService;
import redcoder.quartzextendschedulercenter.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository repository;
    @Resource
    private UserPermissionService manageService;

    @Override
    public List<UserDto> getList() {
        List<UserDto> data = new ArrayList<>();
        repository.findAll().forEach(t -> {
            UserDto dto = new UserDto(t.getUserid(), t.getUsername(), t.getUserType());
            data.add(dto);
        });
        return data;
    }

    @Override
    public ApiResult<String> addOrUpdate(UserDto dto) {
        Integer userid = dto.getUserid();
        if (userid == null) {
            // add
            QuartzSchedulerUser user = new QuartzSchedulerUser();
            user.setUsername(dto.getUsername());
            user.setUserType(dto.getUserType());
            user.setPassword(DEFAULT_PASSWORD);
            repository.save(user);
        } else {
            Optional<QuartzSchedulerUser> optional = repository.findById(userid);
            if (optional.isPresent()) {
                QuartzSchedulerUser user = optional.get();
                user.setUserType(dto.getUserType());
                user.setUsername(dto.getUsername());
                user.setUpdateTime(new Date());
                repository.save(user);
            } else {
                return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "用户不存在");
            }
        }
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> delete(int userid) {
        // 删除用户
        repository.deleteById(userid);
        // 删除用户的角色
        manageService.delete(userid);

        return ApiResult.success();
    }

    @Override
    public ApiResult<String> modifyPassword(ModifyPasswordReq req) {
        int userid = ShiroUtils.getUserId();
        Optional<QuartzSchedulerUser> optional = repository.findById(userid);
        if (!optional.isPresent()) {
            return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "用户不存在");
        }
        QuartzSchedulerUser user = optional.get();
        if (!Objects.equals(req.getOldPwd(), user.getPassword())) {
            return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "旧密码错误");
        }
        repository.updatePassword(userid, req.getNewPwd(), new Date());
        return ApiResult.success();
    }
}
