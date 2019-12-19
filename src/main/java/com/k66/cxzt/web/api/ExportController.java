package com.k66.cxzt.web.api;

import com.k66.cxzt.mapper.InvoiceDetailMapper;
import com.k66.cxzt.mapper.InvoicePackageMapper;
import com.k66.cxzt.model.Invoice;
import com.k66.cxzt.model.InvoiceDetail;
import com.k66.cxzt.model.InvoicePackage;
import com.k66.cxzt.model.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    InvoicePackageMapper invoicePackageMapper;

    @Autowired
    InvoiceDetailMapper invoiceDetailMapper;

    @GetMapping
    public void export(Long startTime , Long endTime , HttpServletResponse response) throws IOException {
        Map<String , Object> map = new HashMap<>();
        String fileName = "导出发票";
        if(null != startTime){
            Date date = new Date(startTime);
            fileName = String.format("%s-%s" , fileName , DateFormatUtils.format(date , "yyyy-MM-dd"));
            map.put("startTime" , date);
        }

        if(null != endTime){
            Date date = new Date(endTime);
            fileName = String.format("%s-%s" , fileName , DateFormatUtils.format(date , "yyyy-MM-dd"));
            map.put("endTime" , new Date(endTime));
        }
        fileName = String.format("%s.xls" , fileName);

        List<InvoicePackage> list = invoicePackageMapper.queryForList(map);
        HSSFWorkbook workbook = createExcel(list);
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes() , "ISO-8859-1"));
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    private HSSFWorkbook createExcel(List<InvoicePackage> list) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.autoSizeColumn(1 , true);

        Row titleRow = sheet.createRow(0);
        setTitleRow(titleRow , workbook , sheet);
        setData(list , workbook , sheet , titleRow);

        return workbook;
    }

    private void setData(List<InvoicePackage> list , HSSFWorkbook workbook , HSSFSheet sheet , Row titleRow){
        if(null == list || list.isEmpty()){
            return;
        }
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        int index = 1;
        for(int i = 0 ; i < list.size(); i++){
            InvoicePackage invoicePackage = list.get(i);
            User user = invoicePackage.getUser();
            Set<Invoice> invoiceSet = invoicePackage.getInvoiceSet();

            if(null == invoiceSet || invoiceSet.isEmpty()){
                continue;
            }

            for(Invoice invoice : invoiceSet){
                Row row = sheet.createRow(index);

                createCell(0 , invoice.getNumber() , cellStyle , sheet , row , titleRow , true);
                createCell(1 , invoice.getCode() , cellStyle , sheet , row , titleRow , true);
                createCell(2 , DateFormatUtils.format(invoice.getDate() , "yyyy-MM-dd") , cellStyle , sheet , row , titleRow , true);
                createCell(3 , invoiceType(invoice.getType()), cellStyle , sheet , row , titleRow , true);
                createCell(4 , invoice.getBuyerName(), cellStyle , sheet , row , titleRow , true);
                createCell(5 , invoice.getBuyerTaxNo(), cellStyle , sheet , row , titleRow , true);
                createCell(6 , invoice.getSellerName(), cellStyle , sheet , row , titleRow , true);
                createCell(7 , invoice.getSellerTaxNo(), cellStyle , sheet , row , titleRow , true);
                createCell(8 , invoice.getAmount(), cellStyle , sheet , row , titleRow , true);
                createCell(9 , invoice.getTaxAmount(), cellStyle , sheet , row , titleRow , true);
                createCell(10 , invoice.getAmountTax(), cellStyle , sheet , row , titleRow , true);
                createCell(11 , user.getNickName(), cellStyle , sheet , row , titleRow , true);
                createCell(12 , DateFormatUtils.format(invoicePackage.getDate() , "yyyy-MM-dd HH:mm:ss"), cellStyle , sheet , row , titleRow , true);

                index += 1;
                List<InvoiceDetail> details = invoiceDetailMapper.queryByInvoiceId(invoice.getId());
                int detailsCount = setDetails(details , workbook , sheet , index);
                index += detailsCount;
            }
        }
    }

    private int setDetails(List<InvoiceDetail> details , HSSFWorkbook workbook, HSSFSheet sheet , int startIndex){
        if(null == details || details.isEmpty()){
            return 0;
        }

        Row titleRow = sheet.createRow(startIndex);
        titleRow.createCell(0);
        titleRow.createCell(1);
        titleRow.createCell(3);
        titleRow.createCell(4);

        CellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeight((short) 250);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        createCell(5 , "货物或应税劳务、服务名称" , cellStyle , sheet , titleRow , null , true);
        createCell(6 , "规格型号" , cellStyle , sheet , titleRow , null , true);
        createCell(7 , "单位" , cellStyle , sheet , titleRow , null , true);
        createCell(8 , "数量" , cellStyle , sheet , titleRow , null , true);
        createCell(9 , "单价" , cellStyle , sheet , titleRow , null , true);
        createCell(10 , "金额" , cellStyle , sheet , titleRow , null , true);
        createCell(11 , "税率" , cellStyle , sheet , titleRow , null , true);
        createCell(12 , "税额" , cellStyle , sheet , titleRow , null , true);

        CellStyle detailStyle = workbook.createCellStyle();
        HSSFFont detailFont = workbook.createFont();
        detailFont.setFontHeight((short) 250);
        detailStyle.setFont(detailFont);
        detailStyle.setBorderLeft(BorderStyle.THIN);
        detailStyle.setBorderRight(BorderStyle.THIN);
        detailStyle.setBorderBottom(BorderStyle.THIN);
        detailStyle.setBorderTop(BorderStyle.THIN);
        detailStyle.setWrapText(true);

        int rowIndex = startIndex + 1;

        for(InvoiceDetail detail : details){
            Row row = sheet.createRow(rowIndex);
            row.createCell(0);
            row.createCell(1);
            row.createCell(3);
            row.createCell(4);

            createCell(5 , detail.getRemark() , detailStyle , sheet , row , titleRow , false);
            createCell(6 , detail.getSpecs() , detailStyle , sheet , row , titleRow , false);
            createCell(7 , detail.getUnit() , detailStyle , sheet , row , titleRow , false);
            createCell(8 , detail.getCount() , detailStyle , sheet , row , titleRow , false);
            createCell(9 , detail.getPrice() , detailStyle , sheet , row , titleRow , false);
            createCell(10 , detail.getAmount() , detailStyle , sheet , row , titleRow , false);
            createCell(11 , detail.getTaxRate() , detailStyle , sheet , row , titleRow , false);
            createCell(12 , detail.getTaxAmount() , detailStyle , sheet , row , titleRow , false);

            rowIndex++;
        }
        return details.size() + 1;
    }

    private void createCell(int index , Object value , CellStyle style , HSSFSheet sheet , Row row , Row titleRow , boolean setWidth){
        Cell cell = row.createCell(index);
        if(null == value){
            value = "";
        }else if(value instanceof String){
            cell.setCellValue(value.toString());
        }else if(value instanceof BigDecimal){
            cell.setCellValue(((BigDecimal)value).doubleValue());
        }
        cell.setCellStyle(style);

        if(!setWidth){
            return;
        }
        int cellLength = value.toString().getBytes().length;
        if(null == titleRow){
            sheet.setColumnWidth(index , cellLength * 256);
        }else{
            Cell titleCell = titleRow.getCell(index);
            int titleLength = titleCell.getStringCellValue().getBytes().length;

            if(titleLength > cellLength){
                sheet.setColumnWidth(index , titleLength * 256);
            }else{
                sheet.setColumnWidth(index , cellLength * 256);
            }
        }
    }

    static final String[] TITLE_ARRAY = new String[]{
            "发票号码","发票代码","开票日期","发票类型",
            "购买方名称","购买方纳税人识别号",
            "销售方名称","销售方纳税人识别号",
            "金额合计","税额合计","价税合计","提交人","提交日期"
    };

    private void setTitleRow(Row row , HSSFWorkbook workbook , HSSFSheet sheet){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short)250);
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        for(int i = 0 ; i < TITLE_ARRAY.length ; i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(TITLE_ARRAY[i]);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i , TITLE_ARRAY[i].getBytes().length * 256);
        }
    }

    private String invoiceType(int type){
        if(1 == type){
            return "增值税专用发票";
        }else if(2 == type){
            return "增值税普通发票";
        }else if(3 == type){
            return "增值税电子普通发票";
        }else if(4 == type){
            return "机动车销售统一发票";
        }else if(5 == type){
            return "卷式普通发票";
        }else if(6 == type){
            return "电子普通发票[通行费]";
        }else if(7 == type){
            return "二手车统一发票";
        }
        return "未知类型";
    }
}
