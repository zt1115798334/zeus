package com.zt.zeus.transfer.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.analysis.service.AnalysisInterfaceService;
import com.zt.zeus.transfer.analysis.service.AnalysisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@AllArgsConstructor
@Component
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisInterfaceService analysisInterfaceService;

    @Override
    public void analysis(String data, String fileName) {
        log.info("fileName:{}", fileName);
        JSONObject params = new JSONObject();
        params.put("data", data);
        params.put("fileName", fileName);
        analysisInterfaceService.analysis(params);
    }
}
