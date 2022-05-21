package redcoder.quartzextendschedulercenter.constant;

/**
 * api请求错误码定义
 */
public enum ApiStatus {

    OK(0, "ok"),

    PARAM_ERROR(1, "参数错误"),
    UNKNOWN_USER(2, "用户名或密码错误"),

    BAD_REQUEST(400, "错误的请求"),

    UNAUTHORIZED_REQUEST(401, "未授权的请求"),

    SERVER_ERROR(500, "服务繁忙"),

    ;

    private final int status;
    private final String message;

    ApiStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
