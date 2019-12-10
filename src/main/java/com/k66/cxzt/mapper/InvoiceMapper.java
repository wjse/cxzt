package com.k66.cxzt.mapper;

import com.k66.cxzt.model.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface InvoiceMapper {
	void add(Invoice invoice);
	Invoice getCountByNumberAndCode(@Param("number") String number, @Param("code") String code);

	void updatePackage(@Param("packageId") long id, @Param("ids") Set<Long> invoiceIdSet);

	List<Invoice> queryForList(Map<String, Object> map);
}
