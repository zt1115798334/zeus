package com.zt.zeus.transfer.kafka;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.zt.zeus.transfer.analysis.service.AnalysisService;
import com.zt.zeus.transfer.custom.SysConst;
import com.zt.zeus.transfer.dto.FileInfoDto;
import com.zt.zeus.transfer.es.domain.EsArticle;
import com.zt.zeus.transfer.service.callable.SendInInterface;
import com.zt.zeus.transfer.utils.ArticleUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/11/18
 * description:
 */
@AllArgsConstructor
@Slf4j
@Component
public class KafkaConsumerListener {

    private final AnalysisService analysisService;

   final RateLimiter rateLimiter = RateLimiter.create(50, 1, NANOSECONDS);

    @KafkaListener(topics = {"${custom.kafka.topic}"})
    public void onMessage(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        ExecutorService executorService = Executors.newFixedThreadPool(SysConst.AVAILABLE_PROCESSORS >> 1);
        log.info("获取数据条数=" + records.size());
        for (ConsumerRecord<String, String> record : records) {
            EsArticle esArticle = ArticleUtils.jsonObjectConvertEsArticle(JSONObject.parseObject(record.value()));
            log.info(esArticle.getId());
            FileInfoDto fileInfoDto = FileInfoDto.builder().filename(esArticle.getId()).content(ArticleUtils.getArticleJson(esArticle)).build();
            executorService.submit(new SendInInterface(rateLimiter, analysisService, fileInfoDto));
        }
        executorService.shutdown();
        ack.acknowledge();
    }
}
