package redcoder.quartzextendschedulercenter.config;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redcoder.quartzextendschedulercenter.constant.ApiStatus;

import javax.servlet.ServletException;
import java.util.Objects;


/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ApiResult handleThrowable(Throwable e) {
        log.error("uncaught exception", e);

        if (e instanceof IllegalArgumentException) {
            return ApiResult.failure(ApiStatus.PARAM_ERROR.getStatus(), e.getMessage());
        }

        if (e instanceof ServletException) {
            return ApiResult.failure(ApiStatus.BAD_REQUEST.getStatus(), e.getMessage());
        }

        return ApiResult.failure(ApiStatus.SERVER_ERROR.getStatus(), ApiStatus.SERVER_ERROR.getMessage());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ApiResult handleBindException(Exception e) {
        BindingResult bindingResult;
        if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        } else {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        log.error(String.format("参数校验失败，被校验的目标对象：%s", bindingResult.getTarget()), e);

        FieldError fieldError = bindingResult.getFieldError();
        String message = "参数校验失败，";
        if (Objects.nonNull(fieldError)) {
            message += fieldError.getDefaultMessage();
        }
        return ApiResult.failure(ApiStatus.PARAM_ERROR.getStatus(), message);
    }

}
