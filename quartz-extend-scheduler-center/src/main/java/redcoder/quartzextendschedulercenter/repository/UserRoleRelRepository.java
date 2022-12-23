package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerUserRoleRelKey;

import java.util.List;

public interface UserRoleRelRepository extends CrudRepository<QuartzSchedulerUserRoleRel, QuartzSchedulerUserRoleRelKey> {

    List<QuartzSchedulerUserRoleRel> findByUserid(int userid);

    boolean existsByUserid(int userid);

    @Transactional(rollbackFor = Exception.class)
    void deleteByUserid(int userid);
}
