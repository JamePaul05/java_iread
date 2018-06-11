package com.ifuture.iread.util.wechat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

public class HttpUtil {
	public static final Logger LOG = LoggerFactory
			.getLogger(HttpUtil.class);
	@SuppressWarnings("deprecation")
	public static String getUrl(String url){
        String result = null;
        try {
            HttpGet request = new HttpGet(url);
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result= EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	/**
     * 发起https请求并获取结�?
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST�?
     * @param outputStr 提交的数�?
     * 
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = { new MyTrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
        	LOG.error("Weixin server connection timed out.");
        } catch (Exception e) {
        	LOG.error("https request error:{}", e);
        }
        return null;
    }
}
