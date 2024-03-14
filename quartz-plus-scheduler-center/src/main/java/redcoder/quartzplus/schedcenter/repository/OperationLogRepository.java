package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationLog;

import java.util.Date;

public interface OperationLogRepository extends PagingAndSortingRepository<QuartzPlusOperationLog, Long> {

    Page<QuartzPlusOperationLog> findByUsernameAndRequestTimeBetween(String username,
                                                                     Date startTime,
                                                                     Date endTime,
                                                                     Pageable pageable);

    Page<QuartzPlusOperationLog> findByUsername(String username, Pageable pageable);

    Page<QuartzPlusOperationLog> findByRequestTimeBetween(Date startTime,
                                                          Date endTime,
                                                          Pageable pageable);
}
