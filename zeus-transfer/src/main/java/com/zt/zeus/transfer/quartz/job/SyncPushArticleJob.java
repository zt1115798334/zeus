package com.zt.zeus.transfer.quartz.job;

import com.zt.zeus.transfer.service.PushService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class SyncPushArticleJob {

    @Resource
    private PushService pushService;

    public void execute() throws ExecutionException, InterruptedException {
        pushService.start();
    }
}
