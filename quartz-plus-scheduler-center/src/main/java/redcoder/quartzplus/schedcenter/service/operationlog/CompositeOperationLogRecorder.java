package redcoder.quartzplus.schedcenter.service.operationlog;

import java.util.ArrayList;
import java.util.List;

public class CompositeOperationLogRecorder implements OperationLogRecorder {

    private List<OperationLogRecorder> recorders = new ArrayList<>();

    public CompositeOperationLogRecorder(List<OperationLogRecorder> recorders) {
        this.recorders.addAll(recorders);
    }

    @Override
    public void record(OperationLog operationLog) {
        recorders.forEach(recorder -> recorder.record(operationLog));
    }

    public void addRecorder(OperationLogRecorder recorder) {
        this.recorders.add(recorder);
    }
}
