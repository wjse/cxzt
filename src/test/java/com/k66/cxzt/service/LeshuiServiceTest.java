package com.k66.cxzt.service;

import com.k66.cxzt.Application;
import com.k66.cxzt.model.Invoice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class LeshuiServiceTest {

    @Autowired
    LeshuiService service;

    @Test
    public void test_get_invoice(){
        Invoice invoice = service.getInvoice("031001800411" , "69840862" , "2019-11-23" , "04829494127266254468");
        System.out.println(invoice);
    }

    public static void main(String[] args) {
        String str = "04829494127266254468";
        System.out.println(str.substring(str.length() - 6));
    }
}
