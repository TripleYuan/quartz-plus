package redcoder.quartzextendschedulercenter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerOperationLog;

import java.util.Date;

public interface OperationLogRepository extends PagingAndSortingRepository<QuartzSchedulerOperationLog, Long> {

    Page<QuartzSchedulerOperationLog> findByUsernameAndOperationTimeBetween(String username,
                                                                            Date startTime,
                                                                            Date endTime,
                                                                            Pageable pageable);

    Page<QuartzSchedulerOperationLog> findByUsername(String username, Pageable pageable);

    Page<QuartzSchedulerOperationLog> findByOperationTimeBetween(Date startTime,
                                                                 Date endTime,
                                                                 Pageable pageable);
}
