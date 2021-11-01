package com.zt.zeus.transfer.es.service;

import com.alibaba.fastjson.JSONArray;
import com.zt.zeus.transfer.custom.CustomPage;
import com.zt.zeus.transfer.enums.Carrier;
import com.zt.zeus.transfer.enums.SearchModel;
import com.zt.zeus.transfer.es.domain.EsArticle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong date: 2018/8/20 11:24 description: es文章查询的业务层
 */
public interface EsArticleService {


    List<EsArticle> findIdEsArticlePage(List<String> articleIds, LocalDate localDate);
    /**
     * 根据相关词获取所有数据文章列表
     *
     * @return EsPage
     */
    CustomPage<EsArticle> findAllDataEsArticlePage(SearchModel searchModel, JSONArray wordJa, String scrollId,
                                                   LocalDateTime startDateTime,
                                                   LocalDateTime endDateTime, int pageSize,
                                                   List<Carrier> carrier);
}
