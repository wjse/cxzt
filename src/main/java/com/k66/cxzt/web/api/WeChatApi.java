package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoiceDetail;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.InvoiceService;
import com.k66.cxzt.service.UserService;
import com.k66.cxzt.service.WechatService;
import com.k66.cxzt.utils.JWTUtil;
import com.k66.cxzt.utils.WebContextUtil;
import com.k66.cxzt.web.vo.BindVO;
import com.k66.cxzt.web.vo.InvoicePackageQueryVO;
import com.k66.cxzt.web.vo.WechatInvoicePackageVO;
import com.k66.cxzt.web.vo.WechatInvoiceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wechat")
public class WeChatApi {

	@Autowired
	WechatService wechatService;

	@Autowired
	UserService userService;

	@Autowired
	InvoiceService invoiceService;

	@GetMapping(value = "/token" , params = {"code"})
	public JSONObject getToken(String code){
		String openId = wechatService.getOpenId(code);
		User user = userService.getByOpenId(openId);
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}
		String token = JWTUtil.createToken(user.getOpenId() , user.getUsername() , true);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("token" , token);
		return result;
	}

	@PostMapping(value = "/bind" , produces = "application/json")
	@Transactional
	public JSONObject bind(BindVO vo){
		User user = userService.getByMobile(vo.getMobile());
		if(null == user){
			throw new BusinessException(ErrorCode.NOT_FOUNT , "user.notFound");
		}

		if(StringUtils.isEmpty(user.getOpenId())){
			String openId = wechatService.getOpenId(vo.getCode());
			user.setOpenId(openId);
			if(StringUtils.isEmpty(user.getWechat())){
				String accessToken = wechatService.getAccessToken(openId);
				JSONObject wechatUser = wechatService.getUserInfo(accessToken , openId);
				if(null != wechatUser){
					user.setWechat(wechatUser.toJSONString());
				}
			}
			userService.updateOpenIdAndWechat(user);
		}

		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("token", JWTUtil.createToken(user.getOpenId() , user.getUsername() , true));
		return result;
	}

	@PostMapping(value = "/invoice" , produces = "application/json")
	public JSONObject scanInvoice(@RequestBody WechatInvoiceVO vo){
		User user = WebContextUtil.getUser();
		vo.setUserId(user.getId());
		Long id = invoiceService.scanInvoice(vo);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("data" , id);
		return result;
	}

	@PostMapping(value = "/invoicePackage" , produces = "application/json")
	public JSONObject savePackage(@RequestBody WechatInvoicePackageVO vo){
		User user = WebContextUtil.getUser();
		vo.setUserId(user.getId());
		invoiceService.addPackage(vo);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		return result;
	}

	@GetMapping(value = "/invoicePackage" , params = {"pageNum" , "pageSize"})
	public JSONObject queryInvoicePackage(int pageNum , int pageSize , InvoicePackageQueryVO vo){
		PageInfo<InvoicePackage> page = invoiceService.queryInvoicePackage(pageNum , pageSize , vo.toMap());
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("data" , page);
		return result;
	}

	@GetMapping(value = "/invoice/{packageId}")
	public JSONObject getInvoiceDetail(@PathVariable("packageId")Long packageId){
		List<Invoice> list = invoiceService.queryInvoiceByPackage(packageId);
		JSONObject result = new JSONObject();
		result.put("code" , 200);
		result.put("data" , list);
		return result;
	}
}
