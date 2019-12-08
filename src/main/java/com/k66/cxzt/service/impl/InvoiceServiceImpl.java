package com.k66.cxzt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.utils.HttpSender;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceServiceImpl {

	@Value("${leshui.url}")
	private String leshuiUrl;

	@Value("${leshui.app-key}")
	private String appKey;

	@Value("${leshui.app-secret}")
	private String appSecret;

	private static String leshuiToken;
	private static AtomicInteger threadCount = new AtomicInteger();

	private ThreadLocal<Integer> requestCount = new ThreadLocal<>();

	//TODO
	private void getInvoice(String code , String number , String time , String check){
		if(!checkInvoice(code)){
			throw new BusinessException(ErrorCode.FORBIDDEN , "invoice.invalidate");
		}

		String url = String.format("%s/api/invoiceInfoForCom" , leshuiUrl);
		PostMethod method = new PostMethod(url);
		method.setRequestBody(new NameValuePair[]{
						new NameValuePair("invoiceCode" , code),
						new NameValuePair("invoiceNumber" , number),
						new NameValuePair("billTime" , time),
						new NameValuePair("checkCode" , check),
						new NameValuePair("token" , leshuiToken),
		});

		JSONObject result = HttpSender.send(method);
		if(null != result && "00".equals(result.getString("RtnCode"))){
			if("1000".equals(result.getString("resultCode"))){
				//TODO
			}
		}
	}

	private boolean checkInvoice(String invoiceCode){
		String url = String.format("%s/api/getInvoiceModel" , leshuiUrl);
		PostMethod method = new PostMethod(url);
		method.setRequestBody(new NameValuePair[]{
						new NameValuePair("invoiceCode" , invoiceCode),
						new NameValuePair("token" , leshuiToken == null ? getLeshuiToken() : leshuiToken)
		});
		JSONObject result = HttpSender.send(method);
		if(null != result && "00".equals(result.getString("RtnCode"))){
			if(isLeshuiTokenError(result)){
				flushLeshuiToken();
				return checkInvoice(invoiceCode);
			}

			if("00".equals("resultCode")){
				return true;
			}
		}

		return false;
	}

	private String getLeshuiToken(){
		String url = String.format("%s/getToken?appKey=%s&appSecret=%s" , leshuiUrl , appKey , appSecret);
		GetMethod method = new GetMethod(url);
		JSONObject result = HttpSender.send(method);
		if(null != result && result.containsKey("token")){
			return result.getString("token");
		}

		throw new BusinessException(ErrorCode.ERROR , "Get leshui token error");
	}

	private void flushLeshuiToken(){
		Integer count = requestCount.get();
		if(null != count && count.intValue() >= 3){
			throw new BusinessException(ErrorCode.ERROR , "Get leshui token error . Maybe have no money");
		}
		leshuiToken = getLeshuiToken();
		requestCount.set(threadCount.getAndIncrement());
	}

	private boolean isLeshuiTokenError(JSONObject json){
		return json.containsKey("error") && "token error".equals(json.getString("error"));
	}

	public static void main(String[] args) {
		ThreadLocal<Integer> t = new ThreadLocal<>();
		AtomicInteger atomicInteger = new AtomicInteger();
		System.out.println(t.get());
		t.set(atomicInteger.getAndIncrement());
		System.out.println(t.get());
		t.set(atomicInteger.getAndIncrement());
		System.out.println(t.get());
	}
}
