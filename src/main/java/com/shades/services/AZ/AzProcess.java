package com.shades.services.az;

import Entities.InventoryEntity;
import com.shades.dao.InventoryDao;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Set<InventoryEntity> inventoryList = new HashSet<>();
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
                        Double cost = currentCell.getNumericCellValue();
                        azInventory.setSupplierPrice(cost);
                        //Float shadesCost = cost + new Float(cost * 0.15);
                        azInventory.setShadesSellingPrice(Utils.shadesPrices(cost));
                        break;
                    case 2: //Quantity
                        azInventory.setQuantity(new Double(currentCell.getNumericCellValue()).intValue());
                        break;
                    case 3://weight
                        Double weight =  currentCell.getNumericCellValue();
                        azInventory.setWeight(weight);

                        if(weight <= 1){
                            azInventory.setShippingCost(7.50);
                        }else if(weight <= 2){
                            azInventory.setShippingCost(10.80);
                        }else if(weight <= 3){
                            azInventory.setShippingCost(15.00);
                        }else if(weight <= 4){
                            azInventory.setShippingCost(17.00);
                        }else if(weight <= 6){
                            azInventory.setShippingCost(18.00);
                        }else if(weight <= 7){
                            azInventory.setShippingCost(20.00);
                        }else {
                            azInventory.setShippingCost(99.00);
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

        inventoryDao.updateInventory(inventoryList);

        return true;
    }
}
