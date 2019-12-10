package com.k66.cxzt.service;

import com.github.pagehelper.PageInfo;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.web.vo.WechatInvoicePackageVO;
import com.k66.cxzt.web.vo.WechatInvoiceVO;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
	Long scanInvoice(WechatInvoiceVO vo);
	void addPackage(WechatInvoicePackageVO vo);

	PageInfo<InvoicePackage> queryInvoicePackage(int pageNum , int pageSize , Map<String, Object> map);
	List<Invoice> queryInvoiceByPackage(Long packageId);
}
