package redcoder.quartzplus.schedcenter.entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

public class QuartzPlusOperationLogSpec {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Specification<QuartzPlusOperationLog> query(String username, String startTime, String endTime, String apiPath) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(username)) {
                predicates.add(criteriaBuilder.equal(root.get("username"), username));
            }
            if (hasText(startTime)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("requestTime"), LocalDateTime.parse(startTime, FORMATTER)));
            }
            if (hasText(endTime)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("requestTime"), LocalDateTime.parse(endTime, FORMATTER)));
            }
            if (hasText(apiPath)) {
                predicates.add(criteriaBuilder.like(root.get("apiPath"), "%" + apiPath.trim() + "%"));
            }

            if (predicates.isEmpty()) {
                return null;
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
