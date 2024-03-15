package redcoder.quartzplus.schedcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobInfoKey;

import javax.transaction.Transactional;
import java.util.List;

public interface JobInfoRepository extends JpaRepository<QuartzPlusJobInfo, QuartzPlusJobInfoKey>,
        QueryByExampleExecutor<QuartzPlusJobInfo>, JpaSpecificationExecutor<QuartzPlusJobInfo> {

    @Query(value = "select distinct(u.schedName) from QuartzPlusJobInfo u order by u.schedName")
    List<String> findAllSchedName();

    @Transactional
    void deleteBySchedNameAndJobNameAndJobGroup(String schedName, String jobName, String jobGroup);

    @Transactional
    void deleteBySchedName(String schedName);
}
