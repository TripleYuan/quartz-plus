package redcoder.quartzplus.schedcenter.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogDto;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogQuery;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerOperationLog;
import redcoder.quartzplus.schedcenter.repository.OperationLogRepository;
import redcoder.quartzplus.schedcenter.service.OperationLogService;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogRepository operationLogRepository;

    @SneakyThrows
    @Override
    public PageResponse<OperationLogDto> getList(OperationLogQuery query) {
        int pageNo = query.getPageNo();
        int pageSize = query.getPageSize();
        String username = query.getUsername();
        String startTime = query.getStartTime();
        String endTime = query.getEndTime();

        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Order.desc("operationTime")));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Page<QuartzSchedulerOperationLog> page;

        if (hasText(username) && hasText(startTime) && hasText(endTime)) {
            page = operationLogRepository.findByUsernameAndOperationTimeBetween(username, df.parse(startTime), df.parse(endTime), pageRequest);
        } else if (hasText(username)) {
            page = operationLogRepository.findByUsername(username, pageRequest);
        } else if (hasText(startTime) && hasText(endTime)) {
            page = operationLogRepository.findByOperationTimeBetween(df.parse(startTime), df.parse(endTime), pageRequest);
        } else {
            page = operationLogRepository.findAll(pageRequest);
        }

        List<OperationLogDto> data = page.stream()
                .map(source -> {
                    OperationLogDto dto = new OperationLogDto();
                    BeanUtils.copyProperties(source, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }
}
