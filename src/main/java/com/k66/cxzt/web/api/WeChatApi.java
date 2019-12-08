package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.service.WechatService;
import com.k66.cxzt.utils.JWTUtil;
import com.k66.cxzt.web.vo.BindVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class WeChatApi {

	@Autowired
	WechatService wechatService;

	@Autowired
	UserService userService;

	@GetMapping(value = "/token" , params = {"code"})
	public JSONObject getToken(String code){
		String openId = wechatService.getOpenId(code);
		User user = userService.getByOpenId(openId);
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}
		String token = JWTUtil.createToken(user.getOpenId() , user.getUsername() , true);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("token" , token);
		return result;
	}

	@PostMapping(value = "/bind" , produces = "application/json")
	@Transactional
	public JSONObject bind(BindVO vo){
		User user = userService.getByMobile(vo.getMobile());
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}

		String openId = wechatService.getOpenId(vo.getCode());
		user.setOpenId(openId);

		String accessToken = wechatService.getAccessToken(openId);
		JSONObject wechatUser = wechatService.getUserInfo(accessToken , openId);
		if(null != wechatUser){
			user.setWechat(wechatUser.toJSONString());
		}

		userService.updateOpenIdAndWechat(user);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("token", JWTUtil.createToken(user.getOpenId() , user.getUsername() , true));
		return result;
	}
}
