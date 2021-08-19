package com.zt.zeus.transfer.service.callable;

import com.zt.zeus.transfer.analysis.service.AnalysisService;
import com.zt.zeus.transfer.dto.FileInfoDto;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SendDbInInterface implements Runnable{
    private final RateLimiter rateLimiter;

    private final AnalysisService analysisService;

    private final FileInfoDto fileInfoDto;

    @Override
    public void run() {
        rateLimiter.acquire();
        analysisService.analysis(fileInfoDto.getContent(), fileInfoDto.getFilename());
    }
}
