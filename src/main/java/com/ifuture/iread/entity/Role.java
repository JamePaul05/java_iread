package com.ifuture.iread.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.*;

/**
 * Created by maofn on 2017/2/23.
 * 角色类
 */
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @NotBlank(message = "此字段不能为空！")
    private String roleName;

    @NotBlank(message = "此字段不能为空！")
    private String code;

    private String pinyin;

    @Type(type = "yes_no")
    private boolean enable = true;

    @Column(length = 200)
    private String remark;

    //权限集合, role可以知道它有多少menu，menu不需要知道它有什么role
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<Menu> menus = new TreeSet<Menu>();

    public Role() {}

    public Role(String roleName, String code, boolean enable) {
        this.roleName = roleName;
        this.code = code;
        this.enable = enable;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
