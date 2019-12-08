package com.k66.cxzt.service.impl;

import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.mapper.UserMapper;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.utils.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		if(!user.getType().equals(User.UserType.ADMIN)){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}
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
}
