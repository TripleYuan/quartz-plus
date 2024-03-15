# Quartz Plus

[中文](README_zh.md)

Quartz Plus is an extension of the Quartz task scheduling framework based on Spring, designed to address the lack of annotation support and a dedicated task scheduling management interface in native Quartz. By supporting annotation for rapid Job and Trigger definition and providing a Task Scheduling Management Center, Quartz Plus offers a more convenient and efficient task scheduling management solution.

## Key Features

- **Annotation Support**: Define Jobs and Triggers quickly using annotations, automatically registered into the Quartz Scheduler.
- **Task Scheduling Management Center**: Implements a management interface for job management, Quartz instance management, user management, etc.

## Getting Started

### Define Job and Trigger

Use `@QuartzJob` and `@QuartzTrigger` annotations to define Job and Trigger, as shown below:

```java
@QuartzJob(description = "Print hello world")
@QuartzTrigger(cron = "0/10 * * * * ?")
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
```

### Configure Spring Boot

In the Spring Boot project configuration file, add the dependency and enable Quartz Plus by specifying the job package path, as shown below:

```xml
<dependency>
    <groupId>redcoder</groupId>
    <artifactId>quartz-plus-core</artifactId>
    <version>${quartzplus.latest.version}</version>
</dependency>
```

```java
@SpringBootApplication
@QuartzJobScan("redcoder.quartzplus.demo.job")
public class QuartzPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzPlusDemoApplication.class, args);
    }
}
```

## Using Quartz Task Scheduling Management Center

To use the Quartz Task Scheduling Management Center, you need to deploy `quartz-plus-scheduler-center` and `quartz-plus-web-console` on your server.

### Local Startup Demonstration

Start `quartz-plus-scheduler-center` in your IDE, or use `java -jar quartz-plus-scheduler-center-{lastest-version}.jar` to start it.

Start `quartz-plus-web-console` in your IDE, or deploy it using nginx.

Open [http://localhost:8080](http://localhost:8080) in your browser to view the registered instances and jobs.

The system comes with two predefined login users:

- User 1: Username `admin`, Password `123456`, with administrator permissions.
- User 2: Username `quartz`, Password `123456`, with only Job list menu permissions.

Add the following configuration to the Spring Boot project configuration file:

```yaml
quartz-job-scheduler:
  registry:
    # Quartz Task Scheduling Management System - Registration URL
    register-url: http://localhost:32007/api/quartz-job-scheduler/instance/register
    # Quartz Task Scheduling Management System - Unregister URL
    unregister-url: http://localhost:32007/api/quartz-job-scheduler/instance/unregister
```