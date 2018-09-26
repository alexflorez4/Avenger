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

public class InventoryUserReport extends AbstractXlsxView{
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"myInventory.xlsx\"");

        List<InventoryEntity> items = (List) map.get("items");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Inventory");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("SKU");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Suggested $");

        System.out.println("Items: " + items.size());
        try {
            // Create data cells
            int rowCount = 1;
            for (InventoryEntity next : items) {
                Row orderRow = sheet.createRow(rowCount++);
                orderRow.createCell(0).setCellValue(next.getSku());
                orderRow.createCell(1).setCellValue(next.getShadesSellingPrice());
                orderRow.createCell(2).setCellValue(next.getQuantity());
                orderRow.createCell(3).setCellValue(StringUtils.isBlank(next.getStatus()) ? StringUtils.EMPTY : next.getStatus());
                orderRow.createCell(4).setCellValue(next.getSuggestedPrice() == null ? 0 : next.getSuggestedPrice());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
