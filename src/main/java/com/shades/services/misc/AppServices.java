package com.shades.services.misc;


import Entities.AsinEntity;
import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.google.common.collect.Maps;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.model.AsinItem;
import com.shades.utilities.Enumerations;
import com.shades.utilities.ParseAmzOrder;
import com.shades.utilities.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.StringUtil;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;

import static com.shades.utilities.Utils.getRandomNumberInRange;

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
                                //order.setShadesPrice(Utils.shadesPrices(order.getSupplierId(), order.getSupplierPrice()));
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

    public List<InventoryEntity> compareUserInventory(File serverFile, int margin) throws IOException {

        List<InventoryEntity> userItems = getInventoryEntities(serverFile);
        List<InventoryEntity> currentProductsList = inventoryDao.getAllProducts();
        return Utils.getInventoryComparison(userItems, currentProductsList, margin);
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
                        String sku;
                        if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                            //sku = String.valueOf((int) cell.getNumericCellValue());
                            sku = NumberToTextConverter.toText(cell.getNumericCellValue());
                        }else if(StringUtils.isNotBlank(cell.getStringCellValue())){
                            sku = cell.getStringCellValue();
                        }else{
                            continue;
                        }
                        userItem.setSku(Utils.parseSku(sku));
                        // TODO: 9/6/2018 sku is holding the old sku before parsing.  need to fix this
                        userItem.setSupplierProductId(sku);
                        break;
                    case 1: //Wholesale Price
                        //Holds the seller price on markets
                        if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                            userItem.setShadesSellingPrice(cell.getNumericCellValue());
                        }else if(cell.getCellTypeEnum().equals(CellType.BLANK)){
                            userItem.setShadesSellingPrice(0.0);
                        }
                        break;
                    case 2: //Quantity of seller on market
                        if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                            userItem.setQuantity(new Double(cell.getNumericCellValue()).intValue());
                        }else if(cell.getCellTypeEnum().equals(CellType.BLANK)){
                            userItem.setQuantity(0);
                        }
                        break;
                    default:
                        break;
                }
            }
            if(StringUtils.isBlank(userItem.getSku())){
                continue;
            }
            if(userItem.getQuantity() == null){
                userItem.setQuantity(0);
            }
            if(userItem.getShadesSellingPrice() == null){
                userItem.setShadesSellingPrice(0.0);
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
                        item.setSku(cell.getCellTypeEnum().equals(CellType.STRING) ? cell.getStringCellValue() : String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 1: //Wholesale Price
                        Double cost = cell.getNumericCellValue();
                        if(supplierId == 500){
                            cost += 2;
                        }
                        item.setSupplierPrice(cost);
                        item.setShadesSellingPrice(Utils.shadesPrices(supplierId, cost));
                        break;
                    case 2: //Quantity
                        item.setQuantity(new Double(cell.getNumericCellValue()).intValue());
                        break;
                    case 3://Weight Per Unit
                        Double weight =  cell.getNumericCellValue();
                        item.setWeight(weight);
                        item.setShippingCost(Utils.calculateShippingCost(supplierId, item.getQuantity(), weight));
                        item.setSuggestedPrice(Utils.getProductRecommendedPrice(item.getShadesSellingPrice(), item.getShippingCost()));
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

    public List<AsinItem> uploadAsin(File serverFile)throws IOException {

        List<AsinEntity> userAsinList = new ArrayList<>();
        Map<String, AsinEntity> userAsinMap = new HashMap<>();
        List<InventoryEntity> dbProductList = inventoryDao.getAllProducts();
        Map<String, InventoryEntity> dbProductsMap = Maps.newHashMap();
        dbProductList.stream().forEach(product -> dbProductsMap.put(product.getSku(), product));

        FileInputStream fileInputStream = new FileInputStream(serverFile);
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        iterator.next(); //skipping head row.

        while ((iterator.hasNext())) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            String sku = StringUtils.EMPTY;
            String asin = StringUtils.EMPTY;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        if(cell.getCellTypeEnum().equals(CellType.BLANK)){
                            continue;
                        }
                        if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                            sku = Utils.parseSku(NumberToTextConverter.toText(cell.getNumericCellValue()));
                        }else{
                            sku = Utils.parseSku(cell.getStringCellValue());
                        }
                        break;
                    case 1:
                        if(cell.getCellTypeEnum().equals(CellType.BLANK)){
                            continue;
                        }
                        if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                            asin = NumberToTextConverter.toText(cell.getNumericCellValue());
                        }else{
                            asin = cell.getStringCellValue();
                        }
                        break;
                    default:
                        break;
                }
            }
            if(dbProductsMap.containsKey(sku) && StringUtils.isNotBlank(asin)) {
                userAsinList.add(new AsinEntity(sku, asin));
                userAsinMap.put(asin, new AsinEntity(sku, asin));
            }
        }

        //Get Database existing asins
        List<AsinEntity> dbAsinList = inventoryDao.getAllAsin();
        Map<String, AsinEntity> dbAsinMap = new HashMap<>();
        dbAsinList.stream().forEach(asinEntity -> dbAsinMap.put(asinEntity.getAsin(), asinEntity));

        List<AsinEntity> asinToDB = new ArrayList<>();

        //Compare with database asins
        Consumer<AsinEntity> consumer = asinFile -> {
            if(!dbAsinMap.containsKey(asinFile.getAsin())){
                asinToDB.add(asinFile);
            }
        };
        userAsinList.stream().forEach(consumer);
        //Update db with new asins
        inventoryDao.insertAsin(asinToDB);
        //System.out.println("Asins to database: ");
        for (AsinEntity ae : asinToDB){
            System.out.println(ae.getSku() + " " + ae.getAsin());
        }

        List<AsinItem> asinToAmz = new ArrayList<>();
        Consumer<AsinEntity> consumer1 = dbAsin -> {
            if(!userAsinMap.containsKey(dbAsin.getAsin())){
                InventoryEntity dbItem = dbProductsMap.get(dbAsin.getSku());
                System.out.println(dbAsin.getSku());
                String sku = dbAsin.getSku() + "_" + new Random().nextInt(999);
                Double shipping = dbItem.getShippingCost() == null ? 9.99 : dbItem.getShippingCost();
                Double sellingPrice = Utils.getProductRecommendedPrice(dbItem.getShadesSellingPrice(), shipping, 20) + new Random().nextFloat();
                DecimalFormat df = new DecimalFormat("#.##");
                sellingPrice = Double.valueOf(df.format(sellingPrice));
                asinToAmz.add(new AsinItem(sku, dbAsin.getAsin(), 1, sellingPrice, "delete", "delete", 11, getRandomNumberInRange(3, 10),
                        'a', 'n', 2));
            }
        };
        try {
            dbAsinList.stream().forEach(consumer1);
        }catch (Exception e){
            logger.info("Exception: " + e.getMessage() + " \nCause: " + e.getCause() + " \nTrace: " + e.getStackTrace());
        }
        return asinToAmz;
    }
}
