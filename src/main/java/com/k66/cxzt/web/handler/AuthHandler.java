package com.k66.cxzt.web.handler;

import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.JWTUtil;
import com.k66.cxzt.utils.WebContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthHandler implements HandlerInterceptor {
	static final String TOKEN = "Token";

	@Autowired
	UserService userService;

	@Autowired
	MessageI18NProcessor messageI18NProcessor;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader(TOKEN);
		if(StringUtils.isBlank(token)){
			sendError(response);
			return false;
		}

		if(JWTUtil.isExpire(token)){
			sendError(response);
			return false;
		}

		Pair<String , String> pair = JWTUtil.get(token);
		if(null == pair){
			sendError(response);
			return false;
		}

		User user = userService.get(Integer.parseInt(pair.getKey()));
		if(null == user){
			sendError(response);
			return false;
		}

		if(JWTUtil.isWillExpire(token)){
			String web = request.getHeader("from");
			if(StringUtils.isNoneBlank(web) && Boolean.valueOf(web)){
				String newToken = JWTUtil.createToken(String.valueOf(user.getId()) , user.getUsername() , false);
				response.addHeader("Token" , newToken);
			}
		}

		WebContextUtil.setUser(user);
		return true;
	}

	private void sendError(HttpServletResponse response) throws IOException {
		response.sendError(401 , messageI18NProcessor.getMessage("invalidate.auth"));
	}
}
