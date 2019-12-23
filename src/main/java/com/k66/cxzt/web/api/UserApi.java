package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.WebContextUtil;
import com.k66.cxzt.web.vo.LoginUser;
import com.k66.cxzt.web.vo.UserQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserApi {

	@Autowired
	UserService userService;

	@GetMapping(params = {"pageNum" , "pageSize"})
	public JSONObject query(int pageNum , int pageSize , UserQueryVO vo){
		PageInfo<User> page = userService.queryForPage(pageNum , pageSize , vo.toMap());
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("data" , page);
		return result;
	}

	@PutMapping("/password/{id}")
	public JSONObject resetPassword(@PathVariable("id") int id){
		userService.resetPassword(id);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		return result;
	}

	@PutMapping("/password")
	public JSONObject updatePassword(@RequestBody LoginUser vo){
		User user = WebContextUtil.getUser();
		userService.updatePassword(user.getId() , vo.getPassword());
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		return result;
	}
}
