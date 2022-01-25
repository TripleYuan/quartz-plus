package org.leekeggs.quartzextendschedulercenter.exception;

/**
 * 刷新job和trigger信息失败
 *
 * @author redcoder54
 * @since 2021-04-25
 */
public class RefreshJobTriggerInfoException extends RuntimeException {

    public RefreshJobTriggerInfoException(String message) {
        super(message);
    }
}
