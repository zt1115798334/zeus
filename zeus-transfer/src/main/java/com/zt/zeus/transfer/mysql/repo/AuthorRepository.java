package com.zt.zeus.transfer.mysql.repo;

import com.zt.zeus.transfer.mysql.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long>,
        JpaSpecificationExecutor<Author> {
    Page<Author> findAll(Pageable pageable);

    boolean existsByAuthorName(String authorName);
}
