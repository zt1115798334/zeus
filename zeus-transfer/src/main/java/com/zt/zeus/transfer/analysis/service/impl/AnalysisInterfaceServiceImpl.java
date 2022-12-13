package com.zt.zeus.transfer.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.analysis.service.AnalysisInterfaceService;
import com.zt.zeus.transfer.analysis.url.UrlConstants;
import com.zt.zeus.transfer.properties.EsProperties;
import com.zt.zeus.transfer.utils.HttpUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@AllArgsConstructor
@Component
public class AnalysisInterfaceServiceImpl implements AnalysisInterfaceService {

    private final EsProperties esProperties;
    private final Random random = new Random();

    @Override
    public void analysis(JSONObject params) {
        HttpUtils.getInstance().doPostForm(
                esProperties.getAnalysis().get(random.nextInt(esProperties.getAnalysis().size())) + UrlConstants.URL_ANALYSIS, params.getInnerMap());
    }

}
