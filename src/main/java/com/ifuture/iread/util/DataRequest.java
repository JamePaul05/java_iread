package com.ifuture.iread.util;

import java.util.List;

/**
 * 用于封装datatables传来的参数
 */
public class DataRequest {
    //datatables发送多少就返回多少（转成整型）
    private String draw;
    //第几页
    private int page;
    //每页显示的行数
    private int rows;
    //排序列
    private String orderField;
    //搜索词
    private String search;
    //搜索词是否是正则表达式
    private boolean regex;
    //排序方向
    private String direction;
    //多列搜索
    private List<String> multiSearchList;

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getMultiSearchList() {
        return multiSearchList;
    }

    public void setMultiSearchList(List<String> multiSearchList) {
        this.multiSearchList = multiSearchList;
    }
}
