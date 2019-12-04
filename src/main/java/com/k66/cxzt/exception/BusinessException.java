package com.k66.cxzt.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{
	int code;
	String message;

	public BusinessException(int code , String message){
		super(message);
		this.code = code;
		this.message = message;
	}

	public BusinessException(int code , String message , Throwable t){
		super(message , t);
		this.code = code;
		this.message = message;
	}
}
