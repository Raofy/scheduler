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
