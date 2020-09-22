package com.ryan.springbootquartzcluster.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class ScheduleConfiguration {

//    public static class MyTask {
//
//        @Bean
//        public JobDetail myTask() {
//            return JobBuilder.newJob(com.ryan.springbootquartzcluster.task.MyTask.class)
//                    .withIdentity("myTask")
//                    .storeDurably(true)
//                    .build();
//        }
//
//        @Bean
//        public Trigger myTaskTigger() {
//            SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInSeconds(5)
//                    .repeatForever();
//
//            return TriggerBuilder.newTrigger()
//                    .forJob(myTask())
//                    .withIdentity("myTaskTrigger")
//                    .withSchedule(schedule)              //设置定时器
//                    .build();
//
//        }
//
//    }
}
