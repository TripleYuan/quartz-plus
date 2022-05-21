package redcoder.quartzextendschedulercenter.model.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`quartz_scheduler_sys_user`")
public class QuartzSchedulerSysUser {
    /**
     * 用户id
     */
    @Id
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 用户名
     */
    @Column(name = "`user_name`")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 是否为超级管理员，0-否、1-是
     */
    @Column(name = "`is_super`")
    private Boolean isSuper;

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

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取是否为超级管理员，0-否、1-是
     *
     * @return is_super - 是否为超级管理员，0-否、1-是
     */
    public Boolean getIsSuper() {
        return isSuper;
    }

    /**
     * 设置是否为超级管理员，0-否、1-是
     *
     * @param isSuper 是否为超级管理员，0-否、1-是
     */
    public void setIsSuper(Boolean isSuper) {
        this.isSuper = isSuper;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}