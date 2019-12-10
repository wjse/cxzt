package com.k66.cxzt.mapper;

import com.k66.cxzt.model.InvoiceDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvoiceDetailMapper {
	void batchAdd(List<InvoiceDetail> list);

	List<InvoiceDetail> queryByInvoiceId(List<Long> invoiceIds);
}
