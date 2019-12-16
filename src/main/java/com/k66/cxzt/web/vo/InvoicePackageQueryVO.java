package com.k66.cxzt.web.vo;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class InvoicePackageQueryVO {
	Integer userId;
	Long startTime;
	Long endTime;

	public Map<String , Object> toMap(){
		Map<String , Object> map = new HashMap<>();
		if(null != userId){
			map.put("userId" , userId);
		}

		if(null != startTime){
			map.put("startTime" , new Date(startTime));
		}

		if(null != endTime){
			map.put("endTime" , new Date(endTime));
		}
		return map;
	}
}
