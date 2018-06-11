package com.ifuture.iread.controller.wechat;

import com.ifuture.iread.entity.CheckCode;
import com.ifuture.iread.entity.Member;
import com.ifuture.iread.service.ICheckCodeService;
import com.ifuture.iread.service.IMemberService;
import com.ifuture.iread.util.Constants;
import com.ifuture.iread.util.RandomUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by maofn on 2017/5/15.
 */
@Controller
@RequestMapping("/sms")
public class SmsApi {

    private  static  final org.slf4j.Logger logger = LoggerFactory.getLogger(SmsApi.class);

    @Value("#{configProperties['apikey']}")
    private String apikey;

    /**
     * 服务http地址
     */
    private static String BASE_URI = "http://yunpian.com";
    /**
     * 服务版本号
     */
    private static String VERSION = "v1";
    /**
     * 编码格式
     */
    private static String ENCODING = "UTF-8";
    /**
     * 查账户信息的http地址
     */
    private static String URI_GET_USER_INFO = BASE_URI + "/" + VERSION
            + "/user/get.json";
    /**
     * 通用发送接口的http地址
     */
    private static String URI_SEND_SMS = BASE_URI + "/" + VERSION
            + "/sms/send.json";
    /**
     * 模板发送接口的http地址
     */
    private static String URI_TPL_SEND_SMS = BASE_URI + "/" + VERSION
            + "/sms/tpl_send.json";

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ICheckCodeService checkCodeService;

    @RequestMapping("/sendCheckCode")
    @ResponseBody
    public String sendCheckCode(@RequestParam(value = "mobile") String mobile) {
        JSONObject jsonObject = new JSONObject();
        //验证是否是手机号
        boolean isMobile = Pattern.matches(Constants.REGEX_MOBILE, mobile);
        if (!isMobile) {
            jsonObject.put("flag", false);
            jsonObject.put("msg", "请输入正确格式手机号码！");
            return jsonObject.toString();
        }
        //验证手机是否已被注册
        Member member = memberService.findOneByMobile(mobile);
        if (member != null) {
            jsonObject.put("flag", false);
            jsonObject.put("msg", "该手机号已被注册");
            return jsonObject.toString();
        }
        ////取得最新的一条
        List<CheckCode> checkCodes = checkCodeService.findOneByMobileOrderBySentDate(mobile);
        String code = "";
        //是否发送了新的验证码
        boolean isNewCode = false;
        if(checkCodes != null && checkCodes.size() > 0){
            CheckCode checkCode = checkCodes.get(0);
            Date date = Calendar.getInstance().getTime();
            //如果没有失效并且没有验证过
            if(date.before(checkCode.getExpirationDate()) && checkCode.getVerifyDate() == null) {
                code = checkCode.getCode();
            } else {
                code = RandomUtil.generateNumberString(6);
                isNewCode = true;
            }
        } else {
            code = RandomUtil.generateNumberString(6);
            isNewCode = true;
        }
        // 设置对应的模板变量值
        String tpl_value = "【爱未来公益】您的验证码是" + code + "，有效时间为5分钟。如非本人操作，请忽略本短信。";
        boolean flag = false;
        String msg = "";
        try {
            //发送短信，接受返回的信息
            String message = this.sendMessage(this.apikey, tpl_value, mobile);
            JSONObject j = new JSONObject(message);
            Object obj = j.get("code");
            int status = (int)obj;
            //如果发送成功
            if(status == 0) {
                //新的验证码才会保存
                if(isNewCode) {
                    checkCodeService.save(code, mobile);
                }
                flag = true;
            } else {
                flag = false;
                msg = (String) j.get("msg");
                logger.error("msg>>>>>>>>>>" + msg);
            }
        } catch(Exception e) {
            e.printStackTrace();
            jsonObject.put("flag", false);
            jsonObject.put("msg", "系统出错，请联系管理员！");
            return jsonObject.toString();
        }
        jsonObject.put("flag", flag);
        jsonObject.put("msg", msg);
        return jsonObject.toString();
    }

    private String sendMessage(String apikey, String text, String mobile) throws IOException {
        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[3];
        nameValuePairs[0] = new NameValuePair("apikey", apikey);
        nameValuePairs[1] = new NameValuePair("text", text);
        nameValuePairs[2] = new NameValuePair("mobile", mobile);
        PostMethod method = new PostMethod(URI_SEND_SMS);
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }

    /**
     * 检查验证码是否正确
     */
    @RequestMapping(value = "/checkCodeValidate", method = RequestMethod.GET)
    @ResponseBody
    public String checkCodeValidate(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String sendCode = request.getParameter("sendCode");
        String mobile = request.getParameter("mobile");
        //找出验证码
        CheckCode checkCode = checkCodeService.findByMobileAndCode(mobile, sendCode);
        if (checkCode != null) {
            Date date = Calendar.getInstance().getTime();
            //如果已经验证过
            if (checkCode.getVerifyDate() != null) {
                jsonObject.put("flag", false);
                jsonObject.put("type", 3);
                jsonObject.put("msg", "验证码已验证过，请重新发送");
            } else {
                //如果失效
                if (date.after(checkCode.getExpirationDate())) {
                    jsonObject.put("flag", false);
                    jsonObject.put("type", 1);
                    jsonObject.put("msg", "验证码已失效");
                } else {
                    /*checkCode.setVerifyDate(new Date());
                    checkCodeService.update(checkCode);*/
                    jsonObject.put("flag", true);
                    jsonObject.put("type", 0);
                    jsonObject.put("msg", "验证成功");
                }
            }
        } else {
            jsonObject.put("flag", false);
            jsonObject.put("type", 2);
            jsonObject.put("msg", "验证码错误");
        }
        return jsonObject.toString();
    }
}
