# Quartz Plus

quartz-plus是一个基于spring的quartz任务调度框架扩展，提供了原生quartz不具备的能力，帮助我们更好的使用quartz：

- 支持注解的方式快速定义job、trigger，并自动注册到quartz scheduler中
- 实现了一个quartz任务调度管理系统（quartz调度中心），提供了Job管理、Quartz实例管理、用户管理等功能

## 快速上手

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("redcoder.quartzplus.job")
2. 在你的Job类上添加`QuartzJob`注解，配置job相关属性，目前支持配置 **jobKeyName**（默认值：类名） 、 **jobKeyGroup**（默认值：DEFAULT） 、 **jobDescription**
   （默认值：空字符串） 、 **storeDurably** （默认值：true）
3. 在你的Job类上添加`QuartzTrigger`注解，配置trigger相关属性，目前支持配置 **triggerKeyName**（默认值：类名+"Trigger""） 、 **triggerKeyGroup**（默认值：DEFAULT） 、 **triggerDescription**
   （默认值：空字符串） 、 **cronExpress** （必选属性）；*注意：目前仅支持cron类型的trigger*

## Quartz任务调度管理系统使用指南

我这里以本地启动的方式进行演示：

> 1.  启动调度中心服务 **quartz-plus-scheduler-center**
>
> 2.  启动web控制台 **quartz-plus-web**
>
> 3.  进入http://localhost:8080，即可查看已注册的实例和 Job

系统内置两个登录用户：

> - 用户1，用户名：admin, 密码：123456
> - 用户2，用户名：quartz, 密码：123456

用户1是管理员用户，拥有所有的菜单权限；用户2是普通用户，只有Job列表菜单权限。

## 我的应用如何接入Quartz任务调度管理系统

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("redcoder.quartzplusdemo.job")
2. 在springboot的项目配置文件中添加配置：
```
quartz-job-scheduler:
  registry:
    # Quartz任务调度管理系统-注册地址
    register-url: http://localhost:32007/api/quartz-job-scheduler/instance/register
    # Quartz任务调度管理系统-解除注册地址
    unregister-url: http://localhost:32007/api/quartz-job-scheduler/instance/unregister
```

完成上述配置后，启动你的应用，应用中的job会自动注册到quartz调度中心，重新打开quartz调度中心web页面，即可看到你的应用job.