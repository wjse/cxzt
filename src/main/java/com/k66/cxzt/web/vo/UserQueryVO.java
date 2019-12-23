package com.k66.cxzt.web.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserQueryVO {

	String nickName;
	String mobile;

	public Map<String , Object> toMap(){
		Map<String , Object> map = new HashMap<>();
		if(StringUtils.isNotBlank(nickName)){
			map.put("nickName" , nickName);
		}

		if(StringUtils.isNotBlank(mobile)){
			map.put("mobile" , mobile);
		}
		return map;
	}
}
