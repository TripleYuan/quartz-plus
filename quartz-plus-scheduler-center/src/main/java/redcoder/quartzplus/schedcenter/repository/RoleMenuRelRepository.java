package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerRoleMenuRel;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerRoleMenuRelKey;

import java.util.List;

public interface RoleMenuRelRepository extends CrudRepository<QuartzSchedulerRoleMenuRel, QuartzSchedulerRoleMenuRelKey> {

    List<QuartzSchedulerRoleMenuRel> findByRoleId(int roleId);

    boolean existsByRoleId(int roleId);

    @Transactional(rollbackFor = Exception.class)
    void deleteByRoleId(int roleId);
}
