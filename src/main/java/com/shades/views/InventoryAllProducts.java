package com.shades.views;

import Entities.InventoryEntity;
import com.shades.utilities.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
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
        header.createCell(3).setCellValue("Est shipping");
        header.createCell(4).setCellValue("Suggested $ 15%");

        // Create data cells
        int rowCount = 1;
        for(InventoryEntity next : items){
            Row orderRow = sheet.createRow(rowCount++);
            orderRow.createCell(0).setCellValue(next.getSku());
            orderRow.createCell(1).setCellValue(next.getShadesSellingPrice());
            orderRow.createCell(2).setCellValue(next.getQuantity());
            Double shipCost = next.getShippingCost() == null ? 0 : next.getShippingCost();
            Double sugPrice = Utils.getProductRecommendedPrice(next.getShadesSellingPrice(), shipCost);
            DecimalFormat df = new DecimalFormat("#.##");
            sugPrice = Double.valueOf(df.format(sugPrice));
            orderRow.createCell(3).setCellValue(shipCost);
            orderRow.createCell(4).setCellValue(sugPrice);
        }
    }
}
