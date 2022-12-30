package redcoder.quartzextendcore.core;

import java.util.Objects;

public enum QuartzTriggerState {
    NONE("NONE","未知"),
    NORMAL("NORMAL","正常"),
    PAUSED("PAUSED","暂停"),
    COMPLETE("COMPLETE","完成"),
    ERROR("ERROR","错误"),
    BLOCKED("BLOCKED", "阻塞");

    public final String code;
    public final String desc;

    QuartzTriggerState(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (QuartzTriggerState state : values()) {
            if (Objects.equals(code, state.code)) {
                return state.desc;
            }
        }
        throw new RuntimeException("未知的code：" + code);
    }
}
