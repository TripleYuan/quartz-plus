package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusInstanceKey;

public interface InstanceRepository
        extends PagingAndSortingRepository<QuartzPlusInstance, QuartzPlusInstanceKey> {

    Page<QuartzPlusInstance> findBySchedName(String schedName, Pageable pageable);

    QuartzPlusInstance findTopBySchedName(String schedName);
}
