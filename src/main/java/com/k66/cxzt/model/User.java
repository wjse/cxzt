package com.k66.cxzt.model;

import lombok.Data;

@Data
public class User {
	private int id;
	private String username;
	private String password;
	private String mobile;
	private String email;
	private UserType type;
	private String openId;
	private String region;
	private String company;

	public enum UserType{
		ADMIN,NORMAL
	}
}
