package org.leekeggs.quartzextendcore.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author leekeggs
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class QuartzApiResult<T> {
    /**
     * 0: 请求成功、其它：失败
     */
    private int status;
    private String message;
    private T data;

    public QuartzApiResult(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public QuartzApiResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public QuartzApiResult() {
    }
}
