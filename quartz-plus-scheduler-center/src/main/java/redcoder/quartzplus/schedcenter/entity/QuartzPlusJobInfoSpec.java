package redcoder.quartzplus.schedcenter.entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

import static org.springframework.util.StringUtils.hasText;

public class QuartzPlusJobInfoSpec {

    public static Specification<QuartzPlusJobInfo> jobQuery(String schedName, String search) {
        return (root, query, builder) -> {
            Predicate predicate_schedName = null;
            Predicate predicate_search = null;

            if (hasText(schedName)) {
                predicate_schedName = builder.equal(root.get("schedName"), schedName);
            }

            if (hasText(search)) {
                Predicate predicate_jobName = builder.like(root.get("jobName"), "%" + search + "%");
                Predicate predicate_jobDesc = builder.like(root.get("jobDesc"), "%" + search + "%");
                predicate_search = builder.or(predicate_jobName, predicate_jobDesc);
            }

            if (predicate_schedName != null && predicate_search != null) {
                return builder.and(predicate_schedName, predicate_search);
            } else if (predicate_schedName != null) {
                return predicate_schedName;
            } else if (predicate_search != null) {
                return predicate_search;
            } else {
                return null;
            }
        };
    }
}
