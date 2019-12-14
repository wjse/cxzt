package com.k66.cxzt.web.vo;

import lombok.Data;

import java.util.Set;
import java.util.TreeSet;

@Data
public class InvoicePackageVO {
    private int userId;
    private int count;
    private String amount;
    private Set<QRInvoiceVO> invoiceArray = new TreeSet<>();
}
