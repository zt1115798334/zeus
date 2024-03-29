package com.zt.zeus.transfer.es.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.base.Objects;
import com.zt.zeus.transfer.custom.CustomPage;
import com.zt.zeus.transfer.enums.Carrier;
import com.zt.zeus.transfer.enums.SearchModel;
import com.zt.zeus.transfer.enums.SearchRange;
import com.zt.zeus.transfer.enums.SearchType;
import com.zt.zeus.transfer.es.domain.EsArticle;
import com.zt.zeus.transfer.es.service.EsArticleService;
import com.zt.zeus.transfer.es.service.EsInterfaceService;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.properties.QueryProperties;
import com.zt.zeus.transfer.utils.ArticleUtils;
import com.zt.zeus.transfer.utils.DateUtils;
import com.zt.zeus.transfer.utils.EsParamsUtils;
import com.zt.zeus.transfer.utils.MStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 11:31
 * description:
 */
@Slf4j
@AllArgsConstructor
@Service
public class EsArticleServiceImpl implements EsArticleService {

    private final EsInterfaceService esInterfaceService;

    private final EsProperties esProperties;

    private final QueryProperties queryProperties;

    private List<EsArticle> jsonToArticleList(JSONArray jsonArray) {
        return Optional.ofNullable(jsonArray)
                .map(obj -> obj.stream()
                        .map(o -> TypeUtils.castToJavaBean(o, JSONObject.class))
                        .map(ArticleUtils::jsonObjectConvertEsArticle).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public boolean isJsonObject(String content) {
        if (StringUtils.isBlank(content))
            return false;
        try {
            @SuppressWarnings("unused")
            JSONObject json = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private CustomPage<EsArticle> jsonToArticlePage(String str) {
        if (!isJsonObject(str)) {
            log.info("返回数据解析错误:" + str);
            return new CustomPage<>();
        }
        return Optional.ofNullable(JSONObject.parseObject(str))
                .map(jo -> {
                    if (jo.getIntValue("code") != 0) {
                        log.info("接口数据查询错误:" + str);
                        return new CustomPage<EsArticle>();
                    }
                    List<EsArticle> rows = jsonToArticleList(jo.getJSONArray("result"));
                    long totalElements = jo.getLongValue("count");
                    long totalPage = jo.getLongValue("page");
                    String scrollId = jo.getString("scrollId");
                    return new CustomPage<>(rows, totalElements, totalPage, scrollId);
                })
                .orElse(new CustomPage<>());
    }

    @Override
    public List<EsArticle> findIdEsArticlePage(List<String> articleIds, LocalDate localDate) {
        List<JSONObject> collect = articleIds.parallelStream()
                .map(articleId -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", articleId);
                    jsonObject.put("publishTime", DateUtils.formatDate(localDate)+" 00:00:00");
                    return jsonObject;
                }).collect(Collectors.toList());
        JSONObject params = new JSONObject();
        params.put("ids", collect);
        String str = esInterfaceService.dataQueryArticleId(params);
        return jsonToArticlePage(str).getList();
    }

    @Override
    public CustomPage<EsArticle> findAllDataEsArticlePage(SearchModel searchModel, JSONArray wordJa,
                                                          String scrollId,
                                                          LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                          int pageSize, List<Carrier> carrier) {
        if (wordJa.isEmpty()) {
            return new CustomPage<>();
        }
        JSONObject params = new JSONObject();
        if (Objects.equal(searchModel, SearchModel.RELATED_WORDS)) {
            if (Objects.equal(esProperties.getSearchType(), SearchType.EXACT)) {
                params.putAll(EsParamsUtils.getQueryRelatedWordsParams(wordJa));
                List<String> exclustionWrods = queryProperties.getRelatedQuery().getExclustion().stream().map(MStringUtils::splitMinGranularityStr)
                        .flatMap(Collection::stream)
                        .distinct().collect(Collectors.toList());
                if (!exclustionWrods.isEmpty()) {
                    JSONArray exclustionWrodJa = JSONArray.parseArray(JSONArray.toJSONString(exclustionWrods));
                    params.putAll(EsParamsUtils.getQueryExclustionWordsParams(exclustionWrodJa));
                }
            } else {
                params.putAll(EsParamsUtils.getQuerySearchValueParams(wordJa));
            }
        } else if (Objects.equal(searchModel, SearchModel.AUTHOR)) {
            params.putAll(EsParamsUtils.getQueryAuthor(wordJa));
        }else if (Objects.equal(searchModel, SearchModel.SITE_NAME)) {
            params.putAll(EsParamsUtils.getQuerySiteName(wordJa));
        }else if (Objects.equal(searchModel, SearchModel.URL_MAIN)) {
            params.putAll(EsParamsUtils.getQueryUrlMain(wordJa));
        }
        params.putAll(EsParamsUtils.getQueryScrollIdParams(scrollId));
        if (Objects.equal(esProperties.getSearchRange(), SearchRange.CREATE_TIME)) {
            params.putAll(EsParamsUtils.getQueryCreatedTimeParams(startDateTime, endDateTime));
        }else if (Objects.equal(esProperties.getSearchRange(), SearchRange.GATHER_TIME)) {
            params.putAll(EsParamsUtils.getQueryGatherTimeParams(startDateTime, endDateTime));
        }else{
            params.putAll(EsParamsUtils.getQueryTimeParams(startDateTime, endDateTime));
        }
        params.put("searchType", "all");
        if (carrier != null && !carrier.isEmpty()) {
            List<Integer> carrierCode = carrier.stream().map(Carrier::getCode).collect(Collectors.toList());
            params.putAll(EsParamsUtils.getQueryCarrieParams(carrierCode));
        }
        String str = esInterfaceService.dataQuery(params, pageSize);
        return jsonToArticlePage(str);
    }
}
