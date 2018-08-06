package com.shades.services.misc;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.utilities.ParseAmzOrder;
import com.shades.utilities.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AppServices {

    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    public List<String> allProductsSet(){
        return inventoryDao.getAllSKUs();
    }

    public boolean processNewSingleOrder(OrderEntity order) throws ShadesException{

        System.out.println(order);
        if(order.getOrderId() == 0){
            int nextOrderId = inventoryDao.getNextOrderId();
            order.setOrderId(nextOrderId);
            order.setSellerId(getSellerId());
        }

        inventoryDao.placeNewOrder(order);
        logger.info("New Order: " + order);
        return false;
    }

    private int getSellerId() throws ShadesException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return inventoryDao.getSellerId(userName);
    }

    //TODO: TRY TO FIX THIS METHOD. NOTE THAT THE FRAG API MIGHT NOT BE STABLE
    private void processProductDetails(OrderEntity order, InventoryEntity product) throws ShadesException {

        /*if(product.getSupplierId() == FRAGX){

            Product productInfo = null;
            //Getting latest product info:
            try {
                productInfo = fragxService.getProductDetails(product);
                if(!productInfo.isInstock()){
                    throw new ShadesException("Product " + product.getSku() + " is out of stock in Fragrance X.");
                }

                Double shadesPrice = Utils.shadesPrices(productInfo.getWholesalePriceUSD());
                Double shippingCost = Utils.fragXShippingCost(order.getQuantity());

                order.setSupplierPrice(productInfo.getWholesalePriceUSD());
                order.setShadesPrice(shadesPrice);
                order.setShippingCost(shippingCost);
                order.setTotalPriceShades(shadesPrice + shippingCost);

            }catch (Exception e){ //TODO: NOTIFY API FAILURE
                logger.info("Fragrance X API is not working.");
                order.setSupplierPrice(product.getSupplierPrice());
                order.setShadesPrice(product.getShadesSellingPrice());
                order.setShippingCost(product.getShippingCost());
            }

        }else*/{
            order.setSupplierPrice(product.getSupplierPrice());
            order.setShadesPrice(product.getShadesSellingPrice());
            order.setShippingCost(product.getShippingCost());
        }

    }

    public Set<OrderEntity> processExpressOrder(File amzOrders) throws ShadesException {

        int sellerId = getSellerId();
        int orderId = inventoryDao.getNextOrderId();

        ParseAmzOrder parseFile = new ParseAmzOrder();
        List<OrderEntity> orderList = parseFile.parse(amzOrders, sellerId, orderId);
        Set<OrderEntity> failingOrders = new HashSet<>();

        for(OrderEntity nextOrder : orderList){
            try {
                inventoryDao.placeNewOrder(nextOrder);
            }catch (ShadesException e){
                failingOrders.add(nextOrder);
            }
        }
        return failingOrders;
    }

    public List<OrderEntity> getSellerPendingOrders() throws ShadesException {
        List<OrderEntity> sellerOrders = inventoryDao.getPendingOrdersBySeller(getSellerId());
        return sellerOrders;
    }

    public List<OrderEntity> getSellerCompletedOrders() throws ShadesException{
        List<OrderEntity> sellerOrders = inventoryDao.getCompletedOrdersBySeller(getSellerId());
        return sellerOrders;
    }

    public OrderEntity getOrderById(int id) {
        return inventoryDao.getOrderById(id);
    }

    public List<OrderEntity> getAllNewOrders() {
        return inventoryDao.getAllNewOrders();
    }

    public void stageOrders(String[] orderToStage) {
        inventoryDao.updateOrder("processed","1", orderToStage);
    }

    public List<OrderEntity> getStagedOrders() {
        return inventoryDao.getStagedOrders();
    }

    public void updateTrackingIds(File orders) {

        List<OrderEntity> orderList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(orders);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            iterator.next(); //head row

            while (iterator.hasNext())
            {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                OrderEntity order = new OrderEntity();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex)
                    {
                        case 0:
                            order.setOrderId((int)cell.getNumericCellValue());
                            break;
                        case 1:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                order.setSupplierOrderId(cell.getCellTypeEnum().equals(CellType.NUMERIC) ?
                                        String.valueOf(cell.getNumericCellValue()) : cell.getStringCellValue());
                            }
                            break;
                        case 2:

                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                                    order.setTrackingId(String.valueOf(cell.getNumericCellValue()));
                                }else if(cell.getCellTypeEnum().equals(CellType.STRING)){
                                    order.setTrackingId(cell.getStringCellValue());
                                }
                            }
                            break;
                        case 3:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.STRING) && StringUtils.isNotBlank(cell.getStringCellValue())){
                                    order.setShippingCost(Double.valueOf(cell.getStringCellValue()));
                                }else{
                                    order.setShippingCost(cell.getNumericCellValue());
                                }
                            }
                            break;
                        case 4:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.STRING) && StringUtils.isNotBlank(cell.getStringCellValue())){
                                    order.setSupplierPrice(Double.valueOf(cell.getStringCellValue()));
                                }else{
                                    order.setSupplierPrice(cell.getNumericCellValue());
                                }
                                order.setShadesPrice(Utils.shadesPrices(order.getSupplierPrice()));
                            }
                            break;
                        default:
                            break;
                    }
                }
                orderList.add(order);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inventoryDao.updateTrackingInfo(orderList);
    }

    public List<OrderEntity> getAllCompletedOrders() {
        return inventoryDao.getAllCompletedOrders();
    }

    public List<OrderEntity> getOrdersForInvoice(String start, String end) {
        return inventoryDao.getOrdersForInvoice(start, end);
    }

    public List<InventoryEntity> compareUserInventory(File serverFile) throws IOException {

        List<InventoryEntity> userItems = getInventoryEntities(serverFile);
        List<InventoryEntity> currentProductsList = inventoryDao.getAllProducts();
        return Utils.getInventoryComparison(userItems, currentProductsList);
    }

    public List<InventoryEntity> compareAllUserInventory(File serverFile) throws IOException{
        List<InventoryEntity> userItems = getInventoryEntities(serverFile);
        List<InventoryEntity> currentProductsList = inventoryDao.getAllProducts();
        return Utils.getAllInventoryComparison(userItems, currentProductsList);
    }

    private List<InventoryEntity> getInventoryEntities(File serverFile) throws IOException {
        List<InventoryEntity> userItems = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(serverFile);
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        iterator.next(); //skipping head row.

        while ((iterator.hasNext())){

            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            InventoryEntity userItem = new InventoryEntity();

            while (cellIterator.hasNext()){

                Cell currentCell = cellIterator.next();
                int cell = currentCell.getColumnIndex();

                switch (cell){
                    case 0: //Item Sku
                        userItem.setSku(currentCell.getStringCellValue());
                        break;
                    case 1: //Wholesale Price
                        Double cost = currentCell.getNumericCellValue();
                        userItem.setSupplierPrice(cost);
                        userItem.setShadesSellingPrice(Utils.shadesPrices(cost));
                        break;
                    case 2: //Quantity
                        userItem.setQuantity(new Double(currentCell.getNumericCellValue()).intValue());
                        break;
                    default:
                        break;
                }
            }
            userItems.add(userItem);
        }
        return userItems;
    }

    public List<InventoryEntity> getAllInventory() {
        return inventoryDao.getAllProducts();
    }


    public boolean updateInventory(File inventoryFile, int supplierId) {

        List<InventoryEntity> newProductsList = new ArrayList<>();
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
            InventoryEntity item = new InventoryEntity();

            while (cellIterator.hasNext()){

                Cell currentCell = cellIterator.next();
                int cell = currentCell.getColumnIndex();

                switch (cell){
                    case 0: //Item Sku
                        item.setSku(currentCell.getStringCellValue());
                        break;
                    case 1: //Wholesale Price
                        Double cost = currentCell.getNumericCellValue();
                        item.setSupplierPrice(cost);
                        item.setShadesSellingPrice(Utils.shadesPrices(cost));
                        break;
                    case 2: //Quantity
                        item.setQuantity(new Double(currentCell.getNumericCellValue()).intValue());
                        break;
                    case 3://Weight Per Unit

                        Double weight =  currentCell.getNumericCellValue();
                        item.setWeight(weight);

                        if(supplierId == 501){
                            item.setShippingCost(5.0);
                        }else{
                            if(weight <= 1){
                                item.setShippingCost(7.50);
                            }else if(weight <= 2){
                                item.setShippingCost(10.80);
                            }else if(weight <= 3){
                                item.setShippingCost(15.00);
                            }else if(weight <= 4){
                                item.setShippingCost(17.00);
                            }else if(weight <= 6){
                                item.setShippingCost(18.00);
                            }else if(weight <= 7){
                                item.setShippingCost(20.00);
                            }else {
                                item.setShippingCost(99.00);
                            }
                        }
                        break;
                    default:
                        break;
                }

                item.setSupplierId(supplierId);
                item.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                newProductsList.add(item);
            }
        }
        List<InventoryEntity> currentProductsList = inventoryDao.getProductsBySupplier(supplierId);
        List<InventoryEntity> itemsChanged = Utils.getDifferentItems(currentProductsList, newProductsList);
        inventoryDao.updateInventory(itemsChanged);
        return true;
    }
}
