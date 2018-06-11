package com.ifuture.iread.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by maofn on 2017/3/25.
 * 书本类
 */
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    private String isbn;    //isbn

    private String bookName;//书名

    private String author;  //作者

    private String press;   //出版社

    /**
     * 不用date类型是因为豆瓣传来的值不一定就是date类型
     */
    @Column(length = 50)
    private String publicationDate;//出版年

    private int pages;      //页数

    private BigDecimal price;//价格

    private int borrowTimes;    //借阅次数

    @Column(length = 10000)
    private String summary; //简介

    private String grade;   //评分

    @Column(name = "simg_url")
    private String simgUrl;

    private String pinyin;

    private int allowBookletBorrowNumber;

    private int bookletsTotal;  //书册总数


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getBorrowTimes() {
        return borrowTimes;
    }

    public void setBorrowTimes(int borrowTimes) {
        this.borrowTimes = borrowTimes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSimgUrl() {
        return simgUrl;
    }

    public void setSimgUrl(String simgUrl) {
        this.simgUrl = simgUrl;
    }

    public int getAllowBookletBorrowNumber() {
        return allowBookletBorrowNumber;
    }

    public void setAllowBookletBorrowNumber(int allowBookletBorrowNumber) {
        this.allowBookletBorrowNumber = allowBookletBorrowNumber;
    }

    public int getBookletsTotal() {
        return bookletsTotal;
    }

    public void setBookletsTotal(int bookletsTotal) {
        this.bookletsTotal = bookletsTotal;
    }
}
