package com.shades.services.misc;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.utilities.Enumerations;
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

    private static final Logger logger = Logger.getLogger(AppServices.class);

    @Autowired
    private InventoryDao inventoryDao;

    public List<String> allProductsSet(){
        return inventoryDao.getAllSKUs();
    }

    public void processNewSingleOrder(OrderEntity order) throws ShadesException{

        if(order.getOrderId() == 0){
            int nextOrderId = inventoryDao.getNextOrderId();
            order.setOrderId(nextOrderId);
            order.setSellerId(getSellerId());
            order.setSellerName(Enumerations.Sellers.getSellerName(order.getSellerId()));
        }

        inventoryDao.placeNewOrder(order);
    }

    private int getSellerId() throws ShadesException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return inventoryDao.getSellerId(userName);
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
                logger.info("Exception thrown at app services: " + e);
                nextOrder.setObservations(e.getMessage());
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
                System.out.println(currentRow.getPhysicalNumberOfCells());
                if(currentRow.getPhysicalNumberOfCells() <= 1){
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.iterator();
                OrderEntity order = new OrderEntity();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex)
                    {
                        case 0:
                            if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                                order.setOrderId((int)cell.getNumericCellValue());
                            }else if(StringUtils.isNotBlank(cell.getStringCellValue())){
                                order.setOrderId(Integer.valueOf(cell.getStringCellValue()));
                            }else {
                                continue;
                            }
                            break;
                        case 1:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                order.setSupplierOrderId(cell.getCellTypeEnum().equals(CellType.NUMERIC) ?
                                    Utils.cellNumberToString(cell.getNumericCellValue()) : cell.getStringCellValue());
                            }
                            break;
                        case 2:

                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                                    String cellValue = Utils.cellNumberToString(cell.getNumericCellValue());
                                    order.setTrackingId(cellValue);
                                }else if(cell.getCellTypeEnum().equals(CellType.STRING) && StringUtils.isNotBlank(cell.getStringCellValue())){
                                    order.setTrackingId(cell.getStringCellValue());
                                }
                            }
                            break;
                        case 3:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.STRING) && StringUtils.isNotBlank(cell.getStringCellValue())){
                                    order.setShippingCost(Double.valueOf(cell.getStringCellValue()));
                                }else if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                                    order.setShippingCost(cell.getNumericCellValue());
                                }
                            }
                            break;
                        case 4:
                            if(!cell.getCellTypeEnum().equals(CellType.BLANK)){
                                if(cell.getCellTypeEnum().equals(CellType.STRING) && StringUtils.isNotBlank(cell.getStringCellValue())){
                                    order.setSupplierPrice(Double.valueOf(cell.getStringCellValue()));
                                }else if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                                    order.setSupplierPrice(cell.getNumericCellValue());
                                }
                                order.setShadesPrice(Utils.shadesPrices(order.getSupplierPrice()));
                            }
                            break;
                        default:
                            break;
                    }
                }
                if(order.getOrderId() == 0){
                    continue;
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

                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();

                switch (columnIndex){
                    case 0: //Item Sku
                        userItem.setSupplierProductId(cell.getCellTypeEnum().equals(CellType.STRING) ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue()));
                        String sku = cell.getCellTypeEnum().equals(CellType.STRING) ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
                        userItem.setSku(Utils.parseSku(sku));
                        break;
                    case 1: //Wholesale Price
                        //Holds the seller price on markets
                        userItem.setShadesSellingPrice(cell.getNumericCellValue());
                        break;
                    case 2: //Quantity of seller on market
                        userItem.setQuantity(new Double(cell.getNumericCellValue()).intValue());
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

                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();

                switch (columnIndex){
                    case 0: //Item Sku
                        item.setSku(cell.getCellTypeEnum().equals(CellType.STRING) ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue()));
                        break;
                    case 1: //Wholesale Price
                        Double cost = cell.getNumericCellValue();
                        if(supplierId == 500){
                            cost += 2;
                        }
                        item.setSupplierPrice(cost);
                        item.setShadesSellingPrice(Utils.shadesPrices(cost));
                        break;
                    case 2: //Quantity
                        item.setQuantity(new Double(cell.getNumericCellValue()).intValue());
                        break;
                    case 3://Weight Per Unit

                        Double weight =  cell.getNumericCellValue();
                        item.setWeight(weight);

                        if(supplierId == 501){
                            item.setShippingCost(6.0);
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
                            item.setSuggestedPrice(Utils.getProductRecommendedPrice(item.getShadesSellingPrice(), item.getShippingCost()));
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
