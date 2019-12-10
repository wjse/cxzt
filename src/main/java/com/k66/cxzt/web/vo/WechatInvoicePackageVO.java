package com.k66.cxzt.web.vo;

import lombok.Data;

import java.util.Set;

@Data
public class WechatInvoicePackageVO {
	private Integer userId;
	private Set<Long> invoiceIdSet;
}
