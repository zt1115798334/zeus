package com.zt.zeus.transfer.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.es.service.EsInterfaceService;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.utils.HttpClientUtils;
import com.zt.zeus.transfer.utils.MD5Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by fan on 7/29/20.
 */
@Slf4j
@AllArgsConstructor
@Service
public class EsInterfaceServiceImpl implements EsInterfaceService {

    private final EsProperties esProperties;

    private StringBuffer splicingUrl(EsProperties.EsInfo esInfo) {
        log.info("esInfo:{}", esInfo);
        long time = System.currentTimeMillis() / 1000;// 秒
        String token = MD5Utils.generateToken(esInfo.getKey(), time);
        StringBuffer restUrl = new StringBuffer();
        restUrl.append(esInfo.getHost()).append(esInfo.getFullQuery()).append("?call_id=").append(time)
                .append("&token=").append(token).append("&appid=").append(esInfo.getAppId());
        return restUrl;
    }

    private StringBuffer splicingUrl(int pageSize) {
        EsProperties.EsInfo es5 = esProperties.getEs5();
        return splicingUrl(es5).append("&page_size=").append(pageSize);
    }

    @Override
    public String dataQuery(JSONObject params, int pageSize) {
        String url = splicingUrl(pageSize).toString();
        return HttpClientUtils.getInstance().httpPostJson(url, params.getInnerMap());
    }
}
