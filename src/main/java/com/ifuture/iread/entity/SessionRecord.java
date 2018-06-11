package com.ifuture.iread.entity;

import javax.persistence.*;

/**
 * Created by maofn on 2017/5/10.
 * 记录上一次登录的身份
 */
@Entity
@Table(name = "session_record")
public class SessionRecord {

    @Id
    private String openId;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "member_id")
    private Member member;

    public SessionRecord() {
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public SessionRecord(String openId, User user) {
        this.openId = openId;
        this.user = user;
    }

    public SessionRecord(String openId, Member member) {
        this.openId = openId;
        this.member = member;
    }
}
