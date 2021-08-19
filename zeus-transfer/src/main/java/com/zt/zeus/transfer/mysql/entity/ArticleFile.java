package com.zt.zeus.transfer.mysql.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author fan
 *
 */
@Data
@Entity
@Table(name = "rencai_zhengce")
public class ArticleFile{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 词类别
     */
    @Column(name = "JsonData")
    private String fileContext;

}
