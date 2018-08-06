package com.shades.views;

import Entities.InventoryEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class InventoryAllProducts extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"inventory.xlsx\"");

        Sheet sheet = workbook.createSheet("Inventory");

        List<InventoryEntity> items = (List) map.get("items");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("SKU");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Est shipping");
        header.createCell(5).setCellValue("Amz recom. $");
        header.createCell(6).setCellValue("Ebay recom. $");

        // Create data cells
        int rowCount = 1;
        for(InventoryEntity next : items){
            Row orderRow = sheet.createRow(rowCount++);
            orderRow.createCell(0).setCellValue(next.getSku());
            orderRow.createCell(1).setCellValue(next.getShadesSellingPrice());
            orderRow.createCell(2).setCellValue(next.getQuantity());
            orderRow.createCell(3).setCellValue(StringUtils.isBlank(next.getStatus()) ? "" : next.getStatus());
            orderRow.createCell(4).setCellValue(next.getShippingCost());
            orderRow.createCell(5).setCellValue(0);
            orderRow.createCell(6).setCellValue(0);
        }
    }
}
