package com.ryan.quartz;

import com.ryan.quartz.task.HelloJob;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootTest
class QuartzApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void quartzTest() {
        try {
            //第一步：通过工厂模式创建一个定时器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //第二步: 开启定时器
            scheduler.start();

            //第三步：关闭定时器
            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


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

}
