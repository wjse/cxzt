package com.k66.cxzt.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Data
public class InvoicePackage {
	private long id;
	private Date date;
	private int userId;
	private User user;
	private int count;
	private BigDecimal amount;
	private Set<Invoice> invoiceSet = new TreeSet<>();
}
