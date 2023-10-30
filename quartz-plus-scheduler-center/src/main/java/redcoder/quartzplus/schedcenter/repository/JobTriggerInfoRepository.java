package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerJobExecutionRecord;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerJobTriggerInfoKey;

import javax.transaction.Transactional;
import java.util.List;

public interface JobTriggerInfoRepository extends JpaRepository<QuartzSchedulerJobTriggerInfo, QuartzSchedulerJobTriggerInfoKey>,
        QueryByExampleExecutor<QuartzSchedulerJobTriggerInfo> {

    @Query(value = "select distinct(u.schedName) from QuartzSchedulerJobTriggerInfo u order by u.schedName")
    List<String> findAllSchedName();

    @Transactional
    void deleteBySchedNameAndJobNameAndJobGroup(String schedName, String jobName, String jobGroup);

    @Transactional
    void deleteBySchedName(String schedName);
}
