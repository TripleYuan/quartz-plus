package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerUserRoleRelKey;

import java.util.List;

public interface UserRoleRelRepository extends CrudRepository<QuartzSchedulerUserRoleRel, QuartzSchedulerUserRoleRelKey> {

    List<QuartzSchedulerUserRoleRel> findByUserid(int userid);
}
