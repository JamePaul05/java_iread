package com.ifuture.iread.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by maofn on 2017/4/2.
 * 书本迹点表
 */
@Entity
@Table(name = "booklet_trace")
public class BookletTrace {
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String outId; //出点id

    @Column(length = 36)
    private String entryId;  //入点id

    @Column(length = 36)
    private String bookletId;   //书册id

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date allocateDate;  //调拨日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String out) {
        this.outId = out;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String in) {
        this.entryId = in;
    }

    public String getBookletId() {
        return bookletId;
    }

    public void setBookletId(String bookletId) {
        this.bookletId = bookletId;
    }

    public Date getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(Date allocateDate) {
        this.allocateDate = allocateDate;
    }
}
