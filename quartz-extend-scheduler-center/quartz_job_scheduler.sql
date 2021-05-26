CREATE
DATABASE `quartz_job_scheduler` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

CREATE TABLE `quartz_scheduler_instance`
(
    `sched_name`    varchar(100) NOT NULL COMMENT 'the name of scheduler',
    `instance_host` varchar(100) NOT NULL COMMENT '实例主机地址',
    `instance_port` int(11) NOT NULL COMMENT '实例服务端口',
    PRIMARY KEY (`sched_name`, `instance_host`, `instance_port`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='注册的实例地址';

CREATE TABLE `quartz_scheduler_job_trigger_info`
(
    `sched_name`     varchar(100) NOT NULL COMMENT 'the name of scheduler',
    `job_name`       varchar(255) DEFAULT NULL COMMENT 'job名称',
    `job_group`      varchar(255) DEFAULT NULL COMMENT 'job所在组名称',
    `job_desc`       varchar(255) DEFAULT NULL COMMENT 'job的描述信息',
    `trigger_name`   varchar(100) NOT NULL COMMENT '与job相关联的触发器名称',
    `trigger_group`  varchar(100) NOT NULL COMMENT '与job相关联的触发器所在组名称',
    `trigger_desc`   varchar(255) DEFAULT NULL COMMENT '触发器的描述信息',
    `prev_fire_time` datetime     DEFAULT NULL COMMENT 'job的上一次执行时间',
    `next_fire_time` datetime     DEFAULT NULL COMMENT 'job的下一次执行时间',
    `trigger_state`  varchar(20)  DEFAULT NULL COMMENT '触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED',
    `create_time`    datetime     DEFAULT NULL,
    `update_time`    datetime     DEFAULT NULL,
    PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储quartz中的job和trigger信息';