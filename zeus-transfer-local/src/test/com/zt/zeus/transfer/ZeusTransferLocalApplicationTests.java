package com.zt.zeus.transfer;

import com.alibaba.fastjson.JSONArray;
import com.zt.zeus.transfer.custom.CustomPage;
import com.zt.zeus.transfer.enums.Carrier;
import com.zt.zeus.transfer.enums.SearchModel;
import com.zt.zeus.transfer.es.domain.EsArticle;
import com.zt.zeus.transfer.es.service.EsArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/10 10:39
 * description:
 */
@SpringBootTest
public class ZeusTransferLocalApplicationTests {

    @Autowired
    private EsArticleService esArticleService;
    @Test
    void queryArticle() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("环球网");
        LocalDateTime start = LocalDateTime.of(2021, 9, 10, 7, 10);
        LocalDateTime end = LocalDateTime.of(2021, 9, 10, 7, 20);
        CustomPage<EsArticle> allDataEsArticlePage = esArticleService.findAllDataEsArticlePage(SearchModel.AUTHOR, jsonArray, "", start, end, 10, Arrays.asList(Carrier.WE_CHAT));
        allDataEsArticlePage.getList().stream().map(EsArticle::getId).forEach(System.out::println);

    }
}
