package com.zt.zeus.transfer.quartz.config;

import com.google.common.collect.Lists;
import com.zt.zeus.transfer.enums.JobType;
import com.zt.zeus.transfer.properties.QuartzProperties;
import com.zt.zeus.transfer.quartz.job.*;
import com.zt.zeus.transfer.quartz.listener.SimpleJobListener;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class QuartzConfig {

    @Resource(name = "quartzProperties")
    private QuartzProperties quartzProperties;


    @Bean(name = "syncPullArticleOfCustomAuthorJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfCustomAuthorJobDetail(SyncPullArticleOfCustomAuthorJob syncPullArticleOfCustomAuthorJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfCustomAuthorJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfCustomAuthorTrigger")
    public CronTriggerFactoryBean syncPullArticleOfCustomAuthorTrigger(JobDetail syncPullArticleOfCustomAuthorJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfCustomAuthorJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }


    @Bean(name = "syncPullArticleOfCustomWordJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfCustomWordJobDetail(SyncPullArticleOfCustomRelatedWordJob syncPullArticleOfCustomRelatedWordJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfCustomRelatedWordJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfCustomSiteNameJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfCustomSiteNameJobDetail(SyncPullArticleOfCustomSiteNameJob syncPullArticleOfCustomSiteNameJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfCustomSiteNameJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfCustomWordTrigger")
    public CronTriggerFactoryBean syncPullArticleOfCustomWordTrigger(JobDetail syncPullArticleOfCustomWordJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfCustomWordJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }


    @Bean(name = "syncPullArticleOfCustomSiteNameTrigger")
    public CronTriggerFactoryBean syncPullArticleOfCustomSiteNameTrigger(JobDetail syncPullArticleOfCustomSiteNameJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfCustomSiteNameJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }


    @Bean(name = "syncPullArticleOfGatherAuthorJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfGatherAuthorJobDetail(SyncPullArticleOfGatherAuthorJob syncPullArticleOfGatherAuthorJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfGatherAuthorJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfGatherAuthorTrigger")
    public CronTriggerFactoryBean syncPullArticleOfGatherAuthorTrigger(JobDetail syncPullArticleOfGatherAuthorJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfGatherAuthorJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }


    @Bean(name = "syncPullArticleOfGatherWordJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfGatherWordJobDetail(SyncPullArticleOfGatherRelatedWordJob syncPullArticleOfGatherRelatedWordJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfGatherRelatedWordJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfGatherWordTrigger")
    public CronTriggerFactoryBean syncPullArticleOfGatherWordTrigger(JobDetail syncPullArticleOfGatherWordJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfGatherWordJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }


    @Bean(name = "syncPushArticleJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPushArticleJobDetail(SyncPushArticleJob syncPushArticleJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPushArticleJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPushArticleTrigger")
    public CronTriggerFactoryBean syncPushArticleTrigger(JobDetail syncPushArticleJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPushArticleJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }

    @Bean(name = "schedulerFactory")
    public SchedulerFactoryBean schedulerFactory(
            Trigger syncPullArticleOfCustomAuthorTrigger,
            Trigger syncPullArticleOfCustomWordTrigger,
            Trigger syncPullArticleOfCustomSiteNameTrigger,
            Trigger syncPullArticleOfGatherAuthorTrigger,
            Trigger syncPullArticleOfGatherWordTrigger,
            Trigger syncPushArticleTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        if (quartzProperties.getState()) {
            List<JobType> jobType = quartzProperties.getJobType();
            List<Trigger> triggerList = Lists.newArrayList();
            if (jobType.contains(JobType.CUSTOM_AUTHOR)) {
                triggerList.add(syncPullArticleOfCustomAuthorTrigger);
            }
            if (jobType.contains(JobType.CUSTOM_WORD)) {
                triggerList.add(syncPullArticleOfCustomWordTrigger);
            }
            if (jobType.contains(JobType.CUSTOM_SITE_NAME)) {
                triggerList.add(syncPullArticleOfCustomSiteNameTrigger);
            }
            if (jobType.contains(JobType.GATHER_AUTHOR)) {
                triggerList.add(syncPullArticleOfGatherAuthorTrigger);
            }
            if (jobType.contains(JobType.GATHER_WORD)) {
                triggerList.add(syncPullArticleOfGatherWordTrigger);
            }
            if (jobType.contains(JobType.PUSH_ARTICLE)) {
                triggerList.add(syncPushArticleTrigger);
            }
            bean.setTriggers(triggerList.toArray(new Trigger[0]));
        }
        bean.setGlobalJobListeners(new SimpleJobListener());
        return bean;
    }

}
