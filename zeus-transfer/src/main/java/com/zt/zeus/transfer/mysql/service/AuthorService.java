package com.zt.zeus.transfer.mysql.service;

import com.zt.zeus.transfer.base.service.BaseService;
import com.zt.zeus.transfer.mysql.entity.Author;

public interface AuthorService  extends BaseService<Author, Long> {
    boolean isExistsAuthor(String author);
}
