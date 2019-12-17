package com.k66.cxzt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.mapper.InvoiceDetailMapper;
import com.k66.cxzt.mapper.InvoiceMapper;
import com.k66.cxzt.mapper.InvoicePackageMapper;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoiceDetail;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.InvoiceService;
import com.k66.cxzt.service.LeshuiService;
import com.k66.cxzt.utils.IdGenUtil;
import com.k66.cxzt.utils.Pair;
import com.k66.cxzt.web.vo.QRInvoiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoicePackageMapper invoicePackageMapper;

	@Autowired
	private InvoiceMapper invoiceMapper;

	@Autowired
	private InvoiceDetailMapper invoiceDetailMapper;

	@Autowired
	private LeshuiService leshuiService;

	@Autowired
	private IdGenUtil idGenUtil;

//	private ExecutorService executorService = Executors.newFixedThreadPool(10 , new CustomizableThreadFactory("InvoiceThread-"));


	@Override
	@Transactional
	public void scanInvoice(InvoicePackage invoicePackage , Set<QRInvoiceVO> invoiceSet) {
		List<Pair<String , String>> numberCodeList = invoiceSet.stream()
				.map(i -> Pair.of(i.getNumber() , i.getCode()))
				.collect(Collectors.toList());

		List<Map<String , Object>> historyList = invoiceMapper.getCountByNumberAndCode(numberCodeList);
		List<QRInvoiceVO> qrList;
		if(!historyList.isEmpty()){
			qrList = invoiceSet.stream().filter(i -> isNotHistory(i , historyList)).collect(Collectors.toList());
		}else{
			qrList = invoiceSet.stream().collect(Collectors.toList());
		}

		if(null == qrList || qrList.isEmpty()){
			return;
		}

		invoicePackage.setId(idGenUtil.nextId());
		List<Invoice> invoiceList = new ArrayList<>();
		List<InvoiceDetail> allDetailList = new ArrayList<>();

		for(QRInvoiceVO vo : qrList){
			Invoice invoice = leshuiService.getInvoice(vo.getCode() , vo.getNumber() , vo.getDate() , vo.getCheck());
			if(null != invoice){
				invoice.setId(idGenUtil.nextId());
				invoice.setUserId(invoicePackage.getUserId());
				invoice.setPackageId(invoicePackage.getId());
				invoiceList.add(invoice);
				List<InvoiceDetail> details = invoice.getInvoiceDetailList().stream().map(d -> {
					d.setId(idGenUtil.nextId());
					d.setInvoiceId(invoice.getId());
					return d;
				}).collect(Collectors.toList());
				allDetailList.addAll(details);
			}
		}

		if(!invoiceList.isEmpty()){
			invoiceMapper.batchAdd(invoiceList);
			invoiceDetailMapper.batchAdd(allDetailList);
			invoicePackageMapper.add(invoicePackage);
		}
	}

	@Override
	public PageInfo<InvoicePackage> queryPackage(User user, int pageNum, int pageSize, Map<String, Object> map) {
		if(User.UserType.NORMAL.equals(user.getType())){
			map.put("userId" , user.getId());
		}
		return PageHelper.startPage(pageNum , pageSize).doSelectPageInfo(() -> invoicePackageMapper.queryForList(map));
	}

	@Override
	public List<InvoiceDetail> queryInvoiceDetail(Long invoiceId) {
		return invoiceDetailMapper.queryByInvoiceId(invoiceId);
	}

	private boolean isNotHistory(QRInvoiceVO invoiceVO , List<Map<String , Object>> historyList){
		Optional<Map<String , Object>> op = historyList.stream().filter(h -> invoiceVO.getNumber().equals(h.get("number")) && invoiceVO.getCode().equals(h.get("code"))).findAny();
		return !op.isPresent();
	}
}
