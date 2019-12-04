package com.k66.cxzt.utils;

import com.k66.cxzt.model.User;

public class WebUserUtil {
	private static final ThreadLocal<User> USER_LOCAL = new ThreadLocal<>();

	public static void setUser(User user){
		USER_LOCAL.set(user);
	}

	public static User getUser(){
		return USER_LOCAL.get();
	}
}
