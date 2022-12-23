package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;

import java.util.Date;

public interface UserRepository extends CrudRepository<QuartzSchedulerUser, Integer> {

    QuartzSchedulerUser findByUsername(String username);

    @Modifying
    @Query("update QuartzSchedulerUser u set u.password=?2, u.updateTime=?3 where u.userid=?1")
    @Transactional(rollbackFor = Exception.class)
    void updatePassword(int userid, String password, Date date);
}
