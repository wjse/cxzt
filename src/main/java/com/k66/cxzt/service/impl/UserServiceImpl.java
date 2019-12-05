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

		User user = userMapper.getByUsernameOrMobile(username , null , User.UserType.ADMIN);
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}

		if(!EncryptUtil.hex(password , "md5").equals(user.getPassword())){
			throw new BusinessException(ErrorCode.FORBIDDEN , "user.wrongPassword");
		}
		return user;
	}

	@Override
	public User getByMobile(String mobile , String password) {
		if(StringUtils.isAnyBlank(mobile , password)){
			throw new BusinessException(ErrorCode.ERROR , "parameter.error");
		}

		User user = userMapper.getByUsernameOrMobile(null , mobile , User.UserType.NORMAL);
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}

		if(!EncryptUtil.hex(password , "md5").equals(user.getPassword())){
			throw new BusinessException(ErrorCode.FORBIDDEN , "user.wrongPassword");
		}
		return user;
	}

	@Override
	public User get(int id) {
		return userMapper.get(id);
	}
}
