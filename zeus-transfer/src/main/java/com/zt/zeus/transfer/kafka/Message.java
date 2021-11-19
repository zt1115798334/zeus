package com.zt.zeus.transfer.kafka;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/11/18
 * description:
 */
@Data
public class Message<T> implements Serializable {
    private String id;
    private T content;
}
