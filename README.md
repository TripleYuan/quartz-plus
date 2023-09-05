# Introduction

[中文](README_zh.md)

Quartz Plus is a quartz extension based spring implementation which add some enhancements for quartz:

- Use annotation to create quartz job and trigger, and these jobs and triggers will be registered into quartz scheduler automatically.
- Implement a quartz scheduler center with a web console,  you can execute several action easily, i.e. view job list, 
  execute job, change trigger's cron and so on. Furthermore, you are free to manage quartz scheduler instance by web console.

# Getting started

## Define Job and Trigger

Declare job and trigger using `@QuartzJob` and `@QuartzTrigger`.

```java
@QuartzJob(description = "print 'hello world'")
@QuartzTrigger(cron = "0/10 * * * * ?")
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    
}
```

## Configuration with Spring Boot

pom:
```pom
<dependency>
    <groupId>redcoder</groupId>
    <artifactId>quartz-plus-core</artifactId>
    <version>${quartzplus.latest.version}</version>
</dependency>
```

App class:
```java
@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.demo.job")
public class QuartzPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusDemoApplication.class, args);
    }

}
```
You can also refer [quartz-plus-demo](quartz-plus-demo) project to learn more usage.

# Quartz scheduler center with a web console

## Deploy quartz-plus-scheduler-center

It's very easy to deploy **quartz-plus-scheduler-center**.

Firstly, deploy `quartz-plus-scheduler-center`and `quartz-plus-web-console` service in your server.

Secondly, add some configuration in your spring-boot application.yaml.

Here is an example that I start scheduler-cent and web-console in my local machine:

1. Execute the command `java -jar quartz-plus-scheduler-center-1.1.0.jar` to start the `quartz-plus-scheduler-center`
2. Deploy and start the `quartz-plus-web-console`. The `quartz-plus-web-console` is a vue app, so you can deploy it using nginx
3. Open console address in your browser: http://localhost:8080

There are built-in login user:

> - non-admin user that only views job list and execute job, username: quartz, password: 123456
> 
> - admin user that has full permissions, username: admin, password: 123456

Add configuration in your spring boot application.yaml:
```yaml
quartz-job-scheduler:
  registry:
    register-url: http://localhost:62000/api/quartz-job-scheduler/instance/register
    unregister-url: http://localhost:62000/api/quartz-job-scheduler/instance/unregister
```