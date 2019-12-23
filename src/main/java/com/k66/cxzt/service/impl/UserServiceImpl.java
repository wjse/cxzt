package com.k66.cxzt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.mapper.UserMapper;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public User getByUsername(String username , String password) {
		if(StringUtils.isAnyBlank(username , password)){
			throw new BusinessException(ErrorCode.ERROR , "parameter.error");
		}

		User user = userMapper.getByUsernameOrMobile(username , null);
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}

		if(!EncryptUtil.hex(password , "md5").equals(user.getPassword())){
			throw new BusinessException(ErrorCode.FORBIDDEN , "user.wrongPassword");
		}

//		if(!user.getType().equals(User.UserType.ADMIN)){
//			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
//		}
		return user;
	}

	@Override
	public User getByMobile(String mobile) {
		if(StringUtils.isBlank(mobile)){
			throw new BusinessException(ErrorCode.ERROR , "parameter.error");
		}

		return userMapper.getByUsernameOrMobile(null , mobile);
	}

	@Override
	public User get(int id) {
		return userMapper.get(id);
	}

	@Override
	public User getByOpenId(String openId) {
		return userMapper.getByOpenId(openId);
	}

	@Override
	public void updateOpenIdAndWechat(User user) {
		userMapper.updateOpenIdAndWechat(user);
	}

	@Override
	public PageInfo<User> queryForPage(int pageNum, int pageSize, Map<String, Object> toMap) {
		return PageHelper.startPage(pageNum , pageSize).doSelectPageInfo(() -> userMapper.queryForList(toMap));
	}

	private static final String DEFAULT_PASSWORD = "123456";

	@Override
	public void resetPassword(int id) {
		String password = EncryptUtil.hex(EncryptUtil.hex(DEFAULT_PASSWORD , "sha1") , "md5");
		userMapper.updatePassword(id , password);
	}

	@Override
	public void updatePassword(int id, String password) {
		userMapper.updatePassword(id , EncryptUtil.hex(password , "md5"));
	}

	@Override
	public void save(User user) {
		user.setPassword(EncryptUtil.hex(EncryptUtil.hex(DEFAULT_PASSWORD , "sha1") , "md5"));
		userMapper.insert(user);
	}

	public static void main(String[] args) {
		System.out.println(EncryptUtil.hex(EncryptUtil.hex(DEFAULT_PASSWORD , "sha1") , "md5"));
	}
}
