package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerRole;

public interface RoleRepository extends CrudRepository<QuartzSchedulerRole, Integer> {
}
