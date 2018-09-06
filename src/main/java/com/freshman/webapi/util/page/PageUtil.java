package com.freshman.webapi.util.page;

import java.util.ArrayList;
import java.util.List;

public class PageUtil implements Page {

    public static final int PAGE_SIZE = 10; // 每页多少行
    private List<?> elements;
    private int pageSize;
    private int pageNumber;
    private int totalElements = 0;

    /**
     * 初始化pageUtil
     */
    public PageUtil() {
    }

    /*
     * 通过传入List列表进行分页构造函数 修改by
     */
    public PageUtil(List<?> list, int pageNumber, int pageSize, int total) {
        this.pageNumber = pageNumber > 0 ? pageNumber : 1;
        try {
            this.pageSize = pageSize;
            // /pageSize;
        } catch (Exception e) {
            this.pageSize = PAGE_SIZE;
        }
        try {
            this.totalElements = total; // 总记录数
            if (Integer.MAX_VALUE == this.pageNumber
                    || this.pageNumber > getLastPageNumber()) // last page
            {
                this.pageNumber = getLastPageNumber();
            }
            if (this.totalElements > 0) {
                elements = list;
            } else
                elements = new ArrayList<Object>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造分页
     *
     * @param puc
     * @return
     */
    @SuppressWarnings("unchecked")
    public static PageBean getPageBean(PageUtil puc) {
        PageBean pb = new PageBean();
        pb.setFirstPage(puc.isFirstPage());
        pb.setHasNextPage(puc.hasNextPage());
        pb.setHasPreviousPage(puc.hasPreviousPage());
        pb.setLastPage(puc.isLastPage());
        pb.setTotal(puc.getLastPageNumber());
        pb.setNextPageNumber(puc.getNextPageNumber());
        pb.setPageSize(puc.getPageSize());
        pb.setPreviousPageNumber(puc.getPreviousPageNumber());
        pb.setPage(puc.getThisPageNumber());
        pb.setRecords(puc.getTotalNumberOfElements());
        pb.setRows((List<Object>) puc.getThisPageElements());
        return pb;
    }

    /**
     * 是否是第一页 return boolean
     */
    public boolean isFirstPage() {
        return getThisPageNumber() == 1;
    }

    /**
     * 是否是最后一页 return boolean
     */
    public boolean isLastPage() {
        return getThisPageNumber() >= getLastPageNumber();
    }

    /**
     * 是否有下一页 return boolean
     */
    public boolean hasNextPage() {
        return getLastPageNumber() > getThisPageNumber();
    }

    /**
     * 是否有上一页 return boolean
     */
    public boolean hasPreviousPage() {
        return getThisPageNumber() > 1;
    }

    /**
     * 返回最后一页的页码 return int
     */
    public int getLastPageNumber() {
        return totalElements % this.pageSize == 0 ? totalElements
                / this.pageSize : (totalElements / this.pageSize) + 1;
    }

    /**
     * 返回本页数据
     *
     * @return Object<List>
     */
    public Object getThisPageElements() {
        return elements;
    }

    /**
     * 返回总记录数
     *
     * @return int
     */
    public int getTotalNumberOfElements() {
        return totalElements;
    }

    /**
     * 返回当前页的第一条数据的记录数 return int
     */
    public int getThisPageFirstElementNumber() {
        return (getThisPageNumber() - 1) * getPageSize();
    }

    /**
     * 返回当前页的最后一条数据的记录数 return int
     */
    public int getThisPageLastElementNumber() {
        int fullPage = getThisPageFirstElementNumber() + getPageSize();
        return getTotalNumberOfElements() < fullPage ? getTotalNumberOfElements()
                : fullPage;
    }

    /**
     * 返回下一页的记录数 return int
     */
    public int getNextPageNumber() {
        return getThisPageNumber();
    }

    /**
     * 返回上一页的记录数 return int
     */
    public int getPreviousPageNumber() {
        return getThisPageNumber();
    }

    /**
     * 返回页面显示的记录条数 return int
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 返回当前页的记录数 return int
     */
    public int getThisPageNumber() {
        return pageNumber;
    }
}
