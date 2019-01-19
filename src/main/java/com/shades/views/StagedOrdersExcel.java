package com.shades.views;


import Entities.OrderEntity;
import com.shades.utilities.Enumerations;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class StagedOrdersExcel extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"staged_orders.xlsx\"");

        List<OrderEntity> orders = (List) map.get("orders");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Orders");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Order No");
        header.createCell(1).setCellValue("SKU");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("First Name");
        header.createCell(4).setCellValue("Last Name");
        header.createCell(5).setCellValue("Street");
        header.createCell(6).setCellValue("Street 2");
        header.createCell(7).setCellValue("City");
        header.createCell(8).setCellValue("State");
        header.createCell(9).setCellValue("Zip");
        header.createCell(10).setCellValue("Country");
        header.createCell(11).setCellValue("Supplier");
        header.createCell(12).setCellValue("Observations");
        header.createCell(13).setCellValue("Supplier Order No");
        header.createCell(14).setCellValue("Tracking No");
        header.createCell(15).setCellValue("Shipping Cost");
        header.createCell(16).setCellValue("Supplier Price");
        header.createCell(17).setCellValue("Seller");


        // Create data cells
        int rowCount = 1;
        for(OrderEntity next : orders){
            Row orderRow = sheet.createRow(rowCount++);
            orderRow.createCell(0).setCellValue(next.getOrderId());
            orderRow.createCell(1).setCellValue(next.getSku());
            orderRow.createCell(2).setCellValue(next.getQuantity());

            String name = next.getBuyerName().trim();
            int index = StringUtils.lastIndexOfAny(name, " ");
            String first = StringUtils.substring(name, 0, index);
            String last = StringUtils.substring(name, index, name.length());

            orderRow.createCell(3).setCellValue(first);
            orderRow.createCell(4).setCellValue(last);
            orderRow.createCell(5).setCellValue(next.getStreet());
            orderRow.createCell(6).setCellValue(StringUtils.isBlank(next.getStreet2()) ? "" : next.getStreet2());
            orderRow.createCell(7).setCellValue(next.getCity());
            orderRow.createCell(8).setCellValue(next.getState());
            orderRow.createCell(9).setCellValue(next.getZipCode());
            orderRow.createCell(10).setCellValue(next.getCountry());
            orderRow.createCell(11).setCellValue(Enumerations.Suppliers.getSupplierName(next.getSupplierId()));
            orderRow.createCell(12).setCellValue(StringUtils.isBlank(next.getObservations()) ? "" : next.getObservations());
            orderRow.createCell(17).setCellValue(Enumerations.Sellers.getSellerName(next.getSellerId()));
        }
    }
}
