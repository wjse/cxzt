package com.k66.cxzt.service;

import com.k66.cxzt.model.User;

public interface UserService {
	User getByUsername(String username , String password);
	User getByMobile(String mobile , String password);
	User get(int id);
}
