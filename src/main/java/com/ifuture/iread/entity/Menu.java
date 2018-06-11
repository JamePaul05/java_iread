package com.ifuture.iread.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by maofn on 2017/2/23.
 *
 * 菜单实体。
 * 实现了Comparable用于排序
 */
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity implements Comparable<Menu> {

    //URL地址．例如：/videoType/query　　
    //不需要项目名和http://xxx:8080
    @NotBlank(message = "此字段不能为空！")
    private String url;

    //这个权限KEY是唯一的，新增时要注意，
    @NotBlank(message = "此字段不能为空！")
    private String code;

    //菜单名称
    @NotBlank(message = "此字段不能为空！")
    private String menuName;

    //菜单排序
    private int sequence;

    private  String pinyin;

    //Transient 不会再数据库插入字段
    //用于获取jsp页面上绑定的字段
    @Transient
    private String[] roleIDS;

    //是否有效
    @Type(type = "yes_no")
    private boolean enable = true;

    //上级菜单
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Menu parent;


    //指定menu和role两个都是维护端，即增加任何一方都会级联到中间表
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @Transient
    private Set<Menu> children = new TreeSet<>();
    //是否是子节点
    private String isChildNode = "0" ;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Menu getParent() {
        return parent;
    }
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Set<Menu> getChildren() {
        return children;
    }

    public void setChildren(Set<Menu> children) {
        this.children = children;
    }

    @Override
    public int compareTo(Menu o) {
        return this.getSequence() - o.getSequence();
    }

    public Menu() {
    }

    public Menu(String url, String code, String menuName, int sequence, Menu parent) {
        this.url = url;
        this.code = code;
        this.menuName = menuName;
        this.sequence = sequence;
        this.parent = parent;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String[] getRoleIDS() {
        return roleIDS;
    }

    public void setRoleIDS(String[] roleIDS) {
        this.roleIDS = roleIDS;
    }

    public String getIsChildNode() {
        return isChildNode;
    }

    public void setIsChildNode(String isChildNode) {
        this.isChildNode = isChildNode;
    }
}
