package org.leekeggs.quartzextendcommon.exception;

/**
 * bean转map失败
 *
 * @author leekeggs
 * @since 1.0.0
 */
public class BeanToMapException extends RuntimeException {

    public BeanToMapException(String message, Throwable t) {
        super(message, t);
    }
}
