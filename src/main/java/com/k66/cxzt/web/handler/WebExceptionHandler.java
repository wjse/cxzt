package com.k66.cxzt.web.handler;

import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class WebExceptionHandler {

	@Autowired
	MessageI18NProcessor messageI18NProcessor;

	@ExceptionHandler(BusinessException.class)
	public JSONObject handle(BusinessException e){
		log.error(e.getMessage() , e);
		JSONObject json = new JSONObject();
		json.put("code" , e.getCode());
		json.put("message" , messageI18NProcessor.getMessage(e.getMessage()));
		return json;
	}

	@ExceptionHandler(Exception.class)
	public JSONObject handle(Exception e){
		log.error(e.getMessage() , e);
		JSONObject json = new JSONObject();
		json.put("code" , ErrorCode.ERROR);
		json.put("message" , messageI18NProcessor.getMessage("error"));
		return json;
	}
}
