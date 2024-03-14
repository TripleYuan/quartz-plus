package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`quartz_plus_user`")
@Data
@Entity(name = "QuartzPlusUser")
public class QuartzPlusUser implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "`userid`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    /**
     * 用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 密码（真实密码的md5值）
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 用户类型，0-普通用户，1-管理员
     */
    @Column(name = "`user_type`")
    private Integer userType;

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