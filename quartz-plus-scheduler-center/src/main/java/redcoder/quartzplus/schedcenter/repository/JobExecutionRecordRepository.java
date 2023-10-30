package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerJobExecutionRecord;

public interface JobExecutionRecordRepository extends JpaRepository<QuartzSchedulerJobExecutionRecord, Long>,
        QueryByExampleExecutor<QuartzSchedulerJobExecutionRecord> {
}
