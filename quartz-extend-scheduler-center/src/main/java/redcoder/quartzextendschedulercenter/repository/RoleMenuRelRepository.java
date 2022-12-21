package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRoleMenuRel;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerRoleMenuRelKey;

import java.util.List;

public interface RoleMenuRelRepository extends CrudRepository<QuartzSchedulerRoleMenuRel, QuartzSchedulerRoleMenuRelKey> {

    List<QuartzSchedulerRoleMenuRel> findByRoleId(int roleId);
}
