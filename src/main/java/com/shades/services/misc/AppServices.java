package com.shades.services.misc;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.utilities.ParseAmzOrder;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.*;

@Transactional
@Service
public class AppServices {

    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    public List<String> allProductsSet(){
        return inventoryDao.getAllProductsSet();
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
                    Cell currentCell = cellIterator.next();
                    int cell = currentCell.getColumnIndex();
                    switch (cell)
                    {
                        case 0:
                            order.setOrderId((int)currentCell.getNumericCellValue());
                            break;
                        case 11:
                            order.setTrackingId(currentCell.getStringCellValue());
                            break;
                        case 12:
                            order.setShippingCost(currentCell.getNumericCellValue());
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
}
