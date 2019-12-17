package com.k66.cxzt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoiceDetail;
import com.k66.cxzt.service.LeshuiService;
import com.k66.cxzt.utils.HttpSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class LeshuiServiceImpl implements LeshuiService {
	@Value("${leshui.url}")
	private String leshuiUrl;

	@Value("${leshui.app-key}")
	private String appKey;

	@Value("${leshui.app-secret}")
	private String appSecret;

	private static String leshuiToken;
	private static AtomicInteger threadCount = new AtomicInteger();
	private ThreadLocal<Integer> requestCount = new ThreadLocal<>();

	@Override
	public Invoice getInvoice(String code , String number , String time , String check){
		if(!checkInvoice(code)){
			throw new BusinessException(ErrorCode.FORBIDDEN , "invoice.invalidate");
		}

		String url = String.format("%s/api/invoiceInfoForCom" , leshuiUrl);
		PostMethod method = new PostMethod(url);
		JSONObject requestJson = new JSONObject();
		requestJson.put("invoiceCode" , code);
		requestJson.put("invoiceNumber" , number);
		requestJson.put("billTime" , time);
		if(null != check){
			requestJson.put("checkCode" , check.substring(check.length() - 6));
		}
		requestJson.put("token" , leshuiToken);
		try {
			method.setRequestEntity(new StringRequestEntity(requestJson.toJSONString() , "application/json" , "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(ErrorCode.ERROR , "system.error");
		}
		JSONObject result = HttpSender.send(method);
		if(null != result && "00".equals(result.getString("RtnCode"))){
			if("1000".equals(result.getString("resultCode"))){
				JSONObject invoiceResult = result.getJSONObject("invoiceResult");
				Invoice invoice = new Invoice();
				invoice.setInvoiceTypeName(invoiceResult.getString("invoiceTypeName"));
				invoice.setType(getInvoiceType(invoiceResult.getString("invoiceTypeCode")));
				try {
					invoice.setDate(DateUtils.parseDate(invoiceResult.getString("billingTime") , "yyyy-MM-dd"));
				} catch (ParseException e) {
					throw new BusinessException(ErrorCode.ERROR , "system.error");
				}
				invoice.setBuyerName(invoiceResult.getString("purchaserName"));
				invoice.setBuyerTaxNo(invoiceResult.getString("taxpayerNumber"));
				invoice.setBuyerBank(invoiceResult.getString("taxpayerBankAccount"));
				invoice.setBuyerAddrTel(invoiceResult.getString("taxpayerAddressOrId"));

				invoice.setSellerName(invoiceResult.getString("salesName"));
				invoice.setSellerTaxNo(invoiceResult.getString("salesTaxpayerNum"));
				invoice.setSellerBank(invoiceResult.getString("salesTaxpayerBankAccount"));
				invoice.setSellerAddrTel(invoiceResult.getString("salesTaxpayerAddress"));
				invoice.setAmountTax(invoiceResult.getBigDecimal("totalTaxSum"));
				invoice.setTaxAmount(invoiceResult.getBigDecimal("totalTaxNum"));
				invoice.setAmount(invoiceResult.getBigDecimal("totalAmount"));
				invoice.setCode(code);
				invoice.setNumber(number);
				invoice.setInvoiceDetailList(getInvoiceDetails(invoiceResult.getJSONArray("invoiceDetailData")));
				return invoice;
			}
		}
		log.error("Get invoice from leshui error : the result is {}" , result);
		return null;
	}

	private List<InvoiceDetail> getInvoiceDetails(JSONArray jArray){
		List<InvoiceDetail> list = new ArrayList<>();
		for(int i = 0 ; i < jArray.size() ; i++){
			JSONObject json = jArray.getJSONObject(i);
			InvoiceDetail detail = new InvoiceDetail();
			detail.setRemark(json.getString("goodserviceName"));
			detail.setSpecs(json.getString("model"));
			detail.setUnit(json.getString("util"));
			if(" ".equals(json.getString("price"))){
				detail.setPrice(BigDecimal.ZERO);
			}else{
				detail.setPrice(json.getBigDecimal("price"));
			}
			if(" ".equals(json.getString("tax"))){
				detail.setTaxAmount(BigDecimal.ZERO);
			}else{
				detail.setTaxAmount(json.getBigDecimal("tax"));
			}
			detail.setTaxRate(json.getString("taxRate"));
			if(" ".equals(json.getString("number"))){
				detail.setCount(BigDecimal.ZERO);
			}else{
				detail.setCount(json.getBigDecimal("number"));
			}
			if(" ".equals(json.getString("sum"))){
				detail.setAmount(BigDecimal.ZERO);
			}else{
				detail.setAmount(json.getBigDecimal("sum"));
			}
			list.add(detail);
		}
		return list;
	}

	private int getInvoiceType(String type){
		if("01".equals(type)){
			return 1;
		}else if("03".equals(type)){
			return 4;
		}else if("04".equals(type)){
			return 2;
		}else if("10".equals(type)){
			return 3;
		}else if("11".equals(type)){
			return 5;
		}else if("14".equals(type)){
			return 6;
		}else if("15".equals(type)){
			return 7;
		}else {
			return 0;
		}
	}

	private boolean checkInvoice(String invoiceCode){
		String url = String.format("%s/api/getInvoiceModel" , leshuiUrl);
		PostMethod method = new PostMethod(url);
		JSONObject requestJson = new JSONObject();
		requestJson.put("invoiceCode" , invoiceCode);
		requestJson.put("token" , leshuiToken == null ? getLeshuiToken() : leshuiToken);
		try {
			method.setRequestEntity(new StringRequestEntity(requestJson.toJSONString() , "application/json" , "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(ErrorCode.ERROR , "system.error");
		}
		JSONObject result = HttpSender.send(method);
		if(null != result && "00".equals(result.getString("RtnCode"))){
			if(isLeshuiTokenError(result)){
				flushLeshuiToken();
				return checkInvoice(invoiceCode);
			}

			if("00".equals(result.getString("resultCode"))){
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
			leshuiToken = result.getString("token");
			return leshuiToken;
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
}
