package com.ifuture.iread.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by maofn on 2017/2/23.
 */
@Controller
public class LoginController {

    /**
     * spring利用RequestParam指定传入参数，如果前端不传error参数，会报错
     * 但是通过required = false可以不传，不传的话将error设为null
     * （注意：int不传会报错因为int无法设null）
     * @param error
     * @return
     */
    @RequestMapping("/admin/login")
    public ModelAndView login(
            @RequestParam(value = "error", required = false)String error,
            @RequestParam(value = "logout", required = false)String logout,
            @RequestParam(value = "invalid", required = false)String invalid) {

        ModelAndView mv = new ModelAndView();
        if(error != null) {
            mv.addObject("msg", "用户名密码不正确！");
        }
        if (logout != null) {
            mv.addObject("msg", "成功登出！");
        }
        if (invalid != null) {
            mv.addObject("msg", "当前账号已被他人登陆或者登陆超时！");
        }
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping("/admin/error")
    public String error() {
        return "error";
    }

    @RequestMapping("/admin/403")
    public String error403(Model model) {
        model.addAttribute("status","403");
        model.addAttribute("msg","没有权限访问该页面！");
        return "error";
    }

}
