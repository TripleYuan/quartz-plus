package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerInstance;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerInstanceKey;

public interface InstanceRepository
        extends PagingAndSortingRepository<QuartzSchedulerInstance, QuartzSchedulerInstanceKey> {

    Page<QuartzSchedulerInstance> findBySchedName(String schedName, Pageable pageable);

    QuartzSchedulerInstance findTopBySchedName(String schedName);
}
