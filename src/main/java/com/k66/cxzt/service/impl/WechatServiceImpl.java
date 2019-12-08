package com.k66.cxzt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.service.WechatService;
import com.k66.cxzt.utils.HttpSender;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WechatServiceImpl implements WechatService {

	@Value("${wechat.api.code2session}")
	private String code2session;

	@Value("${wechat.api.access-token}")
	private String accessTokenUrl;

	@Value("${wechat.api.user-info}")
	private String userInfoUrl;

	@Value("${wechat.app-id}")
	private String appId;

	@Value("${wechat.app-secret}")
	private String appSecret;

	private static JSONObject accessToken;

	@Override
	public String getOpenId(String code) {
		String url = String.format(code2session , appId , appSecret , code);
		GetMethod method = new GetMethod(url);
		JSONObject result = HttpSender.send(method);
		if(null != result){
			String returnCode = result.getString("errcode");
			if("0".equals(returnCode)){
				return result.getString("openid");
			}
		}
		return null;
	}

	@Override
	public String getAccessToken(String openId) {
		if(null != accessToken){
			Date startTime = accessToken.getDate("startTime");
			long currentTime = System.currentTimeMillis();
			if(currentTime - startTime.getTime() < 6000 * 1000){
				return accessToken.getString("token");
			}
		}

		String url = String.format(accessTokenUrl , appId , appSecret);
		GetMethod method = new GetMethod(url);
		JSONObject result = HttpSender.send(method);
		if(null != result && "0".equals(result.getString("errcode"))){
			accessToken = new JSONObject();
			accessToken.put("startTime" , new Date());
			accessToken.put("token" , result.getString("access_token"));
		}
		return accessToken.getString("token");
	}

	@Override
	public JSONObject getUserInfo(String accessToken, String openId) {
		String url = String.format(userInfoUrl , accessToken , openId);
		GetMethod method = new GetMethod(url);
		return HttpSender.send(method);
	}
}
