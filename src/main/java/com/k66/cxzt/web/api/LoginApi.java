package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.JWTUtil;
import com.k66.cxzt.utils.WebContextUtil;
import com.k66.cxzt.web.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApi {

	@Autowired
	UserService userService;

	@PostMapping(value = "/login" , produces = "application/json")
	public JSONObject login(@RequestBody LoginUser loginUser){
		User user = userService.getByUsername(loginUser.getUsername() , loginUser.getPassword());
		WebContextUtil.setUser(user);
		String token = JWTUtil.createToken(String.valueOf(user.getId()) , user.getUsername() , false);
		JSONObject resp = new JSONObject();
		resp.put("code" , 200);
		resp.put("token" , token);
		resp.put("nickName" , user.getNickName());
		resp.put("type" , user.getType());
		return resp;
	}

	@GetMapping("/test")
	public User test(){
		return new User();
	}
}
