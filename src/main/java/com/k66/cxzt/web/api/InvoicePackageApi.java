package com.k66.cxzt.web.api;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.k66.cxzt.exception.BusinessException;
import com.k66.cxzt.exception.ErrorCode;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.model.User;
import com.k66.cxzt.service.InvoiceService;
import com.k66.cxzt.utils.WebContextUtil;
import com.k66.cxzt.web.vo.InvoicePackageQueryVO;
import com.k66.cxzt.web.vo.InvoicePackageVO;
import com.k66.cxzt.web.vo.QRInvoiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/invoice/package")
public class InvoicePackageApi {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping(produces = "application/json")
    public JSONObject add(@RequestBody InvoicePackageVO vo){
        if(null == vo.getInvoiceArray() || vo.getInvoiceArray().isEmpty()){
            throw new BusinessException(ErrorCode.FORBIDDEN , "parameter.error");
        }

        User user = WebContextUtil.getUser();
        InvoicePackage invoicePackage = new InvoicePackage();
        invoicePackage.setUserId(user.getId());
        invoicePackage.setDate(new Date());
        invoicePackage.setCount(vo.getCount());
        invoicePackage.setAmount(BigDecimal.valueOf(Double.valueOf(vo.getAmount())));
        Set<QRInvoiceVO> invoiceSet = vo.getInvoiceArray();
        invoiceService.scanInvoice(invoicePackage , invoiceSet);

        JSONObject result = new JSONObject();
        result.put("code" , 200);
        result.put("message" , "success");
        return result;
    }

    @GetMapping(params = {"pageNum" , "pageSize"})
    public JSONObject query(int pageNum , int pageSize , InvoicePackageQueryVO vo){
        User user = WebContextUtil.getUser();
        PageInfo<InvoicePackage> page = invoiceService.queryPackage(user , pageNum , pageSize , vo.toMap());
        JSONObject result = new JSONObject();
        result.put("code" , 200);
        result.put("data" , page);
        return result;
    }
}
