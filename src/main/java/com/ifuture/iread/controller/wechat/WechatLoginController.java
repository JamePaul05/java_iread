package com.ifuture.iread.controller.wechat;

import com.ifuture.iread.controller.BaseController;
import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.SessionRecord;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.service.IMemberService;
import com.ifuture.iread.service.ISessionRecordService;
import com.ifuture.iread.service.IUserService;
import com.ifuture.iread.util.Constants;
import com.ifuture.iread.util.StringUtil;
import com.ifuture.iread.util.wechat.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by maofn on 2017/3/31.
 */
@Controller
@RequestMapping("/wechat/login")
public class WechatLoginController extends BaseController {

    public static final Logger LOG = LoggerFactory
            .getLogger(WechatLoginController.class);

    @Value("#{configProperties['appid']}")
    private String appid;

    @Value("#{configProperties['secret']}")
    private String secret;

    @Value("#{configProperties['openid']}")
    private String configOpenid;

    @Autowired
    private ISessionRecordService sessionRecordService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/zhuanfa", method = RequestMethod.GET)
    public String redirect(HttpServletRequest request, HttpSession session, Model model) {
        String openId = configOpenid;

        if (!StringUtil.isNotNull(openId)) {
            // 授权获取用户openid和头像
            if (StringUtils.isEmpty(WeChatUtil.getProperties().get("appid"))) {
                WeChatUtil.getProperties().put("appid", appid);
                WeChatUtil.getProperties().put("appsecret", secret);
            }
            openId = authorizedUserInfo(request);
            if (StringUtils.isEmpty(openId)) {
                return "redirect:"+getSnsApiUserInfoUrl(request, "/wechat/login/zhuanfa");
            }
        }

        SessionRecord lastRecord = sessionRecordService.findLastRecord(openId);
        session.setAttribute(Constants.SESSION_OPEN_ID, openId);
        //如果没有登录记录
        // 对会员来说，1.没有注册 2.之前退出登录过，删掉了sessionrecord
        //对工作人员说，1.之前退出登录过，删掉了sessionrecord
        //对双重身份（既是工作人员又是会员），默认进会员
        if (lastRecord == null) {
            Member member = memberService.findOneByWechatId(openId);
            //如果存在会员数据
            if (member != null) {
                SessionRecord sessionRecord = new SessionRecord(openId, member);
                sessionRecordService.save(sessionRecord);
                session.setAttribute(Constants.SESSION_MEMBER, member);
                //如果没有支付
                if (member.getPayTime() == null) {
                    model.addAttribute("memberId", member.getId());
                    return "wechat/pay";
                } else {
                    return "redirect:/wechat/main";
                }
            } else {
                return "wechat/register";
            }
        } else {
            Member member = lastRecord.getMember();
            User user = lastRecord.getUser();
            //如果上次登录时会员身份
            if (member != null) {
                //如果没有支付
                if (member.getPayTime() == null) {
                    model.addAttribute("memberId", member.getId());
                    return "wechat/pay";
                } else {
                    session.setAttribute(Constants.SESSION_MEMBER, member);
                    return "redirect:/wechat/main";
                }
            } else if (user != null) {  //如果上次登录是工作人员身份
                session.setAttribute(Constants.SESSION_USER, user);
                return "redirect:/wechat/main";
            } else {
                return "wechat/register";
            }
        }
    }
}
