# SpringBoot整合Quartz（单机模式）

#### 1. 环境搭建

   - 添加依赖
   
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
        <artifactId>springboot-quartz</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>springboot-quartz</name>
        <description>Demo project for Spring Boot</description>
    
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
    
            <!--  Quartz 依赖 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-quartz</artifactId>
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


#### 2. 相关类编写

   - 任务类
   
   ```java
    package com.ryan.springbootquartz.task;
    
    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.scheduling.quartz.QuartzJobBean;
    
    import java.util.concurrent.atomic.AtomicInteger;
    
    public class MyTask extends QuartzJobBean {
    
        private Logger logger = LoggerFactory.getLogger(getClass());
    
        private final AtomicInteger counts = new AtomicInteger();    //计算器
    
        @Override
        protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    
            //执行自定义逻辑
            logger.info("[executeInternal][定时第 ({}) 次执行]", counts.incrementAndGet());
        }
    }
   ```
   - 配置类
   
   ```java
    package com.ryan.springbootquartz.config;
    
    import com.ryan.springbootquartz.task.MyTask;
    import org.quartz.*;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class ScheduleConfiguration {
    
        public static class MyTask {
    
            @Bean
            public JobDetail myTask() {
                return JobBuilder.newJob(com.ryan.springbootquartz.task.MyTask.class)
                        .withIdentity("myTask")
                        .storeDurably(true)
                        .build();
            }
    
            @Bean
            public Trigger myTaskTigger() {
                SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever();
    
                return TriggerBuilder.newTrigger()
                        .forJob(myTask())
                        .withIdentity("myTaskTrigger")
                        .withSchedule(schedule)              //设置定时器
                        .build();
    
            }
    
        }
    }

   ```


#### 3. 测试

   - 执行结果
   
   ```text
    2020-09-22 15:38:57.044  INFO 12140 --- [           main] org.quartz.impl.StdSchedulerFactory      : Using default implementation for ThreadExecutor
    2020-09-22 15:38:57.051  INFO 12140 --- [           main] org.quartz.core.SchedulerSignalerImpl    : Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
    2020-09-22 15:38:57.051  INFO 12140 --- [           main] org.quartz.core.QuartzScheduler          : Quartz Scheduler v.2.3.2 created.
    2020-09-22 15:38:57.051  INFO 12140 --- [           main] org.quartz.simpl.RAMJobStore             : RAMJobStore initialized.
    2020-09-22 15:38:57.052  INFO 12140 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler meta-data: Quartz Scheduler (v2.3.2) 'quartzScheduler' with instanceId 'NON_CLUSTERED'
      Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
      NOT STARTED.
      Currently in standby mode.
      Number of jobs executed: 0
      Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
      Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.
    
    2020-09-22 15:38:57.052  INFO 12140 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'quartzScheduler' initialized from an externally provided properties instance.
    2020-09-22 15:38:57.052  INFO 12140 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.2
    2020-09-22 15:38:57.052  INFO 12140 --- [           main] org.quartz.core.QuartzScheduler          : JobFactory set to: org.springframework.scheduling.quartz.SpringBeanJobFactory@525575
    2020-09-22 15:38:57.073  INFO 12140 --- [           main] o.s.s.quartz.SchedulerFactoryBean        : Starting Quartz Scheduler now
    2020-09-22 15:38:57.073  INFO 12140 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler quartzScheduler_$_NON_CLUSTERED started.
    2020-09-22 15:38:57.076  INFO 12140 --- [eduler_Worker-1] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:38:57.079  INFO 12140 --- [           main] c.r.s.SpringbootQuartzApplication        : Started SpringbootQuartzApplication in 0.758 seconds (JVM running for 1.309)
    2020-09-22 15:39:01.973  INFO 12140 --- [eduler_Worker-2] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:06.973  INFO 12140 --- [eduler_Worker-3] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:11.973  INFO 12140 --- [eduler_Worker-4] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:16.972  INFO 12140 --- [eduler_Worker-5] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:21.973  INFO 12140 --- [eduler_Worker-6] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:26.973  INFO 12140 --- [eduler_Worker-7] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:31.972  INFO 12140 --- [eduler_Worker-8] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:36.974  INFO 12140 --- [eduler_Worker-9] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]
    2020-09-22 15:39:41.973  INFO 12140 --- [duler_Worker-10] com.ryan.springbootquartz.task.MyTask    : [executeInternal][定时第 (1) 次执行]

   ```


#### 4. 小结

   **为什么最后结果打印都是1**
   
