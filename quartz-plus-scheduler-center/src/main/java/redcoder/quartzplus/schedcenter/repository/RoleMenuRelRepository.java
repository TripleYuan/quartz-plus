package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusRoleMenuRel;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusRoleMenuRelKey;

import java.util.List;

public interface RoleMenuRelRepository extends CrudRepository<QuartzPlusRoleMenuRel, QuartzPlusRoleMenuRelKey> {

    List<QuartzPlusRoleMenuRel> findByRoleId(int roleId);

    boolean existsByRoleId(int roleId);

    @Transactional(rollbackFor = Exception.class)
    void deleteByRoleId(int roleId);
}
