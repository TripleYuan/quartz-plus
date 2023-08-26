package redcoder.quartzplus.schedcenter.dto;

import io.swagger.annotations.ApiModelProperty;

public class BasePageRequest {

    @ApiModelProperty("页码，默认：1")
    private int pageNo = 1;

    @ApiModelProperty("页大小，默认：10")
    private int pageSize = 10;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
