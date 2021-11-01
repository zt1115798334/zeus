package com.zt.zeus.transfer.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.analysis.service.AnalysisInterfaceService;
import com.zt.zeus.transfer.analysis.url.UrlConstants;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.utils.HttpUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AnalysisInterfaceServiceImpl implements AnalysisInterfaceService {

    private final EsProperties esProperties;

    @Override
    public void analysis(JSONObject params) {
        HttpUtils.getInstance().doPostForm(esProperties.getAnalysis() + UrlConstants.URL_ANALYSIS, params.getInnerMap());
    }
}
