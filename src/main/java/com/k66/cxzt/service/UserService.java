package com.k66.cxzt.service;

import com.github.pagehelper.PageInfo;
import com.k66.cxzt.model.User;

import java.util.Map;

public interface UserService {
	User getByUsername(String username , String password);
	User getByMobile(String mobile);
	User get(int id);
	User getByOpenId(String openId);
	void updateOpenIdAndWechat(User user);

	PageInfo<User> queryForPage(int pageNum, int pageSize, Map<String, Object> toMap);

	void resetPassword(int id);

	void updatePassword(int id, String password);

    void save(User user);
}
