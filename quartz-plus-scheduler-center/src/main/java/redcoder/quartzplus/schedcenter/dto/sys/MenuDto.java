package redcoder.quartzplus.schedcenter.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuDto {

    @ApiModelProperty("菜单id")
    private int menuId;

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单类型，C: 目录，M：菜单，A：操作")
    private String menuType;

    public MenuDto() {
    }

    public MenuDto(int menuId, String menuCode, String menuName, String menuType) {
        this.menuId = menuId;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuType = menuType;
    }
}
