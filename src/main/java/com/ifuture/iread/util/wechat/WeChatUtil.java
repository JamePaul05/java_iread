package com.ifuture.iread.util.wechat;

import com.ifuture.iread.util.MD5Util;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WeChatUtil {
	
	private static Logger log = LoggerFactory.getLogger(WeChatUtil.class);
	
	private static AccessToken accessToken;
	private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	private static Properties prop =  null;
	public static boolean CREATE_MENU = false;
	static {
		map.put("expires_in", String.valueOf("0"));
		map.put("create_time", String.valueOf("0"));
        prop =  new  Properties();
        /*prop.put("appid", "wx7e82b0b0158cfb18");
		prop.put("appsecret", "d4624c36b6795d1d99dcf0547af5443d");*/
	}

	public static Properties getProperties() {
		return prop;
	}
	
	public static AccessToken getAccessToken() {
	 	String _token = map.get("access_token");
        String expires_in = map.get("expires_in");
        String create_time = map.get("create_time");
        int _in = Integer.parseInt(expires_in);
        long _time = Long.parseLong(create_time);
		// 有效
        if ((System.currentTimeMillis() - _time) / 1000 < _in) {
            accessToken = new AccessToken();
            accessToken.setExpiresIn(_in);
            accessToken.setToken(_token);
        } else {
			// 无效
        	String appid = (String)prop.get("appid");
        	String appsecret = (String)prop.get("appsecret");
			String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
					+ appid + "&secret=" + appsecret;
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			// 如果请求成功
			if (null != jsonObject) {
				try {
					accessToken = new AccessToken();
					accessToken.setToken(jsonObject.getString("access_token"));
					accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
					
					map.put("access_token", accessToken.getToken());
					map.put("expires_in", String.valueOf(accessToken.getExpiresIn()));
					map.put("create_time", String.valueOf(System.currentTimeMillis()));
				} catch (Exception e) {
					accessToken = null;
					// 获取token失败
					log.error("获取token失败 errcode:{} errmsg:{}",
							jsonObject.getInt("errcode"),
							jsonObject.getString("errmsg"));
					e.printStackTrace();
				}
			}
        }
        return accessToken;
	}
	
	public static void createMenu() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		StringBuffer urlPath = request.getRequestURL();
        String tempContextUrl = urlPath.delete(urlPath.length() - request.getRequestURI().length(), urlPath.length()).append(request.getSession().getServletContext().getContextPath()).toString();
        net.sf.json.JSONArray arr = new net.sf.json.JSONArray();
		net.sf.json.JSONObject b1 = new net.sf.json.JSONObject();
		b1.put("type", "view");
		b1.put("name", "爱阅读.理想家");
		b1.put("key", "IREAD");
		b1.put("url", tempContextUrl + "/wechat/login/zhuanfa");
		arr.add(b1);
//
//		net.sf.json.JSONObject b2 = new net.sf.json.JSONObject();
//		b2.put("type", "view");
//		b2.put("name", "所有活动");
//		b2.put("key", "ALL_ACTIVITY");
//		b2.put("url", tempContextUrl + "/wechat/test");
//		arr.add(b2);
//
//		net.sf.json.JSONObject b3 = new net.sf.json.JSONObject();
//		b3.put("type", "view");
//		b3.put("name", "个人中心");
//		b3.put("key", "PERSON_INFORMATION");
//		b3.put("url", tempContextUrl + "/wechat/specialist/personInformation");
//		arr.add(b3);

		net.sf.json.JSONObject menu = new net.sf.json.JSONObject();
		menu.put("button", arr);

		AccessToken token = getAccessToken();
		String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token.getToken();
		try {
			log.info("menu: "+menu.toString());
			sendResponseToWechat(action, menu.toString());
			CREATE_MENU = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String sendResponseToWechat(String action, String content) throws Exception {
		URL url = new URL(action);
		HttpURLConnection http = (HttpURLConnection) url.openConnection(); 

		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
		http.setDoOutput(true);
		http.setDoInput(true);
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
		System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒

		http.connect();
		OutputStream os= http.getOutputStream();
		if (!StringUtils.isEmpty(content)) {
			os.write(content.toString().getBytes("UTF-8"));//传入参数
		}
		os.flush();
		os.close();

		InputStream is =http.getInputStream();
		int size =is.available();
		byte[] jsonBytes =new byte[size];
		is.read(jsonBytes);
		String message=new String(jsonBytes,"UTF-8");
		log.info("action:" + action + "response:" + message);
		return message;
	}

	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();  
        try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);  
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            	// 当有数据需要提交时
            	if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();  
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
            ce.printStackTrace();
        } catch (Exception e) {
            log.error("https request error:{}", e);
            e.printStackTrace();
        }
        return jsonObject;
	}

	public static String createSign(String characterEncoding, SortedMap<String, Object> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		//sb.append("key=" + ConfigUtil.API_KEY);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

}
