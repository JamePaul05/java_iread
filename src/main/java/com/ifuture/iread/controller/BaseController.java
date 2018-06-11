package com.ifuture.iread.controller;

import com.ifuture.iread.util.wechat.WeChatUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by maofn on 2017/3/26.
 */
public abstract class BaseController {
    private  static  final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Value("#{configProperties['appid']}")
    private String appid;

    @Value("#{configProperties['secret']}")
    private String secret;

    public String getSnsApiUserInfoUrl(HttpServletRequest request, String authorizedOriginalUrl) {
        StringBuffer urlPath = request.getRequestURL();
        String tempContextUrl = urlPath.delete(urlPath.length() - request.getRequestURI().length(), urlPath.length()).append(request.getSession().getServletContext().getContextPath()).toString();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
        if (StringUtils.isEmpty(WeChatUtil.getProperties().getProperty("appid"))) {
            WeChatUtil.getProperties().put("appid", appid);
            WeChatUtil.getProperties().put("appsecret", secret);
        }
        url = url.replace("APPID", WeChatUtil.getProperties().getProperty("appid"));
        url = url.replace("REDIRECT_URI", urlEnodeUTF8(tempContextUrl + authorizedOriginalUrl));
        logger.info("redirect: "+url);
        return url;
    }

    public String authorizedUserInfo(HttpServletRequest request) {
        // 第一步：用户同意授权，获取code
        String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        // 第二步：通过code换取网页授权access_token
        String authUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        if (StringUtils.isEmpty(WeChatUtil.getProperties().getProperty("appid"))) {
            WeChatUtil.getProperties().put("appid", appid);
            WeChatUtil.getProperties().put("appsecret", secret);
        }
        authUrl = authUrl.replace("APPID", (String)WeChatUtil.getProperties().get("appid"));
        authUrl = authUrl.replace("SECRET", (String)WeChatUtil.getProperties().get("appsecret"));
        authUrl = authUrl.replace("CODE", code);
        net.sf.json.JSONObject openIdJsonObject = WeChatUtil.httpRequest(authUrl, "GET", null);
        String openId = (String) openIdJsonObject.get("openid");
        String accessToken = (String) openIdJsonObject.get("access_token");

        // 第三步：拉取用户信息(需scope为 snsapi_userinfo)
        /*String userInfoAuthUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        userInfoAuthUrl = userInfoAuthUrl.replace("ACCESS_TOKEN", accessToken);
        userInfoAuthUrl = userInfoAuthUrl.replace("OPENID", openId);
        net.sf.json.JSONObject userInfoJsonObject = WeChatUtil.httpRequest(userInfoAuthUrl, "GET", null);
        if (userInfoAuthUrl != null) {
            logger.info("portrait: " + userInfoJsonObject.get("headimgurl"));
        }*/
        return openId;
    }

    public static String urlEnodeUTF8(String str){
        String result = str;
        try {
            result = URLEncoder.encode(str,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getContextPath(HttpServletRequest request) {
        return request.getScheme() +"://" + request.getServerName()  + ":" +request.getServerPort() +request.getContextPath();
    }

}
