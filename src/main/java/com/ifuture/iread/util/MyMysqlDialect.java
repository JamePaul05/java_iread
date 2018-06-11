package com.ifuture.iread.util;

import org.hibernate.dialect.MySQLInnoDBDialect;

/**
 * Created by maofn on 2017/3/16.
 * 重写方言设置字符集为utf8
 */
public class MyMysqlDialect extends MySQLInnoDBDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

}
