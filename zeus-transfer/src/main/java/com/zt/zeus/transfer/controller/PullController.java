package com.zt.zeus.transfer.controller;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.base.controller.BaseResultMessage;
import com.zt.zeus.transfer.base.controller.ResultMessage;
import com.zt.zeus.transfer.enums.StorageMode;
import com.zt.zeus.transfer.handler.SyncPullArticleHandler;
import com.zt.zeus.transfer.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 9:48
 * description: 应用中心
 */

@Api(tags = "拉取数据")
@AllArgsConstructor
@RestController
@RequestMapping("api/pull")
public class PullController extends BaseResultMessage {

    @Resource(name = "customWordsByDateRange")
    private final SyncPullArticleHandler.CustomWordsByDateRange customWordsByDateRange;

    @Resource(name = "queryWordsByDateRange")
    private final SyncPullArticleHandler.QueryWordsByDateRange queryWordsByDateRange;

    @Resource(name = "gatherWordsByDateRange")
    private final SyncPullArticleHandler.GatherWordsByDateRange gatherWordsByDateRange;

    @Resource(name = "customAuthorsByDateRange")
    private final SyncPullArticleHandler.CustomAuthorsByDateRange customAuthorsByDateRange;

    @Resource(name = "gatherAuthorsByDateRange")
    private final SyncPullArticleHandler.GatherAuthorsByDateRange gatherAuthorsByDateRange;

    @ApiOperation(value = "通过自定义词拉取数据")

    @PostMapping(value = "pullArticleOfCustomWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfCustomWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                  @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        long handlerData = customWordsByDateRange.handlerData(extraParams);
        return success(handlerData);
    }

    @ApiOperation(value = "通过搜索词拉取数据")
    @PostMapping(value = "pullArticleOfQueryWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfQueryWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                 @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate startDate,
                                                 @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                 @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate endDate,
                                                 @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                 @RequestParam StorageMode storageMode,
                                                 @ApiParam(name = "queryWords", value = "查询词", required = true)
                                                 @RequestParam List<String> queryWords) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("queryWords", queryWords);
        long handlerData = queryWordsByDateRange.handlerData(extraParams);
        return success(handlerData);
    }


    @ApiOperation(value = "通过采集词拉取数据")
    @PostMapping(value = "pullArticleOfGatherWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfGatherWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                  @RequestParam StorageMode storageMode,
                                                  @ApiParam(name = "status", value = "status", required = true)
                                                  @RequestParam(defaultValue = "true") Boolean status) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("status", status);
        gatherWordsByDateRange.handle(extraParams);
        return success();
    }

    @ApiOperation("通过自定义作者拉取数据")
    @PostMapping(value = "pullArticleOfCustomAuthors",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfCustomAuthors(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                    @RequestParam LocalDate startDate,
                                                    @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                    @RequestParam LocalDate endDate,
                                                    @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                    @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        long handlerData = customAuthorsByDateRange.handlerData(extraParams);
        return success(handlerData);
    }

    @ApiOperation("通过采集作者拉取数据")
    @PostMapping(value = "pullArticleOfGatherAuthors",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfGatherAuthors(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                    @RequestParam LocalDate startDate,
                                                    @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                    @RequestParam LocalDate endDate,
                                                    @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                    @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        gatherAuthorsByDateRange.handle(extraParams);
        return success();
    }


}
