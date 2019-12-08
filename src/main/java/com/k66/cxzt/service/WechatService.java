package com.k66.cxzt.service;

import com.alibaba.fastjson.JSONObject;

public interface WechatService {

	String getOpenId(String code);

	String getAccessToken(String openId);

	JSONObject getUserInfo(String accessToken, String openId);
}
