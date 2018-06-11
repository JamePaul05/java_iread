package com.ifuture.iread.util.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifuture.iread.util.UUIDUtil;
import com.ifuture.iread.util.wechat.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class WechatHelper {
	@Value("#{configProperties['wechatToken']}")
	private String wechatToken;

	@Value("#{configProperties['appid']}")
	private String appid;

	@Value("#{configProperties['secret']}")
	private String secret;
	
	private String accessToken;
	
	private long startTime;
	
	private long startTickeTime;
	
	private String ticket;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	private String getToken() throws JsonProcessingException, IOException{
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
		String json= HttpUtil.getUrl(url);
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode rootNode = mapper.readTree(json); 
		return rootNode.path("access_token").asText();
	}
	
	public String getAccessToken() throws JsonProcessingException, IOException{
		if(startTime==0){
			startTime=System.currentTimeMillis();
		}
		while(StringUtils.isEmpty(accessToken)||System.currentTimeMillis()-startTime>60*60*1000){
			accessToken=getToken();
			startTime=System.currentTimeMillis();
		}
	
		return accessToken;
	}
	
	public String getAccessTicket() throws JsonProcessingException, IOException{
		if(startTickeTime==0){
			startTickeTime=System.currentTimeMillis();
		}
		while(StringUtils.isEmpty(ticket)||System.currentTimeMillis()-startTickeTime>60*60*1000){
			ticket=getTicket();
			startTickeTime=System.currentTimeMillis();
		}
	
		return ticket;
	}
	
	private String getTicket() throws JsonProcessingException, IOException{
		String token=getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi";
		String json=HttpUtil.getUrl(url);
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode rootNode = mapper.readTree(json); 
		return rootNode.path("ticket").asText();
	}
	
	public JsonNode getTokenByCode(String code) throws JsonProcessingException, IOException{
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		String jsonString=HttpUtil.getUrl(url);
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode rootNode = mapper.readTree(jsonString); 
		return rootNode;
	}
	
//	private JsonNode getTokenByCode(String code) throws JsonProcessingException, IOException{
//		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
//		String jsonString=HttpUtil.getUrl(url);
//		ObjectMapper mapper = new ObjectMapper();  
//		JsonNode rootNode = mapper.readTree(jsonString); 
//		return rootNode;
//	}
	
	public JsonNode getUserInfo(String code) throws JsonProcessingException, IOException{
		JsonNode json=getTokenByCode(code);
		String access_token=json.path("access_token").asText();
		String openid=json.path("openid").asText();
		String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String jsonString=HttpUtil.getUrl(url);
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonReturn = mapper.readTree(jsonString); 
		return jsonReturn;
	}

	/*生成JS-SDK权限验证的签名*/
	public Map<String, String> sign(String url) throws NoSuchAlgorithmException, IOException {
		Map<String, String> map = new HashMap<String, String>();

		//取的jsapi_ticket。
		String jsapi_ticket = getTicket();
		//随机字符串
		String nonce_str = getNonceStr();
		//时间戳
		String timestamp = getTimestamp();
		String string1;
		//生成的签名
		String signature = "";

		/*Map<String, String> tmp = new HashMap<>();
		tmp.put("jsapi_ticket", jsapi_ticket);
		tmp.put("noncestr", nonce_str);
		tmp.put("timestamp", timestamp);
		tmp.put("url", url);

		String[] arr = new String[]{jsapi_ticket, timestamp, nonce_str, url};
		// 将 token, timestamp, nonce 三个参数进行字典排序
		Arrays.sort(arr);*/

		//注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket +
				"&noncestr=" + nonce_str +
				"&timestamp=" + timestamp +
				"&url=" + url;
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(string1.getBytes("UTF-8"));
		signature = byteToHex(crypt.digest());

		map.put("url", url);
		map.put("jsapi_ticket", jsapi_ticket);
		map.put("nonceStr", nonce_str);
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		return map;
	}

	/**
	 * 生成随机字符串
	 * @return
	 */
	public String getNonceStr() {
		return UUIDUtil.generateUUID();
	}

	/**
	 * 生成时间戳
	 * @return
	 */
	public String getTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
