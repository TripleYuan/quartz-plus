package redcoder.quartzextendschedulercenter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "quartz_scheduler_menu")
public class QuartzSchedulerMenu {

    /**
     * 角色id
     */
    @Id
    @Column(name = "`menu_id`")
    @GeneratedValue(generator = "JDBC")
    private int menuId;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 菜单类型，C: 目录，M：菜单，A：操作
     */
    @Column(name = "menu_type")
    private String menuType;

    /**
     * 父菜单id，0：表示无父菜单，即一级菜单
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 是否显示
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;
}
