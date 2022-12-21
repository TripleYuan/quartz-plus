package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerInstanceKey;

import java.util.List;

public interface InstanceRepository
        extends PagingAndSortingRepository<QuartzSchedulerInstance, QuartzSchedulerInstanceKey> {

    Page<QuartzSchedulerInstance> findBySchedName(String schedName, Pageable pageable);

    QuartzSchedulerInstance findTopBySchedName(String schedName);
}
