package com.zt.zeus.transfer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.google.common.util.concurrent.RateLimiter;
import com.zt.zeus.transfer.analysis.service.AnalysisService;
import com.zt.zeus.transfer.custom.CustomPage;
import com.zt.zeus.transfer.custom.RichParameters;
import com.zt.zeus.transfer.dto.FileInfoDto;
import com.zt.zeus.transfer.enums.StorageMode;
import com.zt.zeus.transfer.es.domain.EsArticle;
import com.zt.zeus.transfer.es.service.EsArticleService;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.service.PullService;
import com.zt.zeus.transfer.service.callable.SendInInterface;
import com.zt.zeus.transfer.service.callable.WriteInLocal;
import com.zt.zeus.transfer.utils.DateUtils;
import com.zt.zeus.transfer.utils.TheadUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PullServiceImpl implements PullService {

    private final EsArticleService esArticleService;

    private final EsProperties esProperties;

    private final AnalysisService analysisService;

    /**
     * 一天一天查询
     *
     * @param richParameters 丰富参数
     * @param words          词
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @return 数量
     */
    @Override
    public long pullEsArticleByDateRange(RichParameters richParameters, List<String> words,
                                         LocalDate startDate, LocalDate endDate) {
        String fromType = richParameters.getFromType();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        JSONArray wordJa = JSONArray.parseArray(JSONArray.toJSONString(words));
        List<LocalDate> localDates = DateUtils.dateRangeList(startDate, endDate);
        List<Future<Long>> collect = localDates.stream().map(localDate -> {
            String mapKey = "day_" + fromType + "_" + DateUtils.formatDate(localDate);
            return executorService.submit(new PullArticleHandle(richParameters, esArticleService, analysisService, wordJa,
                    localDate.atTime(LocalTime.of(0, 0, 0)),
                    localDate.atTime(LocalTime.of(23, 59, 59)),
                    esProperties.getPageSize(), mapKey, esProperties.getFilePath()));
        }).collect(Collectors.toList());
        executorService.shutdown();
        return collect.stream().map(TheadUtils::getFutureLong).mapToLong(Long::longValue).sum();
    }

    /**
     * 时间范围查询
     *
     * @param richParameters 丰富参数
     * @param words          词
     * @param startDateTime  开始时间
     * @param endDateTime    结束时间
     * @return 数量
     */
    @Override
    public long pullEsArticleByTimeRange(RichParameters richParameters, List<String> words, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String fromType = richParameters.getFromType();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        JSONArray wordJa = JSONArray.parseArray(JSONArray.toJSONString(words));
        String mapKey = "timeRange_" + fromType;
        Future<Long> submit = executorService.submit(new PullArticleHandle(richParameters, esArticleService, analysisService, wordJa, startDateTime, endDateTime, esProperties.getPageSize(), mapKey, esProperties.getFilePath()));
        executorService.shutdown();
        return TheadUtils.getFutureLong(submit);
    }


    @AllArgsConstructor
    @Slf4j
    public static class PullArticleHandle implements Callable<Long> {

        private final RichParameters richParameters;

        private final EsArticleService esArticleService;

        private final AnalysisService analysisService;

        private final JSONArray wordJa;

        private final LocalDateTime startDateTime;

        private final LocalDateTime endDateTime;

        private final int pageSize;

        private final String mapKey;

        private final String filePath;

        @Override
        public Long call() {
            StorageMode storageMode = richParameters.getStorageMode();
            ExecutorService executorService = Executors.newFixedThreadPool(3);

            ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(1000);
            AtomicInteger atomicInteger = new AtomicInteger();
            AtomicInteger atomicPage = new AtomicInteger();

            AtomicLong atomicLong = new AtomicLong();
            while (true) {
                long runStart = System.currentTimeMillis();
                long maxRequestTime = Long.MIN_VALUE;
                long minRequestTime = Long.MAX_VALUE;
                long totalRequestTime = 0;

                String scrollId = concurrentHashMap.getOrDefault(mapKey, StringUtils.EMPTY);
                CustomPage<EsArticle> allDataEsArticlePage = esArticleService.findAllDataEsArticlePage(richParameters.getSearchModel(), wordJa,
                        scrollId, startDateTime, endDateTime, pageSize, richParameters.getCarrier());
                if (allDataEsArticlePage.getScrollId() != null) {
                    concurrentHashMap.put(mapKey, allDataEsArticlePage.getScrollId());
                }

                List<EsArticle> articleList = allDataEsArticlePage.getList();
                int articleSize = articleList.size();
                atomicLong.addAndGet(articleSize);
                if (allDataEsArticlePage.getTotalElements() == 0 || articleSize == 0) {
                    break;
                }
                RateLimiter rateLimiter = RateLimiter.create(100);
                List<Future<Long>> futureList = articleList.stream().map(esArticle -> {
//                    String ossPath = esArticle.getOssPath();
//                    String fileName = ossPath.substring(ossPath.indexOf("_") + 1);
                    String fileName = esArticle.getId();
                    FileInfoDto fileInfoDto = FileInfoDto.builder().filename(fileName).content(getArticleJson(esArticle)).build();
                    Callable<Long> callable = Objects.equal(storageMode, StorageMode.LOCAL) ?
                            new WriteInLocal(startDateTime.toLocalDate(), getArticleJson(esArticle), filePath, fileName, atomicInteger)
                            : new SendInInterface(rateLimiter, analysisService, fileInfoDto);
                    return executorService.submit(callable);
                }).collect(Collectors.toList());

                long runEnd = System.currentTimeMillis();
                long second = ((runEnd - runStart) / 1000);

                for (Future<Long> future : futureList) {
                    long time = TheadUtils.getFutureLong(future);
                    totalRequestTime += time;
                    if (time > maxRequestTime) {
                        maxRequestTime = time;
                    }
                    if (time < minRequestTime) {
                        minRequestTime = time;
                    }
                }
                log.info("startDateTime: {}, " +
                                "startDateTime: {}, " +
                                "SearchModel:{}, " +
                                "total page: {}, " +
                                "current page: {}, " +
                                "average qps: {}, " +
                                "average latency: {} ms, " +
                                "maximum latency: {} ms, " +
                                "minimum latency: {} ms ",
                        DateUtils.formatDateTime(startDateTime),
                        DateUtils.formatDateTime(endDateTime),
                        richParameters.getSearchModel(),
                        allDataEsArticlePage.getTotalPage(),
                        atomicPage.incrementAndGet(),
                        articleSize / (second == 0 ? 1 : second),
                        totalRequestTime / articleSize,
                        maxRequestTime,
                        minRequestTime);
            }
            executorService.shutdown();
            long executionEnd = System.currentTimeMillis();
            return atomicLong.get();
        }
    }

    public static String getArticleJson(EsArticle esArticle) {
        JSONObject params = new JSONObject();
        params.put("SpiderInfo", "军犬舆情平台数据推送");
        params.put("ConfigInfo", "");
        params.put("ColumnURL", "");
        params.put("RegularName", esArticle.getSiteName());
        params.put("ConfigTag", "");
        params.put("KeywordID", "");
        params.put("Keyword", "");
        params.put("KeywordTag", "");
        params.put("Country", esArticle.getRegion());
        params.put("Carrie", esArticle.getCarrier());
        params.put("ColumnName", esArticle.getColumnName());
        params.put("Profession", "1000");
        params.put("Area", "");
        params.put("GatherTime", DateUtils.formatDateTime(esArticle.getGatherTime()));
        params.put("OrgURL", esArticle.getUrl());
        params.put("URL", esArticle.getUrl());
        params.put("PublishTime", DateUtils.formatDateTime(esArticle.getPublishTime()));
        params.put("Author", esArticle.getAuthor());
        params.put("Content", esArticle.getContent());
        params.put("Title", esArticle.getTitle());
        params.put("SiteName", esArticle.getSiteName());
        return params.toJSONString();
    }

}
