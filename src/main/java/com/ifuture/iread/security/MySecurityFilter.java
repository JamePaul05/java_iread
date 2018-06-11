package com.ifuture.iread.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by maofn on 2017/3/13.
 */
@Service
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {
    // 注入资源数据定义器
    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;
    // 注入访问决策器
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    // 注入认证管理器
    @Autowired
    private AuthenticationManager myAuthenticationManager;

    /**
     *
     * 在spring容器初始化时就执行此方法
     */
    @PostConstruct
    public void init() {
        // System.err.println(" ---------------  MySecurityFilter init--------------- ");
        super.setAuthenticationManager(myAuthenticationManager);
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest)request;
//        HttpServletResponse httpResponse = (HttpServletResponse)response;

        // 用户未登录情况下 通过在地址栏输入有效的url 访问系统 可能造成系统出现问题，所以限制匿名用户登录 自动跳转到登录页面
        // TODO

        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }


    /**
     *
     * InterceptorStatusToken token = super.beforeInvocation(fi);
     * 在这个方法里主要做了几件事：
     * 1. Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
     *      object是url，获取了该url所需的权限
     * 2. Authentication authenticated = authenticateIfRequired();
     *      authenticated是用户所拥有的权限
     * 3. this.accessDecisionManager.decide(authenticated, object, attributes);
     *      访问控制，调用了我们自定义的accessDecisionManager中的方法
     */
    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = null;
        try {
            token = super.beforeInvocation(fi);
        } catch (Exception e) {
            // 用户登录情况下 系统中存在用户访问的资源url和权限，但是当前用户的角色中没有这个权限 所以提示跳转用户无权访问的页面
            if (e instanceof AccessDeniedException) {
                throw new AccessDeniedException("无权访问该页面");
            }
            return;
        }
        // TODO 不太懂
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.mySecurityMetadataSource;
    }
}
