package com.k66.cxzt.service;

import com.k66.cxzt.model.Invoice;

public interface LeshuiService {
	Invoice getInvoice(String code , String number , String time , String check);
}
