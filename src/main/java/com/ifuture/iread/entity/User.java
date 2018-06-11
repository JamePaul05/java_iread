package com.ifuture.iread.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maofn on 2017/2/23.
 * 用户类
 */
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @NotBlank(message = "此字段不能为空")
    @Column(length = 50, unique = true)
    private String userName;

    @NotBlank(message = "此字段不能为空")
    @Column(length = 50)
    private String passWord;

    @NotBlank(message = "此字段不能为空")
    private String nickName;

    private String pinyin;

    @Type(type = "yes_no")
    private boolean enable = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Transient
    private String shopID;

    //Transient 不会再数据库插入字段
    //用于获取jsp页面上绑定的字段
    @Transient
    private String[] roleIDs;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<Role>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isEnable() { return enable;  }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String[] getRoleIDs() {
        return roleIDs;
    }

    public void setRoleIDs(String[] roleIDs) {
        this.roleIDs = roleIDs;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

}
