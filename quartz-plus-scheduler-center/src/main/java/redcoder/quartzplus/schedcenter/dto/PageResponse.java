package redcoder.quartzplus.schedcenter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    @ApiModelProperty("总数")
    private long total;

    @ApiModelProperty("页数")
    private int pages;

    @ApiModelProperty("页码")
    private int pageNo;

    @ApiModelProperty("页大小")
    private int pageSize;

    @ApiModelProperty("数据")
    private List<T> data;

    public PageResponse(long total, int pageNo, int pageSize, List<T> data) {
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.data = data;
        this.pages = (int) ((total % pageSize == 0) ? total / pageSize : (total / pageSize + 1));
    }
}
