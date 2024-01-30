package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobExecutionRecord;

public interface JobExecutionRecordRepository extends JpaRepository<QuartzPlusJobExecutionRecord, Long>,
        QueryByExampleExecutor<QuartzPlusJobExecutionRecord> {
}
