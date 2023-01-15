package com.lifengqiang.video.model.data;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class Pager {
    /**
     * 分页总页数
     */
    private long pageCount;
    /**
     * 当前页
     */
    private Long currentPage;
    /**
     * 分页跳转目标链接
     */
    private String url;
    /**
     * 当前页的列数
     */
    private Integer size;
    /**
     * 链接尾部附加的参数
     */
    private String tailAppend = "";

    public Pager(long pageCount, Long currentPage, String url) {
        this.pageCount = pageCount;
        this.currentPage = currentPage;
        this.url = url;
    }

    public Pager(long pageCount, Long currentPage, String url, Integer size) {
        this.pageCount = pageCount;
        this.currentPage = currentPage;
        this.url = url;
        this.size = size;
    }

    public Pager(Page<?> page, String url) {
        this(page.getPages(), page.getCurrent(), url, page.getRecords().size());
    }

    public long beginIndex() {
        return Math.max(1, currentPage - 2);
    }

    public long endIndex() {
        return Math.min(pageCount, currentPage + 2);
    }
}
