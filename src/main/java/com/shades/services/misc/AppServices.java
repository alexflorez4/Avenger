package com.shades.services.misc;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.services.fragx.FragxService;
import com.shades.services.fragx.Models.Product;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Transactional
@Service
public class AppServices {

    private static final int FRAGX = 501;
    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private FragxService fragxService;

    public List<String> allProductsSet(){
        return inventoryDao.getAllProductsSet();
    }

    public boolean processNewSingleOrder(OrderEntity order, String seller) throws ShadesException{

        int sellerId = inventoryDao.getUserId(seller);

        InventoryEntity product = inventoryDao.findProductDetails(order.getSku());
        processProductDetails(order, product);
        order.setSellerId(1);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setSupplierId(product.getSupplierId());
        order.setSellerId(sellerId);

        inventoryDao.placeNewOrder(order);

        logger.info("New Order: " + order);
        logger.info("Product Information:" + product);
        return false;
    }

    private void processProductDetails(OrderEntity order, InventoryEntity product) throws ShadesException {

        if(product.getSupplierId() == FRAGX){
            Product productInfo = fragxService.getProductDetails(product);

            if(!productInfo.isInstock()){
                throw new ShadesException("Product " + product.getSku() + " is out of stock in Fragrance X.");
            }

            Double shadesPrice = Utils.shadesPrices(productInfo.getWholesalePriceUSD());
            Double shippingCost = Utils.fragXShippingCost(order.getQuantity());

            order.setSupplierPrice(productInfo.getWholesalePriceUSD());
            order.setShadesPrice(shadesPrice);
            order.setShippingCost(shippingCost);
            order.setTotalPriceShades(shadesPrice + shippingCost);

        }else{
            order.setSupplierPrice(product.getSupplierPrice());
            order.setShadesPrice(product.getShadesSellingPrice());
            order.setShippingCost(product.getShippingCost());
        }

    }
}
