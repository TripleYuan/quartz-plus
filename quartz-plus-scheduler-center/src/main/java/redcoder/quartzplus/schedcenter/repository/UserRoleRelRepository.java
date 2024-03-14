package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusUserRoleRel;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusUserRoleRelKey;

import java.util.List;

public interface UserRoleRelRepository extends CrudRepository<QuartzPlusUserRoleRel, QuartzPlusUserRoleRelKey> {

    List<QuartzPlusUserRoleRel> findByUserid(int userid);

    boolean existsByUserid(int userid);

    @Transactional(rollbackFor = Exception.class)
    void deleteByUserid(int userid);
}
