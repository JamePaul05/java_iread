package com.ifuture.iread.exception;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2016/12/23.
 */
@Service
public class CustomerExceptionResolver implements HandlerExceptionResolver {
    private  static  final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView=new ModelAndView();
        logger.error(ex.getLocalizedMessage());
        ex.printStackTrace();
        modelAndView.addObject("status","系统异常");
        modelAndView.addObject("msg","请联系管理员");
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
