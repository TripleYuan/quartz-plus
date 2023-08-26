package redcoder.quartzplus.schedcenter.exception;

/**
 * 触发job失败
 *
 * @author redcoder54
 * @since 2021-04-25
 */
public class TriggerJobException extends RuntimeException {

    public TriggerJobException(String message) {
        super(message);
    }
}
