# 介绍

quartz-plus是一个基于spring的quartz任务调度框架扩展，提供了原生quartz不具备的能力，帮助我们更好的使用quartz：

- 支持注解的方式快速定义job、trigger，并自动注册到quartz scheduler中；
- 实现了一个有管理界面的quartz调度中心，提供了job管理、quartz实例管理、用户管理等功能。

# 快速上手

## 定义Job和Trigger

使用注解`@QuartzJob`和`@QuartzTrigger`定义job和trigger.

```java
@QuartzJob(description = "打印hello world")
@QuartzTrigger(cron = "0/10 * * * * ?")
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    
}
```

## 配置Spring Boot

引入依赖:
```pom
<dependency>
    <groupId>redcoder</groupId>
    <artifactId>quartz-plus-core</artifactId>
    <version>${quartzplus.latest.version}</version>
</dependency>
```

启用quartz-plus并指定job包路径:
```java
@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.demo.job")
public class QuartzPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusDemoApplication.class, args);
    }

}
```

# Quartz任务调度中心使用指南

## 部署任务调度中心服务

要使用Quartz任务调度中心，你需要在你的服务器上部署`quartz-plus-scheduler-center`and `quartz-plus-web-console`这两个服务

我这里以本地启动的方式进行演示：

1. 在IDE中启动**quartz-plus-scheduler-center**，或者使用`java -jar quartz-plus-scheduler-center-1.1.0.jar`启动
2. 在IDE中启动**quartz-plus-web-console**，或者使用nginx部署
3. 在浏览器中打开 http://localhost:8080 即可查看已注册的实例和Job

系统内置两个登录用户：

> - 用户1，用户名：admin, 密码：123456
> - 用户2，用户名：quartz, 密码：123456

用户1是管理员用户，拥有所有的菜单权限；用户2是普通用户，只有Job列表菜单权限。

## 在spring boot项目的配置文件中添加配置

```
quartz-job-scheduler:
  registry:
    # Quartz任务调度管理系统-注册地址
    register-url: http://localhost:32007/api/quartz-job-scheduler/instance/register
    # Quartz任务调度管理系统-解除注册地址
    unregister-url: http://localhost:32007/api/quartz-job-scheduler/instance/unregister
```