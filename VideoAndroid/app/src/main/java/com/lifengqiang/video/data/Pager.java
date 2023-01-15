package com.lifengqiang.video.data;

/**
 * 分页
 *
 * @author 李凤强
 */
public class Pager {
    /**
     * pageCount : 7
     * currentPage : 1
     * url : /room/list/
     * size : 15
     * tailAppend :
     */

    private int pageCount;
    private int currentPage;
    private String url;
    private int size;
    private String tailAppend;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTailAppend() {
        return tailAppend;
    }

    public void setTailAppend(String tailAppend) {
        this.tailAppend = tailAppend;
    }
}
