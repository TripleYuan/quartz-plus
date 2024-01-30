package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationParams;

public interface OperationParamsRepository extends JpaRepository<QuartzPlusOperationParams, Long> {
}
