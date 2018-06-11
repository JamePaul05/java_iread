package com.ifuture.iread.controller;

import com.ifuture.iread.util.CaptchaUtil;
import com.ifuture.iread.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by maofn on 2017/5/11.
 *
 * 生成验证码类
 */
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {

    /**
     * 获取图形码
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        ServletOutputStream sos = response.getOutputStream();
        CaptchaUtil captchaUtil = new CaptchaUtil();
        if (StringUtil.isNotNull((String)session.getAttribute("kaptcha"))) {
            session.removeAttribute("kaptcha");
        }
        session.setAttribute("kaptcha", captchaUtil.getCode());
        captchaUtil.write(sos);
    }

    /**
     * 图形码验证
     */
    @RequestMapping(value = "/checkKaptcha")
    @ResponseBody
    public String checkKaptcha(@RequestParam("input")String input, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (!StringUtil.isNotNull(input)) {
            jsonObject.put("flag", false);
        } else {
            if (input.equalsIgnoreCase(kaptcha)) {
                jsonObject.put("flag", true);
            } else {
                jsonObject.put("flag", false);
            }
        }
        return jsonObject.toString();
    }

}
