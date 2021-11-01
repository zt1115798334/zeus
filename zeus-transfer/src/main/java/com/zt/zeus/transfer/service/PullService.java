package com.zt.zeus.transfer.service;

import com.zt.zeus.transfer.custom.RichParameters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PullService {

    long pullEsArticleByArticleIds(RichParameters richParameters, List<String> articleIds, LocalDate localDate);

    long pullEsArticleByDateRange(RichParameters richParameters, List<String> words, LocalDate startDate, LocalDate endDate);

    long pullEsArticleByTimeRange(RichParameters richParameters, List<String> words, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
