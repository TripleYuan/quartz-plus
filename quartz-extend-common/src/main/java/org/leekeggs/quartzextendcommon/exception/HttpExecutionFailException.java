package org.leekeggs.quartzextendcommon.exception;

/**
 * http请求执行失败，http status != 200
 *
 * @author leekeggs
 * @since 1.0.0
 */
public class HttpExecutionFailException extends RuntimeException {

    public HttpExecutionFailException(String message) {
        super(message);
    }
}
