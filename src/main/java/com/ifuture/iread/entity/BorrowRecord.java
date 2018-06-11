package com.ifuture.iread.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by maofn on 2017/4/2.
 * 借阅记录表
 */
@Entity
@Table(name = "borrow_record")
public class BorrowRecord {

    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String borrowerId;  //借书人id

    @Column(length = 36)
    private String returnId;  //还书人id

    @Column(length = 36)
    private String bookletId;   //书册id

    @Column(length = 36)
    private String borrowShopId;    //借阅地点id

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowTime;    //借阅时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnTime;    //归还时间

    @Column(length = 36)
    private String returnShopId;    //归还地点id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getBookletId() {
        return bookletId;
    }

    public void setBookletId(String bookletId) {
        this.bookletId = bookletId;
    }

    public String getBorrowShopId() {
        return borrowShopId;
    }

    public void setBorrowShopId(String borrowShopId) {
        this.borrowShopId = borrowShopId;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnShopId() {
        return returnShopId;
    }

    public void setReturnShopId(String returnShopId) {
        this.returnShopId = returnShopId;
    }
}
