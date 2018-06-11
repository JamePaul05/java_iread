package com.ifuture.iread.util.wechat;

public class ConfigUtil {
	/**
	 * 服务号相关信息
	 */
	 public final static String MCH_ID = "1397807902";//商户号
	 public final static String API_KEY = "2058c1e6123842b1ae12a977fb474df6";//API密钥
	 public final static String SIGN_TYPE = "MD5";//签名加密方式

	/**
	 * 微信支付接口地址
	 */
	//微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
