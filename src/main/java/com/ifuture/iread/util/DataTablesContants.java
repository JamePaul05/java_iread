package com.ifuture.iread.util;

/**
 * Created by maofn on 2017/3/18.
 * datatables参数常量
 */
public class DataTablesContants {

    /**
     * Datatables发送的draw是多少那么服务器就返回多少
     * 用来确保Ajax从服务器返回的是对应的
     */
    public static final String DRAW = "draw";

    /**
     * 数据坐标
     */
    public static final String START = "start";

    /**
     * 每页数据长度
     */
    public static final String LENGTH = "length";

    /**
     * 需要排序的列的坐标
     */
    public static final String ORDER_INDEX = "order[0][column]";

    /**
     *  排列名的参数前缀
     */
    public static final String ORDER_FIELD_PREFIX = "columns[";

    /**
     *  列名的参数后缀
     */
    public static final String ORDER_FIELD_SUFFIX = "][data]";

    /**
     *  列名是否参与搜索参数后缀
     */
    public static final String COLUMN_SEARCHABLE = "][searchable]";

    /**
     *  列名参与搜索
     */
    public static final String COLUMN_SEARCH_VALUE = "][search][value]";
    /**
     *  搜索关键词
     */
    public static final String SEARCH = "search[value]";

    /**
     * 排序方向
     */
    public static final String DIRECTION = "order[0][dir]";

    public static final String DIRECTION_ASC = "asc";

    public static final String DIRECTION_DESC = "desc";

    /**
     * 数据库总记录数
     */
    public static final String RECORDS_TOTAL = "recordsTotal";

    /**
     * 过滤后的记录数
     */
    public static final String RECORDS_FILTERED = "recordsFiltered";

    /**
     * 返回的数据集合
     */
    public static final String DATA = "data";


}
