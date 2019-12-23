package com.k66.cxzt.mapper;

import com.k66.cxzt.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
	User getByUsernameOrMobile(@Param("username") String username ,
														 @Param("mobile") String mobile);

	User get(int id);

	User getByOpenId(String openId);

	void updateOpenIdAndWechat(User user);

	List<User> queryForList(Map<String, Object> toMap);

	void updatePassword(@Param("id") int id , @Param("password") String password);
}
