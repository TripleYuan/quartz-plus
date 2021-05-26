package org.leekeggs.quartzextendschedulercenter.exception;

/**
 * 触发job失败
 *
 * @author leekeggs
 * @since 2021-04-25
 */
public class TriggerJobException extends RuntimeException {

    public TriggerJobException(String message) {
        super(message);
    }
}
