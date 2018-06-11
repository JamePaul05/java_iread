package com.ifuture.iread.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2017/2/20.
 */
@Entity
@Table(name="check_code")
public class CheckCode {

    @Id
    @Column(length=36)
    private String id;

    @Column(length=50)
    private String mobile;//手机号

    @Column(length=50)
    private String code;//验证码

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;//发送日期

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;//失效日期

    @Temporal(TemporalType.TIMESTAMP)
    private Date verifyDate;//验证日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }
}
