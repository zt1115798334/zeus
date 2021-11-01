package com.zt.zeus.transfer.controller;

import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.base.controller.BaseResultMessage;
import com.zt.zeus.transfer.base.controller.ResultMessage;
import com.zt.zeus.transfer.custom.RichParameters;
import com.zt.zeus.transfer.enums.StorageMode;
import com.zt.zeus.transfer.handler.SyncPullArticleHandler;
import com.zt.zeus.transfer.service.PullService;
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

    private final PullService pullService;

    @Resource(name = "customRelatedWordsByDateRange")
    private final SyncPullArticleHandler.CustomRelatedWordsByDateRange customRelatedWordsByDateRange;

    @Resource(name = "queryRelatedWordsByDateRange")
    private final SyncPullArticleHandler.QueryRelatedWordsByDateRange queryRelatedWordsByDateRange;

    @Resource(name = "gatherRelatedWordsByDateRange")
    private final SyncPullArticleHandler.GatherRelatedWordsByDateRange gatherRelatedWordsByDateRange;

    @Resource(name = "customAuthorsByDateRange")
    private final SyncPullArticleHandler.CustomAuthorsByDateRange customAuthorsByDateRange;

    @Resource(name = "queryAuthorsByDateRange")
    private final SyncPullArticleHandler.QueryAuthorsByDateRange queryAuthorsByDateRange;

    @Resource(name = "gatherAuthorsByDateRange")
    private final SyncPullArticleHandler.GatherAuthorsByDateRange gatherAuthorsByDateRange;

    @ApiOperation(value = "通过文章id拉取数据")
    @PostMapping(value = "pullArticleOfCustomArticles",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfCustomArticles(@ApiParam(name = "storageMode", value = "存储项", required = true)
                                                     @RequestParam StorageMode storageMode,
                                                     @ApiParam(name = "localDate", value = "时间 格式：yyyy-MM-dd", required = true)
                                                     @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                     @RequestParam LocalDate localDate,
                                                     @ApiParam(name = "articleIds", value = "文章id集合", required = true)
                                                     @RequestParam List<String> articleIds) {
        long result = pullService.pullEsArticleByArticleIds(RichParameters.builder().storageMode(storageMode).build(), articleIds, localDate);
        return success(result);
    }

    @ApiOperation(value = "通过自定义词拉取数据")
    @PostMapping(value = "pullArticleOfCustomRelatedWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfCustomRelatedWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
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
        long result = customRelatedWordsByDateRange.handlerData(extraParams);
        return success(result);
    }

    @ApiOperation(value = "通过搜索词拉取数据")
    @PostMapping(value = "pullArticleOfQueryRelatedWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfQueryRelatedWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                 @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate startDate,
                                                 @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                 @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate endDate,
                                                 @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                 @RequestParam StorageMode storageMode,
                                                 @ApiParam(name = "queryRelatedWords", value = "查询词", required = true)
                                                 @RequestParam List<String> queryRelatedWords) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("queryRelatedWords", queryRelatedWords);
        long result = queryRelatedWordsByDateRange.handlerData(extraParams);
        return success(result);
    }


    @ApiOperation(value = "通过采集词拉取数据")
    @PostMapping(value = "pullArticleOfGatherRelatedWords",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfGatherRelatedWords(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
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
        long result =gatherRelatedWordsByDateRange.handle(extraParams);
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
        long result  = customAuthorsByDateRange.handlerData(extraParams);
        return success(result);
    }

    @ApiOperation(value = "通过搜索作者拉取数据")
    @PostMapping(value = "pullArticleOfQueryAuthors",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage pullArticleOfQueryAuthors(@ApiParam(name = "startDate", value = "开始时间 格式：yyyy-MM-dd", required = true)
                                                   @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                   @RequestParam LocalDate startDate,
                                                   @ApiParam(name = "endDate", value = "结束时间 格式：yyyy-MM-dd", required = true)
                                                   @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                   @RequestParam LocalDate endDate,
                                                   @ApiParam(name = "storageMode", value = "存储项", required = true)
                                                   @RequestParam StorageMode storageMode,
                                                   @ApiParam(name = "queryAuthors", value = "查询词", required = true)
                                                   @RequestParam List<String> queryAuthors) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("queryAuthors", queryAuthors);
        long handlerData = queryAuthorsByDateRange.handlerData(extraParams);
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
