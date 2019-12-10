package com.k66.cxzt.mapper;

import com.k66.cxzt.model.InvoicePackage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InvoicePackageMapper {
	void add(InvoicePackage iPackage);
	List<InvoicePackage> queryForList(Map<String, Object> map);
}
