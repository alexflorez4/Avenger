package com.shades.services.fragx;

import Entities.InventoryEntity;
import com.shades.controller.InventoryController;
import com.shades.dao.InventoryDao;
import com.shades.services.fragx.Models.Product;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


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
            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            List<Product> products = client.getAllProductList();
            logger.info("Products: " + products.size());


            Consumer<Product> consumer = new Consumer<Product>() {
                @Override
                public void accept(Product product) {

                }
            };

            products.stream().forEach(s -> {
                InventoryEntity inventoryEntity = new InventoryEntity();
                inventoryEntity.setSku(s.getItemId());
                inventoryEntity.setSupplierId(501);
                inventoryEntity.setQuantity(s.isInstock() ? 10 : 0);
                inventoryEntity.setSupplierProductId(s.getParentCode());
                inventoryEntity.setSupplierPrice(new Float(s.getWholesalePriceUSD()));
                inventoryEntity.setShadesSellingPrice(Utils.shadesPrices(new Float(s.getWholesalePriceUSD())));
                inventoryEntity.setShippingCost(new Float(5.00));
                inventoryEntity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                inventorySet.add(inventoryEntity);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Inventory Set Size in Service: " + inventorySet.size());
        inventoryDao.updateInventory(inventorySet);
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
