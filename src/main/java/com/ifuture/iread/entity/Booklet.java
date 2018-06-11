package com.ifuture.iread.entity;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by maofn on 2017/3/25.
 * 书册类
 */
@Entity
@Table(name = "booklet")
public class Booklet extends BaseEntity {

    /**
     * 形如isbn_01
     */
    @Column(unique = true)
    private String bianHao;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    //CascadeType.DETACH  级联脱管/游离操作 撤销所有相关的外键关联
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    /**
     * 状态。分为馆藏、录入系统、失踪、借出。
     */
    private String state;

    /**
     * 是否被借出
     */
    private boolean borrowed;

    /**
     * 借书人，关系维护端
     */
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "borrower_id")
    private Member borrower;

    /**
     * 借出日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowDate;

    /**
     * 还书的工作人员，关系维护端
     */
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "returner_id")
    private User returner;

    /**
     * 还书日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    /**
     * 盘点日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date inventoryDate;


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public Member getBorrower() {
        return borrower;
    }

    public void setBorrower(Member borrower) {
        this.borrower = borrower;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getBianHao() {
        return bianHao;
    }

    public void setBianHao(String bianHao) {
        this.bianHao = bianHao;
    }

    public User getReturner() {
        return returner;
    }

    public void setReturner(User returner) {
        this.returner = returner;
    }
}
