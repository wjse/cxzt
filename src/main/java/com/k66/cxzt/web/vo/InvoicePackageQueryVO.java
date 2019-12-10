package com.k66.cxzt.web.vo;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class InvoicePackageQueryVO {
	Integer userId;
	Date startTime;
	Date endTime;

	public Map<String , Object> toMap(){
		Map<String , Object> map = new HashMap<>();
		if(null != userId){
			map.put("userId" , userId);
		}

		if(null != startTime){
			map.put("startTime" , startTime);
		}

		if(null != endTime){
			map.put("endTime" , endTime);
		}
		return map;
	}
}
