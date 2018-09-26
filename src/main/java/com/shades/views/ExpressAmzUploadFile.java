package com.shades.views;

import Entities.InventoryEntity;
import com.shades.model.AsinItem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExpressAmzUploadFile extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"products.xlsx\"");

        Sheet sheet = workbook.createSheet("Products");

        List<AsinItem> items = (List) map.get("items");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("sku");
        header.createCell(1).setCellValue("'product-id");
        header.createCell(2).setCellValue("'product-id-type");
        header.createCell(3).setCellValue("'price");
        header.createCell(4).setCellValue("'minimum-seller-allowed-price");
        header.createCell(5).setCellValue("'maximum-seller-allowed-price");
        header.createCell(6).setCellValue("'item-condition");
        header.createCell(7).setCellValue("'quantity");
        header.createCell(8).setCellValue("'add-delete");
        header.createCell(9).setCellValue("'will-ship-internationally");
        header.createCell(10).setCellValue("'expedited-shipping");
        header.createCell(11).setCellValue("'standard-plus");
        header.createCell(12).setCellValue("'item-note");

        // Create data cells
        int rowCount = 1;
        for(AsinItem next : items){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(next.getSku());
            row.createCell(1).setCellValue(next.getProductIdAsin());
            row.createCell(2).setCellValue(next.getProductIdType());
            row.createCell(3).setCellValue(next.getPrice());
            row.createCell(4).setCellValue(next.getMinimumSellerAllowedPrice());
            row.createCell(5).setCellValue(next.getMaximumSellerAllowedPrice());
            row.createCell(6).setCellValue(next.getItemCondition());
            row.createCell(7).setCellValue(next.getQuantity());
            row.createCell(8).setCellValue("a");
            row.createCell(9).setCellValue("n");
            row.createCell(10).setCellValue("");
            row.createCell(11).setCellValue("");
            row.createCell(12).setCellValue("");
        }
    }
}
