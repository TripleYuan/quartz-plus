package redcoder.quartzplus.common.exception;

/**
 * http请求执行失败，http status != 200
 */
public class HttpExecutionFailException extends RuntimeException {

    private int statusCode;

    private String responseBody;

    public HttpExecutionFailException(String message) {
        super(message);
    }

    public HttpExecutionFailException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpExecutionFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

