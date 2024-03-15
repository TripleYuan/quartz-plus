# Quartz Plus

Quartz Plus 是一个基于 Spring 的 Quartz 任务调度框架扩展，旨在为开发者提供更简便、高效的任务调度管理解决方案。该框架不仅提供了对 Quartz 原生功能的增强，还实现了一个全功能的任务调度管理系统，帮助用户更好地管理和监控任务调度。

## 主要功能

-  **注解式任务定义**：通过简单的注解，开发者可以快速定义任务和触发器，并自动注册到 Quartz 调度器中，摆脱了硬编码创建Job和Trigger的烦恼。
-  **任务调度管理系统**：提供了一个集成的任务调度管理系统，包括作业管理、Quartz 实例管理、用户管理等功能，用户可以通过 Web 控制台方便地管理任务调度。

## 快速上手

1. 在 Spring Boot 应用的启动类或自定义配置类上，添加 `QuartzJobScan` 注解，并指定任务所在的包路径，例如 `@QuartzJobScan("com.example.jobs")`。
2. 在需要调度的任务类上，使用 `QuartzJob` 注解进行标注，配置任务相关属性，如任务名称、组别、描述等。
3. 同样在任务类上使用 `QuartzTrigger` 注解，配置触发器相关属性，目前支持 cron 表达式类型的触发器。

## Quartz 任务调度管理系统使用指南

### 本地启动演示

1. 启动调度中心服务 **quartz-plus-scheduler-center**。
2. 启动 Web 控制台 **quartz-plus-web-console**。
3. 在浏览器中打开 http://localhost:8080，即可访问任务调度管理系统，查看已注册的任务和 Quartz 实例。

系统预置了两个登录用户：

- 用户1：用户名为 `admin`，密码为 `123456`，拥有管理员权限。
- 用户2：用户名为 `quartz`，密码为 `123456`，仅具有普通用户权限。

### 接入指南

1. 在 Spring Boot 应用的启动类或自定义配置类上，添加 `QuartzJobScan` 注解，指定任务所在的包路径。
2. 在应用的配置文件中添加 Quartz 任务调度管理系统的相关配置，包括注册地址和解除注册地址。

```yaml
quartz-job-scheduler:
  registry:
    # Quartz 任务调度管理系统-注册地址
    register-url: http://localhost:32007/api/quartz-job-scheduler/instance/register
    # Quartz 任务调度管理系统-解除注册地址
    unregister-url: http://localhost:32007/api/quartz-job-scheduler/instance/unregister
```

配置完成后，启动你的应用，应用中的任务将自动注册到 Quartz 调度中心，通过 Web 控制台即可管理任务。