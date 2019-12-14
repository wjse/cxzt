package com.k66.cxzt.mapper;

import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.utils.Pair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface InvoiceMapper {

	void add(Invoice invoice);

	List<Map<String , Object>> getCountByNumberAndCode(List<Pair<String , String>> list);

	List<Invoice> queryForList(Map<String, Object> map);

	void batchAdd(List<Invoice> invoiceList);
}
