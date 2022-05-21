package redcoder.quartzextendschedulercenter.service.support;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * api path存储中心
 *
 * @author redcoder54
 * @since 2021-05-22
 */
@Component
public class ApiPathRegister {

    private final Set<ApiPath> cache = new HashSet<>();

    public synchronized void addApiPath(List<ApiPath> apiPaths) {
        cache.addAll(apiPaths);
    }

    public List<ApiPath> getApiPath(List<String> excludeList) {
        List<ApiPath> apiPaths = new ArrayList<>(cache);
        return apiPaths.stream()
                .filter(t -> !excludeList.contains(t.getPath()))
                .collect(Collectors.toList());
    }

    @Data
    public static class ApiPath {
        /**
         * api路径
         */
        private String path;

        /**
         * api描述
         */
        private String description;

        public ApiPath(String path, String description) {
            this.path = path;
            this.description = description;
        }
    }
}
