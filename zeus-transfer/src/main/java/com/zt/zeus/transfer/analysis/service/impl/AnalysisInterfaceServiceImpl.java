package com.zt.zeus.transfer.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.analysis.service.AnalysisInterfaceService;
import com.zt.zeus.transfer.analysis.url.UrlConstants;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AnalysisInterfaceServiceImpl implements AnalysisInterfaceService {

    private final EsProperties esProperties;

    @Override
    public void analysis(JSONObject params) {
        HttpClientUtils.getInstance().httpPostFrom(esProperties.getAnalysis() + UrlConstants.URL_ANALYSIS, params.getInnerMap());
    }
}
