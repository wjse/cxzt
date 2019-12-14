package com.k66.cxzt.service;

import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.web.vo.QRInvoiceVO;

import java.util.Set;

public interface InvoiceService {

	void scanInvoice(InvoicePackage invoicePackage , Set<QRInvoiceVO> invoiceSet);
}
