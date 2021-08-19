package com.zt.zeus.transfer.mysql.entity;

import com.zt.zeus.transfer.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_author")
public class Author  extends IdEntity {
    private String authorName;
}
