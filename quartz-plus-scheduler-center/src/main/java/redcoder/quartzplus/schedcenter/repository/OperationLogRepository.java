package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationLog;

public interface OperationLogRepository extends PagingAndSortingRepository<QuartzPlusOperationLog, Long>,
        JpaSpecificationExecutor<QuartzPlusOperationLog> {

}
