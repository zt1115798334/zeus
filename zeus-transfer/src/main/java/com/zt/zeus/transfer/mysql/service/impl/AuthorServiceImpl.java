package com.zt.zeus.transfer.mysql.service.impl;

import com.zt.zeus.transfer.base.service.PageUtils;
import com.zt.zeus.transfer.mysql.entity.Author;
import com.zt.zeus.transfer.mysql.repo.AuthorRepository;
import com.zt.zeus.transfer.mysql.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Page<Author> findPageByEntity(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageUtils.buildPageRequest(pageNumber, pageSize);
        return authorRepository.findAll(pageRequest);
    }

    @Override
    public boolean isExistsAuthor(String author) {
        return authorRepository.existsByAuthorName(author);
    }
}
