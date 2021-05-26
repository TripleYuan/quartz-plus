package org.leekeggs.quartzextendcommon.exception;

/**
 * Bean和Json字符串转换异常
 *
 * @author leekeggs
 * @since 1.0.0
 */
public class JacksonApiException extends RuntimeException {

    public JacksonApiException(String message) {
        super(message);
    }
}
