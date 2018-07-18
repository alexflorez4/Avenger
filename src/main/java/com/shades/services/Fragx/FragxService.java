package com.shades.services.fragx;

import Entities.InventoryEntity;
import com.shades.controller.InventoryController;
import com.shades.dao.InventoryDao;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.BadItemIdException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Models.Product;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Transactional
@Service
public class FragxService {

    private String apiId;
    private String apiKey;

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private InventoryDao inventoryDao;

    public void updateInventory() {

        Set<InventoryEntity> inventorySet = new HashSet<>();

        try {

            List<InventoryEntity> oldList = inventoryDao.getProductsBySupplier(501);

            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            List<Product> newList = client.getAllProductList();

            //Out of Stock Inventory
            Predicate<InventoryEntity> predicate = inventoryEntity -> newList.stream().noneMatch(s -> s.getItemId().equalsIgnoreCase(inventoryEntity.getSku()));
            Set<InventoryEntity> outOfStockItems = oldList.stream().filter(predicate).collect(Collectors.toSet());
            outOfStockItems.stream().forEach(inventoryEntity -> inventoryEntity.setQuantity(0));
            inventoryDao.updateInventory(outOfStockItems);
            logger.info("Out of Stock items: " + outOfStockItems.size());

            //New Inventory
            Predicate<Product> predicate2 = new Predicate<Product>() {
                @Override
                public boolean test(Product product) {
                    return oldList.stream().noneMatch(s -> s.getSku().equalsIgnoreCase(product.getItemId()));
                }
            };
            List<Product> newItems = newList.stream().filter(predicate2).collect(Collectors.toList());
            logger.info("New items: " + newItems.size());

            //TODO: FIX THIS LAMBDA EXPRESSION, USE FUNCTIONAL - COLLECT
            newItems.stream().forEach(s -> {
                InventoryEntity inventoryEntity = new InventoryEntity();
                inventoryEntity.setSku(s.getItemId());
                inventoryEntity.setSupplierId(501);
                inventoryEntity.setQuantity(s.isInstock() ? 10 : 0);
                inventoryEntity.setSupplierProductId(s.getParentCode());
                inventoryEntity.setSupplierPrice(s.getWholesalePriceUSD());
                inventoryEntity.setShadesSellingPrice(Utils.shadesPrices(s.getWholesalePriceUSD()));
                inventoryEntity.setShippingCost(5.00);
                inventoryEntity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                inventorySet.add(inventoryEntity);
            });

        } catch (Exception e) {
            //TODO: CATCH THIS EXCEPTION BETTER
            e.printStackTrace();
        }
        inventoryDao.updateInventory(inventorySet);
    }



    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Product getProductDetails(InventoryEntity product){

        Product productInfo = null;
        try {
            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            productInfo = client.getProductById(product.getSku());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadAccessIdOrKeyException e) {
            e.printStackTrace();
        } catch (HttpErrorException e) {
            e.printStackTrace();
        } catch (BadItemIdException e) {
            e.printStackTrace();
        }

        return productInfo;
    }
}
