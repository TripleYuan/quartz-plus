package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRole;

public interface RoleRepository extends CrudRepository<QuartzPlusRole, Integer> {
}
