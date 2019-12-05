package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.JWTUtil;
import com.k66.cxzt.utils.WebContextUtil;
import com.k66.cxzt.web.vo.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginApi {

	static final String FROM_WEB = "web";
	static final String FROM_WE_CHAT = "wechat";

	@Autowired
	UserService userService;

	@PostMapping(value = "/login" , produces = "application/json")
	public JSONObject login(@RequestBody LoginUser loginUser){
		String from = loginUser.getFrom();
		if(StringUtils.isBlank(from)){
			from = FROM_WEB;
		}

		User user = null;
		String token = null;
		if(FROM_WEB.equals(from)){
			user = userService.getByUsername(loginUser.getUsername() , loginUser.getPassword());
			WebContextUtil.setUser(user);
			token = JWTUtil.createToken(String.valueOf(user.getId()) , user.getUsername() , false);
		}else if(FROM_WE_CHAT.equals(from)){
			user = userService.getByMobile(loginUser.getUsername() , loginUser.getPassword());
			token = JWTUtil.createToken(String.valueOf(user.getId()) , user.getUsername() , true);
		}

		if(null == user || null == token){
			throw new BusinessException(ErrorCode.ERROR , "parameter.error");
		}
		JSONObject resp = new JSONObject();
		resp.put("code" , 200);
		resp.put("token" , token);
		resp.put("username" , user.getUsername());
		return resp;
	}

	@GetMapping("/test")
	public User test(){
		return new User();
	}
}
