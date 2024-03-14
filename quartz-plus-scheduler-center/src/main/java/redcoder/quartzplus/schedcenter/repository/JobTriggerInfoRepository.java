package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobTriggerInfoKey;

import javax.transaction.Transactional;
import java.util.List;

public interface JobTriggerInfoRepository extends JpaRepository<QuartzPlusJobTriggerInfo, QuartzPlusJobTriggerInfoKey>,
        QueryByExampleExecutor<QuartzPlusJobTriggerInfo> {

    @Query(value = "select distinct(u.schedName) from QuartzSchedulerJobTriggerInfo u order by u.schedName")
    List<String> findAllSchedName();

    @Transactional
    void deleteBySchedNameAndJobNameAndJobGroup(String schedName, String jobName, String jobGroup);

    @Transactional
    void deleteBySchedName(String schedName);
}
