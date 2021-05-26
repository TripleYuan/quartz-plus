# 基于spring的quartz任务调度框架扩展

这是一款基于spring的quartz任务调度框架扩展，主要功能：

- 支持注解的方式快速定义Job、Trigger，并自动注册到Quartz Scheduler中
- 实现了一个简单quartz任务调度管理平台，支持Job的删除和手动触发、Quartz实例的管理等

## 上手指南

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("org.leekeggs.quartzextenddemo.job")
2. 在你的Job类上添加`QuartzJob`注解，配置job相关属性，目前支持配置 **jobKeyName**（默认值：类名） 、 **jobKeyGroup**（默认值：DEFAULT） 、 **jobDescription**
   （默认值：空字符串） 、 **storeDurably** （默认值：true）
3. 在你的Job类上添加`QuartzTrigger`注解，配置trigger相关属性，目前支持配置 **triggerKeyName**（默认值：类名+"Trigger""） 、 **triggerKeyGroup**（默认值：DEFAULT） 、 **triggerDescription**
   （默认值：空字符串） 、 **cronExpress** （必选属性）；*注意：目前仅支持cron类型的trigger*

## Quartz调度平台使用指南

1. 启动 **quartz-extend-scheduler-center** 应用
2. 打开quartz调度中心web页面 **job.html** （位于quartz-extend-scheduler-center-web模块目录下）

## 我的应用如何接入Quartz调度平台

1. 在spring boot的启动类或自定义配置类上，添加注解`QuartzJobScan`，指定你的job所在包，比如`@QuartzJobScan("org.leekeggs.quartzextenddemo.job")
2. 在springboot的项目配置文件中添加配置：`quartz-job-scheduler.registry.url: your scheduler center url` （即指定你的quartz调度中心应用地址）

完成上述配置后，启动你的应用，应用中的job会自动注册到quartz调度中心，重新打开quartz调度中心web页面，即可看到你的应用job.