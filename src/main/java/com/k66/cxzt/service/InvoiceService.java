package com.k66.cxzt.service;

import com.github.pagehelper.PageInfo;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.model.User;
import com.k66.cxzt.web.vo.QRInvoiceVO;

import java.util.Map;
import java.util.Set;

public interface InvoiceService {

	void scanInvoice(InvoicePackage invoicePackage , Set<QRInvoiceVO> invoiceSet);

    PageInfo<InvoicePackage> queryPackage(User user, int pageNum , int pageSize , Map<String, Object> toMap);
}
