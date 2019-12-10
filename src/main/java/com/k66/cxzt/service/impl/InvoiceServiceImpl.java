package com.k66.cxzt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.mapper.InvoiceDetailMapper;
import com.k66.cxzt.mapper.InvoiceMapper;
import com.k66.cxzt.mapper.InvoicePackageMapper;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoiceDetail;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.service.InvoiceService;
import com.k66.cxzt.service.LeshuiService;
import com.k66.cxzt.utils.IdGenUtil;
import com.k66.cxzt.web.vo.WechatInvoicePackageVO;
import com.k66.cxzt.web.vo.WechatInvoiceVO;
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

	@Override
	@Transactional
	public Long scanInvoice(WechatInvoiceVO vo) {
		Invoice invoice = invoiceMapper.getCountByNumberAndCode(vo.getNumber() , vo.getCode());
		if(null != invoice && null != invoice.getPackageId()){
			throw new BusinessException(101 , "invoice.existed");
		}

		if(null == invoice){
			Invoice newInvoice = leshuiService.getInvoice(vo.getCode() , vo.getNumber() , vo.getDate() , vo.getCheck());
			if(null == newInvoice){
				throw new BusinessException(102 , "invoice.invalidate");
			}
			IdGenUtil idGenUtil = new IdGenUtil();
			newInvoice.setId(idGenUtil.nextId());
			newInvoice.setUserId(vo.getUserId());

			List<InvoiceDetail> details = newInvoice.getInvoiceDetailList().stream().map(d -> {
				d.setInvoiceId(newInvoice.getId());
				d.setId(idGenUtil.nextId());
				return d;
			}).collect(Collectors.toList());

			invoiceMapper.add(newInvoice);
			invoiceDetailMapper.batchAdd(details);
			invoice = newInvoice;
		}
		return invoice.getId();
	}

	@Override
	@Transactional
	public void addPackage(WechatInvoicePackageVO vo) {
		Set<Long> invoiceIdSet = vo.getInvoiceIdSet();
		if(null == invoiceIdSet || invoiceIdSet.isEmpty()){
			throw new BusinessException(ErrorCode.ERROR , "parameter.error");
		}

		IdGenUtil idGenUtil = new IdGenUtil();
		InvoicePackage iPackage = new InvoicePackage();
		iPackage.setCount(invoiceIdSet.size());
		iPackage.setId(idGenUtil.nextId());
		iPackage.setDate(new Date());
		iPackage.setUserId(vo.getUserId());

		invoicePackageMapper.add(iPackage);
		invoiceMapper.updatePackage(iPackage.getId() , invoiceIdSet);
	}

	@Override
	public PageInfo<InvoicePackage> queryInvoicePackage(int pageNum , int pageSize , Map<String, Object> map) {
		return PageHelper.startPage(pageNum , pageSize)
						.doSelectPageInfo(() -> invoicePackageMapper.queryForList(map));
	}

	@Override
	public List<Invoice> queryInvoiceByPackage(Long packageId) {
		Map<String , Object> map = new HashMap<>();
		map.put("packageId" , packageId);
		List<Invoice> list = invoiceMapper.queryForList(map);
		if(null == list){
			return Collections.EMPTY_LIST;
		}

		List<Long> invoiceIds = list.stream().map(i -> i.getId()).collect(Collectors.toList());
		List<InvoiceDetail> details = invoiceDetailMapper.queryByInvoiceId(invoiceIds);
		for(Invoice invoice : list){
			List<InvoiceDetail> tmp = details.stream().filter(d -> d.getInvoiceId() == invoice.getId()).collect(Collectors.toList());
			invoice.setInvoiceDetailList(tmp);
		}
		return list;
	}
}
