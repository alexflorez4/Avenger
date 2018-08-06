package com.shades.views;

import Entities.OrderEntity;
import com.shades.utilities.Enumerations;
import com.shades.utilities.ParseAmzOrder;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class InvoiceOrders extends AbstractXlsxView {

    public static final FastDateFormat AVE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"Invoice.xlsx\"");

        List<OrderEntity> orders = (List) map.get("orders");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Orders");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Order ID");
        header.createCell(1).setCellValue("SKU");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("Supplier");
        header.createCell(4).setCellValue("Price");
        header.createCell(5).setCellValue("Shipping Cost");
        header.createCell(6).setCellValue("Total");
        header.createCell(7).setCellValue("Market");
        header.createCell(8).setCellValue("Date");
        header.createCell(9).setCellValue("Seller");


        // Create data cells
        int rowCount = 1;
        for(OrderEntity next : orders){
            Row orderRow = sheet.createRow(rowCount++);
            orderRow.createCell(0).setCellValue(next.getOrderId());
            orderRow.createCell(1).setCellValue(next.getSku());
            orderRow.createCell(2).setCellValue(next.getQuantity());
            orderRow.createCell(3).setCellValue(Enumerations.Suppliers.getSupplierName(next.getSupplierId()));
            orderRow.createCell(4).setCellValue(next.getShadesPrice());
            orderRow.createCell(5).setCellValue(next.getShippingCost());
            orderRow.createCell(6).setCellValue(next.getTotalPriceShades());
            orderRow.createCell(7).setCellValue(Enumerations.Markets.getMarketName(next.getMarketId()));
            orderRow.createCell(8).setCellValue(AVE_FORMAT.format(next.getOrderDate()));
            orderRow.createCell(9).setCellValue(Enumerations.Sellers.getSellerName(next.getSellerId()));
        }
    }
}
