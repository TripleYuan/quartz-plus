CREATE
    DATABASE `quartz_job_scheduler` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

-- 注册的quartz实例
drop table if exists quartz_scheduler_instance;
CREATE TABLE `quartz_scheduler_instance`
(
    `sched_name`    varchar(100) NOT NULL COMMENT 'the name of scheduler',
    `instance_host` varchar(100) NOT NULL COMMENT '实例主机地址',
    `instance_port` int          NOT NULL COMMENT '实例服务端口',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`sched_name`, `instance_host`, `instance_port`)
);

-- 存储quartz中的job和trigger信息
drop table if exists quartz_scheduler_job_trigger_info;
CREATE TABLE `quartz_scheduler_job_trigger_info`
(
    `sched_name`         varchar(100) NOT NULL COMMENT 'the name of scheduler',
    `job_name`           varchar(255)  DEFAULT NULL COMMENT 'job名称',
    `job_group`          varchar(255)  DEFAULT NULL COMMENT 'job所在组名称',
    `job_desc`           varchar(255)  DEFAULT NULL COMMENT 'job的描述信息',
    `trigger_name`       varchar(100) NOT NULL COMMENT '与job相关联的触发器名称',
    `trigger_group`      varchar(100) NOT NULL COMMENT '与job相关联的触发器所在组名称',
    `trigger_desc`       varchar(255)  DEFAULT NULL COMMENT '触发器的描述信息',
    `prev_fire_time`     datetime      DEFAULT NULL COMMENT 'job的上一次执行时间',
    `next_fire_time`     datetime      DEFAULT NULL COMMENT 'job的下一次执行时间',
    `trigger_state`      varchar(20)   DEFAULT NULL COMMENT '触发器的状态',
    `trigger_state_desc` varchar(20)   DEFAULT NULL COMMENT '触发器的状态描述',
    `cron`               varchar(1000) default null COMMENT 'cron expression',
    `create_time`        datetime      DEFAULT CURRENT_TIMESTAMP,
    `update_time`        datetime      DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
);

-- 菜单
drop table if exists quartz_scheduler_menu;
create table quartz_scheduler_menu
(
    `menu_id`     int auto_increment not null comment '菜单id',
    `menu_code`   varchar(100)       not null comment '菜单编码',
    `menu_name`   varchar(100)       not null comment '菜单名称',
    `menu_type`   varchar(100)       not null comment '菜单类型，C: 目录，M：菜单，A：操作',
    `parent_id`   int                not null default 0 comment '父菜单id，0：表示无父菜单，即一级菜单',
    `menu_status` int                not null default 1 comment '菜单状态，0-隐藏，1-显示',
    `create_time` datetime                    DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                    DEFAULT CURRENT_TIMESTAMP,
    primary key (`menu_id`),
    constraint quartz_scheduler_menu_uidx_menu_code unique (`menu_code`)
);
-- 添加菜单
insert into quartz_scheduler_menu(`menu_id`, `menu_code`, `menu_name`, `menu_type`, `parent_id`)
values (1, 'sys-manage', '系统管理', 'C', 0),
       (2, 'user-manage', '用户管理', 'M', 1),
       (3, 'role-manage', '角色管理', 'M', 1),
       (4, 'job-list', '任务列表', 'M', 0),
       (5, 'job-manage', '任务管理', 'M', 0),
       (6, 'instance-manage', '实例管理', 'M', 0);

-- 用户
drop table if exists quartz_scheduler_user;
create table quartz_scheduler_user
(
    `userid`      int auto_increment not null comment '用户id',
    `username`    varchar(20)        not null comment '用户名',
    `password`    varchar(50)        not null comment '密码（真实密码的md5值）',
    `user_type`   int                not null default 0 comment '用户类型，0-普通用户，1-管理员',
    `create_time` datetime                    DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                    DEFAULT CURRENT_TIMESTAMP,
    primary key (`userid`),
    constraint quartz_scheduler_menu_uidx_username unique (`username`)
);
-- 添加用户
insert into quartz_scheduler_user(`userid`, `username`, `password`, `user_type`)
values (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1),
       (2, 'quartz', 'e10adc3949ba59abbe56e057f20f883e', 0);

-- 角色表
drop table if exists quartz_scheduler_role;
create table quartz_scheduler_role
(
    role_id       int auto_increment not null comment '角色id',
    role_name     varchar(20)        not null comment '用户名',
    role_desc     varchar(20)        not null comment '角色描述',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
    primary key (role_id)
);
-- 添加角色
insert into quartz_scheduler_role(role_id, role_name, role_desc)
values (1, '普通用户', '只能查看任务列表的用户');

-- 用户角色表
drop table if exists quartz_scheduler_user_role_rel;
create table quartz_scheduler_user_role_rel
(
    userid        int not null comment '用户id',
    role_id       int not null comment '角色id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
    primary key (userid, role_id)
);
-- 添加用户角关系
insert into quartz_scheduler_user_role_rel(userid, role_id)
values (2, 1);

-- 角色菜单关系表
drop table if exists quartz_scheduler_role_menu_rel;
create table quartz_scheduler_role_menu_rel
(
    role_id       int not null comment '角色id',
    menu_id       int not null comment '菜单id',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
    primary key (role_id, menu_id)
);
-- 添加角色菜单关系
insert into quartz_scheduler_role_menu_rel(role_id, menu_id)
values (1, 4);

-- 操作日志表
drop table if exists quartz_scheduler_operation_log;
create table quartz_scheduler_operation_log
(
    id             bigint auto_increment,
    userid         int                                null comment '操作人用户id',
    `username`     varchar(20)                        not null comment '用户名',
    controller     varchar(200)                       not null comment '访问的controller名称',
    method         varchar(100)                       not null comment '访问的方法名',
    api_desc       varchar(100)                       null comment '访问的接口信息',
    operation_time datetime default CURRENT_TIMESTAMP null comment '操作时间',
    primary key (id)
)