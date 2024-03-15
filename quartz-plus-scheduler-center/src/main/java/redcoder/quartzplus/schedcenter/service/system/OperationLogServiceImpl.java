package redcoder.quartzplus.schedcenter.service.system;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogInfo;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogQuery;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationLog;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationLogSpec;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationParams;
import redcoder.quartzplus.schedcenter.repository.OperationLogRepository;
import redcoder.quartzplus.schedcenter.repository.OperationParamsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    private OperationLogRepository logRepository;
    private OperationParamsRepository paramsRepository;

    @Autowired
    public OperationLogServiceImpl(OperationLogRepository logRepository, OperationParamsRepository paramsRepository) {
        this.logRepository = logRepository;
        this.paramsRepository = paramsRepository;
    }

    @SneakyThrows
    @Override
    public PageResponse<OperationLogInfo> getOperationLogs(OperationLogQuery query) {
        int pageNo = query.getPageNo();
        int pageSize = query.getPageSize();
        String username = query.getUsername();
        String startTime = query.getStartTime();
        String endTime = query.getEndTime();
        String apiPath = query.getApiPath();

        Specification<QuartzPlusOperationLog> specification = QuartzPlusOperationLogSpec.query(username, startTime, endTime, apiPath);
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Order.desc("requestTime")));
        Page<QuartzPlusOperationLog> page = logRepository.findAll(specification, pageRequest);
        List<OperationLogInfo> data = page.stream()
                .map(source -> {
                    OperationLogInfo dto = new OperationLogInfo();
                    BeanUtils.copyProperties(source, dto);
                    // 输入输出参数
                    QuartzPlusOperationParams params = paramsRepository.findById(source.getParamsId()).orElse(null);
                    if (params != null) {
                        dto.setInParams(params.getInParams());
                        dto.setOutParams(params.getOutParams());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }
}
