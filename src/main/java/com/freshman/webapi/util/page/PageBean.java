package com.freshman.webapi.util.page;

import java.util.List;

public class PageBean {
    private boolean isFirstPage;// 是否是第一页
    private boolean isLastPage;// 是否是最后一页
    private boolean hasNextPage;// 是否还有下一页
    private boolean hasPreviousPage;// 是否还有上一页
    private int total;// 获得总页数
    private List<Object> rows;// 当前页的数据
    private int records;// 记录总数
    private int nextPageNumber;// 下一页的页码
    private int previousPageNumber;// 上一页的页面
    private int pageSize;// 每一页显示的记录条数
    private int page;// 当前页码

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public int getTotal() {
        if (total <= 0) {
            total = 1;
        }
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getNextPageNumber() {
        return nextPageNumber;
    }

    public void setNextPageNumber(int nextPageNumber) {
        this.nextPageNumber = nextPageNumber;
    }

    public int getPreviousPageNumber() {
        return previousPageNumber;
    }

    public void setPreviousPageNumber(int previousPageNumber) {
        this.previousPageNumber = previousPageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        if (page <= 0) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PageBean [isFirstPage=" + isFirstPage + ", isLastPage="
                + isLastPage + ", hasNextPage=" + hasNextPage
                + ", hasPreviousPage=" + hasPreviousPage + ", total=" + total
                + ", rows=" + rows + ", records=" + records
                + ", nextPageNumber=" + nextPageNumber
                + ", previousPageNumber=" + previousPageNumber + ", pageSize="
                + pageSize + ", page=" + page + "]";
    }
}
