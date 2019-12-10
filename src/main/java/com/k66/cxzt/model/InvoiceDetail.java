package com.k66.cxzt.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceDetail {
	private long id;

	/**
	 * 摘要
	 */
	private String remark;

	/**
	 * 规格型号
	 */
	private String specs;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 数量
	 */
	private BigDecimal count;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 税率
	 */
	private BigDecimal taxRate;

	/**
	 * 税额
	 */
	private BigDecimal taxAmount;

	private long invoiceId;
}
