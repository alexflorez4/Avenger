package com.shades.services.misc;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.utilities.ParseAmzOrder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class AppServices {

    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    public List<String> allProductsSet(){
        return inventoryDao.getAllProductsSet();
    }

    public boolean processNewSingleOrder(OrderEntity order, String seller) throws ShadesException{

        int sellerId = inventoryDao.getSellerId(seller);

        //InventoryEntity product = inventoryDao.findProductDetails(order.getSku());
        //processProductDetails(order, product);

        int nextOrderId = inventoryDao.getNextOrderId();
        order.setOrderId(nextOrderId);
        order.setSellerId(sellerId);
        //order.setSupplierId(product.getSupplierId());

        inventoryDao.placeNewOrder(order);

        logger.info("New Order: " + order);
        //logger.info("Product Information:" + product);
        return false;
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

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        int sellerId = inventoryDao.getSellerId(userName);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        int sellerId = inventoryDao.getSellerId(userName);
        List<OrderEntity> sellerOrders = inventoryDao.getPendingOrdersBySeller(sellerId);
        return sellerOrders;
    }
}
