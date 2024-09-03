package com.zt.zeus.transfer.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.es.service.EsInterfaceService;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.utils.HttpUtils;
import com.zt.zeus.transfer.utils.MD5Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

/**
 * Created by fan on 7/29/20.
 */
@Slf4j
@AllArgsConstructor
@Service
public class EsInterfaceServiceImpl implements EsInterfaceService {

    private final EsProperties esProperties;

    private StringBuffer splicingUrl(String key,String host,String url,String appId) {
        long time = System.currentTimeMillis() / 1000;// 秒
        String token = MD5Utils.generateToken(key, time);
        StringBuffer restUrl = new StringBuffer();
        restUrl.append(host).append(url).append("?call_id=").append(time)
                .append("&token=").append(token).append("&appid=").append(appId);
        return restUrl;
    }

    private StringBuffer splicingUrlFull(int pageSize) {
        EsProperties.EsInfo esInfo = esProperties.getEs5();
        return splicingUrl(esInfo.getKey(),esInfo.getHost(),esInfo.getFullQuery(),esInfo.getAppId()).append("&page_size=").append(pageSize);
    }
    private StringBuffer splicingUrlArticle() {
        EsProperties.EsInfo esInfo = esProperties.getEs5();
        return splicingUrl(esInfo.getKey(),esInfo.getHost(),esInfo.getArticleQuery(),esInfo.getAppId());
    }

    @Override
    public String dataQueryArticleId(JSONObject params) {
        String url = splicingUrlArticle().toString();
        return HttpUtils.getInstance().doPostJSON(url, params.getInnerMap()).orElse("");
    }

    @Override
    public String dataQuery(JSONObject params, int pageSize) {
        log.info(params.toJSONString());
        String url = splicingUrlFull(pageSize).toString();
        return HttpUtils.getInstance().doPostJSON(url, params.getInnerMap()).orElse("");
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis() / 1000;// 秒
        String token = MD5Utils.generateToken("yq_ztjy", time);
        System.out.println(time);
        System.out.println(token);
    }
}
