# Quartz Plus

Quartz Plus 是一个基于 Spring 的 Quartz 任务调度框架扩展，旨在为开发者提供更简便、高效的任务调度管理解决方案。该框架不仅提供了对 Quartz 原生功能的增强，还实现了一个全功能的任务调度管理系统，帮助用户更好地管理和监控任务调度。

## 核心功能

-  **注解式定义任务**：通过简单的注解，开发者可以快速定义任务和触发器，并自动注册到 Quartz 调度器中，摆脱了硬编码创建Job和Trigger的烦恼。
-  **任务调度管理系统**：提供了一个集成的任务调度管理系统，包括作业管理、Quartz 实例管理、用户管理等功能，用户可以通过 Web 控制台方便地管理任务调度。

## 快速上手

### 部署Quartz任务调度系统

调度系统分为前端和后端两个项目，可以分开部署 **quartz-plus-scheduler-center**（调度服务） 和 **quartz-plus-web-console**（前端应用），也可将前端代码打包后的静态资源文件集成到 `quartz-plus-scheduler-center` 中。

**`quartz-plus-scheduler-center` 默认使用的是H2数据库，生产环境建议使用外部数据库，如 MySQL、PostgreSQL 等。**

系统内置了两个登录用户：

> 用户1：用户名为 admin，密码为 123456，拥有管理员权限。
>
> 用户2：用户名为 quartz，密码为 123456，仅具有普通用户权限。

### 注解式定义Job和Trigger

使用 **@QuartzJob** 和 **@QuartzTrigger** 注解快速定义 `Job`和`Trigger`：

```java
@QuartzJob(description = "打印 hello world")
@QuartzTrigger(cron = "0/10 * * * * ?")
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
```

### SpringBoot应用接入

1. 引入最新版本

```xml
<dependency>
    <groupId>redcoder</groupId>
    <artifactId>quartz-plus-core</artifactId>
    <version>${quartzplus.latest.version}</version>
</dependency>
```

2. 在启用类上添加注解 `@QuartzJobScan`，并指定 job 类所在的包路径。

```java
@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.demo.job")
public class QuartzPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusDemoApplication.class, args);
    }
}
```

3. 添加配置，指定注册中心服务地址

```yaml
quartz-job-scheduler:
  registry:
    # 调度服务注册地址（替换成你的IP和端口）
    register-url: http://localhost:32007/api/quartz-job-scheduler/instance/register
    # 调度服务取消注册地址（替换成你的IP和端口）
    unregister-url: http://localhost:32007/api/quartz-job-scheduler/instance/unregister
```

