package redcoder.quartzextendschedulercenter.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import redcoder.quartzextendschedulercenter.constant.ApiStatus;

import static redcoder.quartzextendschedulercenter.constant.ApiStatus.OK;

@Getter
@Setter
@ApiModel("api请求结果模型")
public class ApiResult<T> {

    @ApiModelProperty("请求结果码，0：请求成功，其它：失败")
    private Integer status;

    @ApiModelProperty("请求结果描述")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    public ApiResult() {
    }

    public ApiResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static <T> ApiResult<T> success() {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(OK.getStatus());
        result.setMessage(OK.getMessage());
        return result;
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(OK.getStatus());
        result.setMessage(OK.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> ApiResult<T> failure(int code, String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setStatus(code);
        result.setMessage(message);
        return result;
    }

    public static <T> ApiResult<T> failure(ApiStatus apiStatus) {
        return new ApiResult<>(apiStatus.getStatus(), apiStatus.getMessage());
    }
}
