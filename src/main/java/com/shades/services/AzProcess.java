package com.shades.services;

import Entities.InventoryEntity;
import Entities.SellerEntity;
import com.shades.dao.InventoryDao;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


@Transactional
@Service
public class AzProcess {

    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    public boolean updateInventory(File inventoryFile) {


        logger.info("Updating inventory");

        Set<InventoryEntity> inventoryList = new HashSet<InventoryEntity>();
        FileInputStream fileInputStream = null;
        Workbook workbook = null;
        try {
            fileInputStream = new FileInputStream(inventoryFile);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        iterator.next(); //skipping head row.

        while ((iterator.hasNext())){

            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            InventoryEntity azInventory = new InventoryEntity();

            while (cellIterator.hasNext()){

                Cell currentCell = cellIterator.next();
                int cell = currentCell.getColumnIndex();

                switch (cell){
                    case 0: //sku
                        azInventory.setSku(currentCell.getStringCellValue());
                        break;
                    case 1: //Cost
                        Float cost = new Float(currentCell.getNumericCellValue());
                        azInventory.setSupplierPrice(cost);

                        Float shadesCost = cost + new Float(cost * 0.15);
                        azInventory.setShadesSellingPrice(shadesCost);
                        break;
                    case 2: //Quantity
                        azInventory.setQuantity(new Double(currentCell.getNumericCellValue()).intValue());
                        break;
                    case 3://weight
                        Float weight = new Float(currentCell.getNumericCellValue());
                        azInventory.setWeight(weight);

                        if(weight <= 1){
                            azInventory.setShippingCost(new Float(7.50));
                        }else if(weight <= 2){
                            azInventory.setShippingCost(new Float(10.80));
                        }else if(weight <= 3){
                            azInventory.setShippingCost(new Float(15.00));
                        }else if(weight <= 4){
                            azInventory.setShippingCost(new Float(17.00));
                        }else if(weight <= 6){
                            azInventory.setShippingCost(new Float(18.00));
                        }else if(weight <= 7){
                            azInventory.setShippingCost(new Float(20.00));
                        }else {
                            azInventory.setShippingCost(new Float(99.00));
                        }

                        break;
                    default:
                        break;
                }
                azInventory.setSupplierId(500);
                azInventory.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                inventoryList.add(azInventory);
            }
        }

        inventoryDao.insertAzInventory(inventoryList);

        return true;
    }
}
