package com.k66.cxzt.mapper;

import com.k66.cxzt.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	User getByUsernameOrMobile(@Param("username") String username ,
														 @Param("mobile") String mobile);

	User get(int id);

	User getByOpenId(String openId);

	void updateOpenIdAndWechat(User user);
}
