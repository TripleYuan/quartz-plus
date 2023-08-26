package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerUserRoleRel;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerUserRoleRelKey;

import java.util.List;

public interface UserRoleRelRepository extends CrudRepository<QuartzSchedulerUserRoleRel, QuartzSchedulerUserRoleRelKey> {

    List<QuartzSchedulerUserRoleRel> findByUserid(int userid);

    boolean existsByUserid(int userid);

    @Transactional(rollbackFor = Exception.class)
    void deleteByUserid(int userid);
}
