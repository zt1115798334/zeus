package com.zt.zeus.transfer.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zt.zeus.transfer.base.handler.page.PageHandler;
import com.zt.zeus.transfer.custom.RichParameters;
import com.zt.zeus.transfer.dto.GatherRelatedWordDto;
import com.zt.zeus.transfer.enums.SearchModel;
import com.zt.zeus.transfer.enums.StorageMode;
import com.zt.zeus.transfer.mysql.entity.Author;
import com.zt.zeus.transfer.mysql.service.AuthorService;
import com.zt.zeus.transfer.mysql.service.GatherRelatedWordsService;
import com.zt.zeus.transfer.properties.QueryProperties;
import com.zt.zeus.transfer.service.PullService;
import com.zt.zeus.transfer.utils.MStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan on 7/29/20.
 */
@Component
@AllArgsConstructor
public class SyncPullArticleHandler {


    @Component("queryRelatedWordsByDateRange")
    @AllArgsConstructor
    public static class QueryRelatedWordsByDateRange {

        private final PullService pullEsArticle;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            JSONArray queryRelatedWords = extraParams.getJSONArray("queryRelatedWords");
            List<String> gatherRelatedWords = queryRelatedWords.stream().map(String::valueOf)
                    .map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("custom")
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherRelatedWords, startDate, endDate);
        }
    }

    @Component("queryAuthorsByDateRange")
    @AllArgsConstructor
    public static class QueryAuthorsByDateRange {

        private final PullService pullEsArticle;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            JSONArray queryAuthors = extraParams.getJSONArray("queryAuthors");
            List<String> authors = queryAuthors.stream().map(String::valueOf)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType("custom")
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, authors, startDate, endDate);
        }
    }

    @Component("customAuthorsByDateRange")
    @AllArgsConstructor
    public static class CustomAuthorsByDateRange {

        private final PullService pullEsArticle;
        private final QueryProperties queryProperties;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> authors = queryProperties.getAuthorQuery().getAuthor().stream()
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType("custom")
                    .carrier(queryProperties.getAuthorQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, authors, startDate, endDate);
        }
    }

    @Component("customRelatedWordsByDateRange")
    @AllArgsConstructor
    public static class CustomRelatedWordsByDateRange {

        private final PullService pullEsArticle;
        private final QueryProperties queryProperties;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> gatherRelatedWords = queryProperties.getRelatedQuery().getRelated().stream().map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("custom")
                    .carrier(queryProperties.getRelatedQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherRelatedWords, startDate, endDate);
        }
    }

    @Component("gatherAuthorsByDateRange")
    @AllArgsConstructor
    public static class GatherAuthorsByDateRange extends PageHandler<Author> {

        private final PullService pullEsArticle;

        private final AuthorService authorService;

        private final QueryProperties queryProperties;

        @Override
        protected long handleDataOfPerPage(List<Author> list, int pageNumber, JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> authors = list.parallelStream().map(Author::getAuthorName)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType("custom")
                    .carrier(queryProperties.getAuthorQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, authors, startDate, endDate);
        }

        @Override
        protected Page<Author> getPageList(int pageNumber, JSONObject extraParams) {
            return authorService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
        }
    }

    @Component("gatherRelatedWordsByDateRange")
    @AllArgsConstructor
    public static class GatherRelatedWordsByDateRange extends PageHandler<GatherRelatedWordDto> {

        private final PullService pullEsArticle;

        private final GatherRelatedWordsService gatherRelatedWordsService;

        private final QueryProperties queryProperties;


        @Override
        protected long handleDataOfPerPage(List<GatherRelatedWordDto> list, int pageNumber, JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> gatherRelatedWords = list.stream().map(GatherRelatedWordDto::getName).collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("gather")
                    .carrier(queryProperties.getRelatedQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherRelatedWords, startDate, endDate);
        }

        @Override
        protected Page<GatherRelatedWordDto> getPageList(int pageNumber, JSONObject extraParams) {
            if (extraParams.getBoolean("status")) {
                return gatherRelatedWordsService.findPageByEntityStatus(1L, pageNumber, DEFAULT_BATCH_SIZE);
            } else {
                return gatherRelatedWordsService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
            }
        }
    }

    @Component("customAuthorsByTimeRange")
    @AllArgsConstructor
    public static class CustomAuthorsByTimeRange {

        private final PullService pullEsArticle;

        private final QueryProperties queryProperties;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> authors = queryProperties.getAuthorQuery().getAuthor().stream()
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType(fromType)
                    .carrier(queryProperties.getAuthorQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, authors, startDateTime, endDateTime);
        }
    }

    @Component("customRelatedWordsByTimeRange")
    @AllArgsConstructor
    public static class CustomRelatedWordsByTimeRange {

        private final PullService pullEsArticle;

        private final QueryProperties queryProperties;

        public long handlerData(JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> gatherRelatedWords = queryProperties.getRelatedQuery().getRelated().stream().map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .carrier(queryProperties.getRelatedQuery().getCarrier())
                    .fromType(fromType).build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, gatherRelatedWords, startDateTime, endDateTime);
        }
    }

    @Component("gatherAuthorsByTimeRange")
    @AllArgsConstructor
    public static class GatherAuthorsByTimeRange extends PageHandler<Author> {

        private final PullService pullEsArticle;

        private final AuthorService authorService;

        private final QueryProperties queryProperties;

        @Override
        protected long handleDataOfPerPage(List<Author> list, int pageNumber, JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> authors = list.parallelStream().map(Author::getAuthorName)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder().storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType(fromType)
                    .carrier(queryProperties.getAuthorQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, authors, startDateTime, endDateTime);
        }

        @Override
        protected Page<Author> getPageList(int pageNumber, JSONObject extraParams) {
            return authorService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
        }
    }

    @Component("gatherRelatedWordsByTimeRange")
    @AllArgsConstructor
    public static class GatherRelatedWordsByTimeRange extends PageHandler<GatherRelatedWordDto> {

        private final PullService pullEsArticle;

        private final GatherRelatedWordsService gatherRelatedWordsService;

        private final QueryProperties queryProperties;

        @Override
        protected long handleDataOfPerPage(List<GatherRelatedWordDto> list, int pageNumber, JSONObject extraParams) {
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> gatherRelatedWords = list.stream().map(GatherRelatedWordDto::getName).collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType(fromType)
                    .carrier(queryProperties.getRelatedQuery().getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, gatherRelatedWords, startDateTime, endDateTime);
        }

        @Override
        protected Page<GatherRelatedWordDto> getPageList(int pageNumber, JSONObject extraParams) {
            if (extraParams.getBoolean("status")) {
                return gatherRelatedWordsService.findPageByEntityStatus(1L, pageNumber, DEFAULT_BATCH_SIZE);
            } else {
                return gatherRelatedWordsService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
            }
        }
    }
}
