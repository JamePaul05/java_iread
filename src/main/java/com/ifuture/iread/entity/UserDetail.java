package com.ifuture.iread.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by maofn on 2017/3/13.
 */
public class UserDetail extends User {

    private String id;
    private String nickName;

    public UserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * @param username
     * @param password
     * @param enabled   是否可用
     * @param accountNonExpired 账户是否没有失效
     * @param credentialsNonExpired 证书是否没有失效
     * @param accountNonLocked  账户是否没有锁定
     * @param authorities   权限集合
     */
    public UserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
