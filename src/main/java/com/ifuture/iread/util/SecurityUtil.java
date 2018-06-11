package com.ifuture.iread.util;

import com.ifuture.iread.entity.User;
import com.ifuture.iread.entity.UserDetail;
import com.ifuture.iread.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by maofn on 2017/3/16.
 *
 * 有关springsecurity 的工具类
 */
public class SecurityUtil {

    /**
     * 到SecurityContextHolder中获取用户信息
     * @param userRepository
     * @return
     */
    public static User getUser(UserRepository userRepository) {
        if (SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return null;
        }
        UserDetail userDetail = (UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUserName(userDetail.getUsername());
        return user;
    }
}
