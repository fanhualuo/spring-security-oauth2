package com.hehe.common.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 数据返回工具类
 * Date: 2017/8/12
 * Time: 20:36
 * email: qinghe101@qq.com
 * Author: hehe
 */
public class Paging<T> implements Serializable {
    private static final long serialVersionUID = 3385795752386139200L;
    private Long total;
    private List<T> data;

    public Paging() {
    }

    public Paging(Long total, List<T> data) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean isEmpty() {
        return Objects.equals(0L, this.total) || this.data == null || this.data.isEmpty();
    }

    public static <T> Paging<T> empty(Class<T> clazz) {
        List<T> emptyList = Collections.emptyList();
        return new Paging(0L, emptyList);
    }

    public static <T> Paging<T> empty() {
        return new Paging(0L, Collections.emptyList());
    }
}
