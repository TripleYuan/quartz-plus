package redcoder.quartzplus.schedcenter.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.constant.ApiStatus;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.system.PasswordUpdate;
import redcoder.quartzplus.schedcenter.dto.system.UserInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUser;
import redcoder.quartzplus.schedcenter.repository.UserRepository;
import redcoder.quartzplus.schedcenter.shiro.ShiroUtils;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserPermissionService manageService;

    public UserServiceImpl(UserRepository repository, UserPermissionService manageService) {
        this.repository = repository;
        this.manageService = manageService;
    }

    @Override
    public List<UserInfo> getUsers() {
        List<UserInfo> data = new ArrayList<>();
        repository.findAll().forEach(t -> {
            UserInfo dto = new UserInfo(t.getUserid(), t.getUsername(), t.getUserType());
            data.add(dto);
        });
        return data;
    }

    @Override
    public ApiResult<String> addOrUpdate(UserInfo info) {
        Integer userid = info.getUserid();
        if (userid == null) {
            // add
            QuartzPlusUser user = new QuartzPlusUser();
            user.setUsername(info.getUsername());
            user.setUserType(info.getUserType());
            user.setPassword(DEFAULT_PASSWORD);
            repository.save(user);
        } else {
            Optional<QuartzPlusUser> optional = repository.findById(userid);
            if (optional.isPresent()) {
                QuartzPlusUser user = optional.get();
                user.setUserType(info.getUserType());
                user.setUsername(info.getUsername());
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
    public ApiResult<String> updatePassword(PasswordUpdate passwordUpdate) {
        int userid = ShiroUtils.getUserId();
        Optional<QuartzPlusUser> optional = repository.findById(userid);
        if (!optional.isPresent()) {
            return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "用户不存在");
        }
        QuartzPlusUser user = optional.get();
        if (!Objects.equals(passwordUpdate.getOldPwd(), user.getPassword())) {
            return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), "旧密码错误");
        }
        repository.updatePassword(userid, passwordUpdate.getNewPwd(), new Date());
        return ApiResult.success();
    }
}
