package com.ifuture.iread.security;

import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.entity.UserDetail;
import com.ifuture.iread.repository.MenuRepository;
import com.ifuture.iread.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by maofn on 2017/3/13.
 * 该类是用户信息的定义和验证
 * 这个类主要是处理用户登录信息，在用户输入用户名和密码后，
 * spring security会带着用户名调用类里面的loadUserByUsername(usrename)方法，
 * 通过用户名查出用户信息，然后把数据库中查出的用户密码和刚刚用户输入的存储在session中的密码做比较，然后判断该用户是否合法
 */
@Service("myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    /**
     * Transactional给整个业务添加事务管理
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据登录名获取登陆用户
        User user = userRepository.findByUserName(username);
        if(null == user) {
            throw new UsernameNotFoundException("用户" + username + "不存在");
        }
        Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user);
        UserDetail userdetail = new UserDetail(user.getUserName(), user.getPassWord()==null?"":user.getPassWord(),
                true,
                true, true, true, grantedAuths
        );

        BeanUtils.copyProperties(user, userdetail);

        return userdetail;
    }

    private Collection<GrantedAuthority> obtionGrantedAuthorities(User user) {
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        //判断是否是管理员，是的话获取所有菜单设为管理员权限
        /*if (user.getUserName().equals("admin")) {
            List<Menu> menus = menuRepository.findAll();
            for (Menu menu : menus) {
                authSet.add(new SimpleGrantedAuthority(menu.getCode()));
            }
            return authSet;
        }*/
        //获取角色集合
        List<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //获取每个角色的权限集
                for (Menu menu : role.getMenus()) {
                    authSet.add(new SimpleGrantedAuthority(menu.getCode()));
                }
            }
        }
        return authSet;
    }
}
