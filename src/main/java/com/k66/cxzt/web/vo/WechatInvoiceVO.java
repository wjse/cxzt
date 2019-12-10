package com.k66.cxzt.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WechatInvoiceVO {
	private Integer userId;
	private String code;
	private String number;
	private BigDecimal amount;
	private String date;
	private String check;
}
