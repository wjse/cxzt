package com.k66.cxzt.model;

import com.sun.tools.javac.util.List;
import lombok.Data;

import java.util.Date;

@Data
public class InvoicePackage {
	private long id;
	private Date date;
	private int userId;
	private User user;
	private int count;
	private List<Invoice> invoiceList;
}
