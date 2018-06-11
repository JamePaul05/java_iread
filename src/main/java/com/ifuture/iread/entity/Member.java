package com.ifuture.iread.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by maofn on 2017/3/23.
 */
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    /**
     * 姓名
     */
    @NotBlank(message = "此字段不能为空！")
    private String memberName;

    /**
     * 性别 1：男    0：女
     */
    private String sex = "1";

    @Column(length = 11, unique = true)
    @NotBlank(message = "此字段不能为空！")
    private String mobile;

    @Column(length = 18, unique = true)
    @NotBlank(message = "此字段不能为空！")
    private String idNumber;

    @Column(length = 50)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "city_id")
    private City city;

    @Transient
    private String cityID;

    private String address;

    @Column(length = 400)
    private String additional;

    /**
     * 会员等级
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "memberLevel_id")
    private MemberLevel memberLevel;

    @Transient
    private String memberLevelID;

    /**
     * 注册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerTime;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    /**
     * 一对多，一个会员可能借了多本书，被维护端
     */
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "borrower")
    private List<Booklet> booklets;

    private boolean enable = true;

    /**
     * openid
     */
    private String wechatId;

    private String pinyin;

    // 支付订单号,唯一
    private String paymentNo;


    @Column(length = 50)
    private String memberPassword;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechat) {
        this.wechatId = wechat;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getMemberLevelID() {
        return memberLevelID;
    }

    public void setMemberLevelID(String memberLevelID) {
        this.memberLevelID = memberLevelID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<Booklet> getBooklets() {
        return booklets;
    }

    public void setBooklets(List<Booklet> booklets) {
        this.booklets = booklets;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

}
