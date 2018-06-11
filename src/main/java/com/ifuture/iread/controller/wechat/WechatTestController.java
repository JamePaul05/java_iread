package com.ifuture.iread.controller.wechat;

import com.ifuture.iread.util.wechat.InputMessage;
import com.ifuture.iread.util.wechat.OutputMessage;
import com.ifuture.iread.util.wechat.WeChatUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by maofn on 2017/3/31.
 * 用于测试微信的“验证服务器地址的有效性”
 */
@Controller
@RequestMapping("/wechat/test")
public class WechatTestController {

    /*https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect*/

    @Value("#{configProperties['appid']}")
    private String appid;

    @Value("#{configProperties['secret']}")
    private String secret;

    private static final String token = "iread";

    @RequestMapping(value = "testConnect", method = {RequestMethod.GET, RequestMethod.POST})
    public void testConnect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戮
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        String result = "";
        if (!StringUtils.isEmpty(signature) && !StringUtils.isEmpty(echostr)) {
            if (this.checkSignature(signature, timestamp, nonce)) {
                result = echostr;
            }
            try {
                OutputStream os = response.getOutputStream();
                os.write(result.getBytes("UTF-8"));
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WeChatUtil.getProperties().put("appid", appid);
        WeChatUtil.getProperties().put("appsecret", secret);
        //处理接收消息
        ServletInputStream in = request.getInputStream();

        // 将POST流转换为XStream对象
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(InputMessage.class);
        xs.processAnnotations(OutputMessage.class);
        // 将指定节点下的xml节点数据映射为对象
        xs.alias("xml", InputMessage.class);
        // 将流转换为字符串
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            xmlMsg.append(new String(b, 0, n, "UTF-8"));
        }
       //将xml内容转换为InputMessage对象
        if (!StringUtils.isEmpty(xmlMsg.toString())) {
            InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());
         //关注事件
            if ("subscribe".equals(inputMsg.getEvent())) {
                WeChatUtil.createMenu();
            }
        }
    }

    private boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token, timestamp, nonce 三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        String tmpStr = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 shal 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()): false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param digest
     * @return
     */
    private String byteToStr(byte[] digest) {
        String strDigest = "";
        for(int i = 0; i < digest.length; i++){
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     * @param b
     * @return
     */
    private String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];
        String s = new String(tempArr);
        return s;
    }
}


