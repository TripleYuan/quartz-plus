package redcoder.quartzplus.schedcenter.constant;

/**
 * api请求错误码定义
 */
public enum ApiStatus {

    OK(0, "ok"),

    PARAM_ERROR(1, "参数错误"),
    UNKNOWN_USER(2, "用户名或密码错误"),

    BAD_REQUEST(400, "错误的请求"),

    UNAUTHORIZED_REQUEST(401, "未登录，请登录后进行此操作"),

    SERVER_ERROR(500, "服务繁忙"),

    ;

    public final int status;
    public final String message;

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
