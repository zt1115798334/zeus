package com.zt.zeus.transfer.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.enums.StorageMode;
import com.zt.zeus.transfer.enums.TimeType;
import com.zt.zeus.transfer.handler.SyncPullArticleHandler;
import com.zt.zeus.transfer.properties.QuartzProperties;
import com.zt.zeus.transfer.utils.DateUtils;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

@Slf4j
@Component
@EnableScheduling
public class SyncPullArticleOfCustomAuthorJob {
    @Resource(name = "customAuthorsByDateRange")
    private SyncPullArticleHandler.CustomAuthorsByDateRange customAuthorsByDateRange;

    @Resource(name = "customAuthorsByTimeRange")
    private SyncPullArticleHandler.CustomAuthorsByTimeRange customAuthorsByTimeRange;

    @Resource
    private QuartzProperties quartzProperties;

    public void execute() {
        TimeType timeType = quartzProperties.getTimeType();
        Integer timeRange = quartzProperties.getTimeRange();
        long count;
        DateUtils.DateRange dateRange = DateUtils.intervalTimeCoverTimeType(timeType, timeRange);
        if (Objects.equal(timeType, TimeType.YEAR) ||
                Objects.equal(timeType, TimeType.MONTH) ||
                Objects.equal(timeType, TimeType.DAY)) {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDate", dateRange.getStartDate());
            extraParams.put("endDate", dateRange.getEndDate());
            count = customAuthorsByDateRange.handlerData(extraParams);
        } else {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDateTime", dateRange.getStartDateTime());
            extraParams.put("endDateTime", dateRange.getEndDateTime());
            extraParams.put("fromType", DateUtils.formatDate(LocalDate.now()));
            count = customAuthorsByTimeRange.handlerData(extraParams);
        }
        log.info("exec num: {}", count);
    }

}
