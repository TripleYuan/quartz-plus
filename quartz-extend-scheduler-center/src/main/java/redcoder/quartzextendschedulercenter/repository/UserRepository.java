package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerUser;

public interface UserRepository extends CrudRepository<QuartzSchedulerUser, Integer> {

    QuartzSchedulerUser findByUsername(String username);
}
