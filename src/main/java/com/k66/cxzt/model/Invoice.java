package com.k66.cxzt.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Invoice {
	private long id;

	/**
	 * 发票号码
	 */
	private String number;

	/**
	 * 发票代码
	 */
	private String code;

	/**
	 * 开票日期
	 */
	private Date date;

	/**
	 * 发票类型
	 * 1-专票
	 * 2-普票
	 * 3-电子
	 * 4-机动车销售
	 * 5-卷式
	 * 6-电子通行费
	 * 7-二手车
	 */
	private int type;

	/**
	 *  票据类型
	 *  1-进项票
	 *  2-销项票
	 */
	private int invoiceType;

	/**
	 * 购方名称
	 */
	private String buyerName;

	/**
	 * 购方税号
	 */
	private String buyerTaxNo;

	/**
	 * 购方地址电话
	 */
	private String buyerAddrTel;

	/**
	 * 购方银行账号
	 */
	private String buyerBank;

	/**
	 * 销方名称
	 */
	private String sellerName;

	/**
	 * 销方税号
	 */
	private String sellerTaxNo;

	/**
	 * 销方地址电话
	 */
	private String sellerAddrTel;

	/**
	 * 销方银行账号
	 */
	private String sellerBank;

	/**
	 * 价税合计
	 */
	private BigDecimal amountTax;

	/**
	 * 金额合计
	 */
	private BigDecimal amount;

	/**
	 * 税额合计
	 */
	private BigDecimal taxAmount;

	private Long packageId;
	private List<InvoiceDetail> invoiceDetailList;
	private int userId;
}
