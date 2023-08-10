# 基于spring的quartz任务调度框架扩展

这是Quartz任务调度管理系统的前端项目，后端项目[源码](https://github.com/redcoder54/spring-quartz-extend)

这是一款基于spring的quartz任务调度框架扩展，主要功能：

- 支持注解的方式快速定义Job、Trigger，并自动注册到Quartz Scheduler中
- 一个简单的Quartz任务调度管理系统，实现了Jo管理、Quartz实例管理、用户管理等功能

## 快速上手

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("redcoder.quartzextenddemo.job")
2. 在你的Job类上添加`QuartzJob`注解，配置job相关属性，目前支持配置 **jobKeyName**（默认值：类名） 、 **jobKeyGroup**（默认值：DEFAULT） 、 **jobDescription**
   （默认值：空字符串） 、 **storeDurably** （默认值：true）
3. 在你的Job类上添加`QuartzTrigger`注解，配置trigger相关属性，目前支持配置 **triggerKeyName**（默认值：类名+"Trigger""） 、 **triggerKeyGroup**（默认值：DEFAULT） 、 **triggerDescription**
   （默认值：空字符串） 、 **cronExpress** （必选属性）；*注意：目前仅支持cron类型的trigger*

## Quartz任务调度管理系统使用指南

我这里以本地启动的方式进行演示：

> 1. 本地启动后端项目 **quartz-extend-scheduler-center**
>   
> 2. 本地启动前端项目 **quartz-extend-scheduler-web**
>   
> 3. 进入http://localhost:8080

系统内置两个登录用户：

> - 用户1，用户名：admin, 密码：123456
> - 用户2，用户名：quartz, 密码：123456

用户1是管理员用户，拥有所有的菜单权限；用户2是普通用户，只有Job列表菜单权限。

## 我的应用如何接入Quartz任务调度管理系统

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("redcoder.quartzextenddemo.job")
2. 在springboot的项目配置文件中添加配置：
```
quartz-job-scheduler:
  registry:
    # Quartz任务调度管理系统-注册地址
    register-url: http://localhost:62000/api/quartz-job-scheduler/instance/register
    # Quartz任务调度管理系统-解除注册地址
    unregister-url: http://localhost:62000/api/quartz-job-scheduler/instance/unregister
```

完成上述配置后，启动你的应用，应用中的job会自动注册到quartz调度中心，重新打开quartz调度中心web页面，即可看到你的应用job.