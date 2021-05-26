package org.leekeggs.quartzextendschedulercenter.constant;

/**
 * api请求错误码定义
 */
public enum ApiStatusEnum {

    OK(0, "ok"),

    PARAM_ERROR(1, "参数错误"),

    BAD_REQUEST(400, "错误的请求"),

    SERVER_ERROR(500, "服务繁忙"),

    ;

    private final int status;
    private final String message;

    ApiStatusEnum(int status, String message) {
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
