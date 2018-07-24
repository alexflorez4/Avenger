package com.shades.services.fragx;

import Entities.InventoryEntity;
import com.shades.controller.InventoryController;
import com.shades.dao.InventoryDao;
import com.shades.exceptions.ShadesException;
import com.shades.services.fragx.Exceptions.*;
import com.shades.services.fragx.Models.Product;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@Service
public class FragxService {

    private String apiId;
    private String apiKey;

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private InventoryDao inventoryDao;

    public void updateInventory() throws ShadesException {

        try {

            List<InventoryEntity> currentProductList = inventoryDao.getProductsBySupplier(501);

            FrgxListingApiClient client = new FrgxListingApiClient(apiId, apiKey);
            List<Product> newProductList = client.getAllProductList();

            //Mapping Product -> InventoryEntity
            Function<Product, InventoryEntity> transProdFunction = product -> {
                InventoryEntity inventory = new InventoryEntity(
                        product.getItemId(),
                        product.isInstock() ? 10 : 0,
                        501,
                        product.getWholesalePriceUSD(),
                        Utils.shadesPrices(product.getWholesalePriceUSD()),
                        5.0,
                        null,
                        new Timestamp(System.currentTimeMillis()),
                        0.0,
                        null);
                return inventory;
            };

            List<InventoryEntity> newProductsList = newProductList.stream().map(transProdFunction).collect(Collectors.toList());
            List<InventoryEntity> itemsChanged = Utils.getDifferentItems(currentProductList, newProductsList);
            inventoryDao.updateInventory(itemsChanged);

        } catch (Exception e) {
            if(e instanceof BadAccessIdOrKeyException || e instanceof BadItemIdException || e instanceof EmptyOrderException
                    || e instanceof HttpErrorException || e instanceof NullResponseException){
                throw new ShadesException("Error with Fragrance X. Exception: " + e.getClass().getName());
            }else {
                throw new ShadesException("Error. " + e.getMessage());
            }
        }
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
