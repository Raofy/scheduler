# quartz

是一款开源作业调度库框架，使用它来创建数以万计的简单或者复杂的任务并执行，支持JTA事务和分布式。

简言之，是一个定时器或者说是一个任务调度器，定时执行创建好的任务。

最近项目中使用到quartz作为任务调度中间件，这里就进行记录一下


# 快速开始（Java篇）

#### 1. 环境搭建
  
   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.3.4.RELEASE</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.Ryan</groupId>
        <artifactId>quartz</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>quartz</name>
        <description>quartz project for Spring Boot</description>
    
        <properties>
            <java.version>1.8</java.version>
        </properties>
    
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
            </dependency>
    
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
    
            <!-- quartz-scheduler 依赖 -->
            <dependency>
                <groupId>org.quartz-scheduler</groupId>
                <artifactId>quartz</artifactId>
                <version>2.3.1</version>
            </dependency>
    
        </dependencies>
    
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    
    </project>

   ```

#### 2. 配置

   - 配置Quartz
   
        在Resource文件目录下创建quartz.properties配置文件
        
        ```properties
        org.quartz.scheduler.instanceName = MyScheduler
        org.quartz.threadPool.threadCount = 3
        org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
        ```
     
#### 3. 测试

   - 编写任务类
   
   ```java
    package com.ryan.quartz.task;
    
    import org.quartz.Job;
    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    
    public class HelloJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.err.println("hello, quartz!!!");
        }
    }
   ```

   - 测试代码
   
   ```java
    @Test
    void quartzJobTest() {
        try {
            //第一步：通过工厂模式创建一个定时器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //第二步: 开启定时器
            scheduler.start();

            // 定义工作并将其绑定到我们的HelloJob类
            JobDetail job = newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // 触发作业立即运行，然后每40秒重复一次
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(40)
                            .repeatForever())
                    .build();

            // 告诉定时器使用我们的触发器安排工作
            scheduler.scheduleJob(job, trigger);

            //延迟
            Thread.sleep(60000);

            //第三步：关闭定时器
            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
   ```

   - 执行结果
   
   ```text
    2020-09-22 14:31:43.324  INFO 6768 --- [           main] org.quartz.impl.StdSchedulerFactory      : Using default implementation for ThreadExecutor
    2020-09-22 14:31:43.337  INFO 6768 --- [           main] org.quartz.core.SchedulerSignalerImpl    : Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
    2020-09-22 14:31:43.337  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Quartz Scheduler v.2.3.1 created.
    2020-09-22 14:31:43.338  INFO 6768 --- [           main] org.quartz.simpl.RAMJobStore             : RAMJobStore initialized.
    2020-09-22 14:31:43.339  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler meta-data: Quartz Scheduler (v2.3.1) 'MyScheduler' with instanceId 'NON_CLUSTERED'
      Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
      NOT STARTED.
      Currently in standby mode.
      Number of jobs executed: 0
      Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 3 threads.
      Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.
    
    2020-09-22 14:31:43.339  INFO 6768 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'MyScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
    2020-09-22 14:31:43.339  INFO 6768 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.1
    2020-09-22 14:31:43.339  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler MyScheduler_$_NON_CLUSTERED started.
    hello, quartz!!!
    hello, quartz!!!
    2020-09-22 14:32:43.345  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler MyScheduler_$_NON_CLUSTERED shutting down.
    2020-09-22 14:32:43.345  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler MyScheduler_$_NON_CLUSTERED paused.
    2020-09-22 14:32:43.345  INFO 6768 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler MyScheduler_$_NON_CLUSTERED shutdown complete.
    
    
    Process finished with exit code 0
   ```

#### 4. 小结

   Quartz作业调度框架主要是三大组件构成
        
   - 定时器  Scheduler
   
   - 触发器  Trigger
   
   - 任务    Job
    





