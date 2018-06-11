package com.ifuture.iread.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by maofn on 2017/3/15.
 *
 * 页面跳转控制器
 */
@Controller
public class PageController {

    @RequestMapping("/admin/main")
    public String main() {
        return "tiles.main";
    }

}
