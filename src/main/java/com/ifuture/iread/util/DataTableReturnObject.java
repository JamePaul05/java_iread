package com.ifuture.iread.util;

import java.util.List;

/**
 * 用于封装返回给前台数据的对象
 */
public class DataTableReturnObject {
	/**
	 * 数据库总共记录数
	 */
	private long recordsTotal;
	/**
	 * 过滤后的记录数
	 */
	private long recordsFiltered;
	/**
	 * datatables发送多少就返回多少（转成整型）
	 */
    private Integer draw;
	/**
	 * 数据集合
	 */
	private List data;

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
