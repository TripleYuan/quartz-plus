package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import redcoder.quartzplus.schedcenter.repository.OperationLogRepository;
import redcoder.quartzplus.schedcenter.repository.OperationParamsRepository;

import java.util.List;

@Configuration
@Slf4j
public class OperationLogConfiguration {

    private final String interceptorName = "operationLogInterceptor";

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public OperationLogAdvisor apiLogAdvisor() {
        OperationLogAdvisor advisor = new OperationLogAdvisor();
        advisor.setAdviceBeanName(interceptorName);
        return advisor;
    }

    @Bean(interceptorName)
    public OperationLogInterceptor apiLogInterceptor(List<OperationLogRecorder> recorders) {
        OperationLogInterceptor interceptor = new OperationLogInterceptor();
        interceptor.setCreator(new DefaultOperationLogCreator());
        interceptor.setRecorder(new CompositeOperationLogRecorder(recorders));
        return interceptor;
    }

    @Bean
    public OperationLogRecorder loggerOperationLogRecorder() {
        return new LoggerOperationLogRecorder();
    }

    @Bean
    public H2DatabaseOperationLogRecorder h2DatabaseOperationLogRecorder(OperationParamsRepository paramsRepository,
                                                                         OperationLogRepository logRepository) {
        return new H2DatabaseOperationLogRecorder(logRepository, paramsRepository);
    }
}
