package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerJobTriggerInfoKey;

import javax.transaction.Transactional;
import java.util.List;

public interface JobTriggerInfoRepository
        extends PagingAndSortingRepository<QuartzSchedulerJobTriggerInfo, QuartzSchedulerJobTriggerInfoKey> {

    @Query(value = "select distinct(u.schedName) from QuartzSchedulerJobTriggerInfo u order by u.schedName")
    List<String> findAllSchedName();

    Page<QuartzSchedulerJobTriggerInfo> findBySchedNameAndJobNameLike(String schedName, String jobName, Pageable pageable);

    Page<QuartzSchedulerJobTriggerInfo> findBySchedName(String schedName, Pageable pageable);

    Page<QuartzSchedulerJobTriggerInfo> findByJobNameLike(String jobName, Pageable pageable);

    @Transactional
    void deleteBySchedNameAndJobNameAndJobGroup(String schedName, String jobName, String jobGroup);

    @Transactional
    void deleteBySchedName(String schedName);
}
