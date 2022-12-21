package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerRole;

public interface RoleRepository extends CrudRepository<QuartzSchedulerRole, Integer> {
}
