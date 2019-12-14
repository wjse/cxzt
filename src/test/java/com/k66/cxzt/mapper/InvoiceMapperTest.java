package com.k66.cxzt.mapper;

import com.k66.cxzt.Application;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.utils.IdGenUtil;
import com.k66.cxzt.utils.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class InvoiceMapperTest {

    @Autowired
    IdGenUtil idGenUtil;

    @Autowired
    InvoiceMapper mapper;

    @Test
    public void test_count_0(){
        List<Map<String , Object>> list = mapper.getCountByNumberAndCode(Arrays.asList(Pair.of("1" , "3") , Pair.of("2" , "4")));
        assert list.isEmpty();
    }

    @Test
    public void test_count_1(){
        Invoice invoice = new Invoice();
        invoice.setId(idGenUtil.nextId());
        invoice.setNumber("1");
        invoice.setCode("3");
        invoice.setDate(new Date());
        invoice.setType(1);
        invoice.setInvoiceType(1);
        invoice.setBuyerName("test");
        invoice.setBuyerAddrTel("test");
        invoice.setBuyerBank("test");
        invoice.setBuyerTaxNo("test");
        invoice.setSellerName("test");
        invoice.setSellerAddrTel("test");
        invoice.setSellerBank("test");
        invoice.setSellerTaxNo("test");
        mapper.add(invoice);

        invoice.setId(idGenUtil.nextId());
        invoice.setNumber("2");
        invoice.setCode("4");
        invoice.setDate(new Date());
        invoice.setType(1);
        invoice.setInvoiceType(1);
        invoice.setBuyerName("test");
        invoice.setBuyerAddrTel("test");
        invoice.setBuyerBank("test");
        invoice.setBuyerTaxNo("test");
        invoice.setSellerName("test");
        invoice.setSellerAddrTel("test");
        invoice.setSellerBank("test");
        invoice.setSellerTaxNo("test");
        mapper.add(invoice);

        List<Map<String , Object>> list = mapper.getCountByNumberAndCode(Arrays.asList(Pair.of("1" , "3") , Pair.of("2" , "4")));
        assert list.size() == 2;
        assert list.get(0).get("number").equals("1") && Integer.parseInt(list.get(0).get("count").toString()) == 1;
    }
}
