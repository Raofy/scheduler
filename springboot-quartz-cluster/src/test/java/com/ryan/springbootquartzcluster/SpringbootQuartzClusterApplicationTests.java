package com.ryan.springbootquartzcluster;

import com.ryan.springbootquartzcluster.task.MyTask;
import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootQuartzClusterApplication.class)
class SpringbootQuartzClusterApplicationTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    void contextLoads() {
    }

    @Test
    void springBootQuartzTest() throws SchedulerException {
        // 创建 JobDetail
        JobDetail jobDetail = JobBuilder.newJob(MyTask.class)
                .withIdentity("myTask") // 名字为 demoJob01
                .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
                .build();
        // 创建 Trigger
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5) // 频率。
                .repeatForever(); // 次数。
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail) // 对应 Job 为 demoJob01
                .withIdentity("myTaskTrigger") // 名字为 demoJob01Trigger
                .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
                .build();
        // 添加调度任务
        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
    }

}
