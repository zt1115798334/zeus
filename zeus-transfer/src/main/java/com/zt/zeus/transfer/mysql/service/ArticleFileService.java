package com.zt.zeus.transfer.mysql.service;

import com.zt.zeus.transfer.mysql.entity.ArticleFile;
import org.springframework.data.domain.Page;

public interface ArticleFileService {


    Page<ArticleFile> findPage(int pageNumber, int pageSize);

    long findCount();

    void truncateTable();
}
