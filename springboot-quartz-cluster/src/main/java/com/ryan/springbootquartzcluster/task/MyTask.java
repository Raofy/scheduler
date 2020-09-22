package com.ryan.springbootquartzcluster.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.atomic.AtomicInteger;


@DisallowConcurrentExecution      //实现在相同 Quartz Scheduler 集群中，相同 JobKey 的 JobDetail ，保证在多个 JVM 进程中，有且仅有一个节点在执行。
public class MyTask extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();    //计算器

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //执行自定义逻辑
        logger.info("[executeInternal][定时第 ({}) 次执行]", counts.incrementAndGet());
    }
}
