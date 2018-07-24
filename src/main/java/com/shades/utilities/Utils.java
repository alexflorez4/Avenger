package com.shades.utilities;


import Entities.InventoryEntity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.shades.exceptions.ShadesException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    public static File multipartToFile(MultipartFile multipart) throws ShadesException {

        try {
            File newFile = new File(multipart.getOriginalFilename());
            multipart.transferTo(newFile);
            return newFile;
        } catch (IOException e) {
            logger.info("Error at multipart file conversion. " + e.getLocalizedMessage());
            throw new ShadesException("Error at multipart file conversion to file. Message: " + e.getMessage());
        }

    }

    public static Double shadesPrices(Double cost){
        return cost + (cost * 0.15);
    }

    public static Double fragXShippingCost(int quantity){
        Double shippingCost = 4.95 + quantity;
        return shippingCost;
    }

    public static List<InventoryEntity> getDifferentItems(List<InventoryEntity> currentProductList, List<InventoryEntity> newProductsList){

        Multimap<String, InventoryEntity> newProductsMultimap = ArrayListMultimap.create();
        newProductsList.stream().forEach(item-> newProductsMultimap.put(item.getSku(), item));

        //Existing inventory to multimap
        Multimap<String, InventoryEntity> currProductsMultiMap = ArrayListMultimap.create();
        currentProductList.stream().forEach(inventoryEntity -> currProductsMultiMap.put(inventoryEntity.getSku(), inventoryEntity));

        //new items
        Predicate<InventoryEntity> testNewProd = inventoryEntity -> !currProductsMultiMap.containsKey(inventoryEntity.getSku());
        Consumer<InventoryEntity> addNewItemsConsumer = inventoryEntity -> {
            inventoryEntity.setStatus("new");
            currProductsMultiMap.put(inventoryEntity.getSku(), inventoryEntity);
        };
        newProductsList.stream().filter(testNewProd).forEach(addNewItemsConsumer);

        Consumer<InventoryEntity> applyChanges = new Consumer<InventoryEntity>() {
            @Override
            public void accept(InventoryEntity currentProduct) {
                //Out of Stock
                if(!newProductsMultimap.containsKey(currentProduct.getSku())){
                    Collection<InventoryEntity> replacement = currProductsMultiMap.get(currentProduct.getSku());
                    InventoryEntity entity = replacement.stream().findAny().get();
                    entity.setQuantity(0);
                    entity.setStatus("out");
                    currProductsMultiMap.removeAll(currentProduct.getSku());
                    currProductsMultiMap.put(entity.getSku(), entity);
                }else{
                    Collection<InventoryEntity> collection = newProductsMultimap.get(currentProduct.getSku());
                    InventoryEntity newProduct = collection.stream().findAny().get();
                    if(newProduct.getSupplierPrice() > currentProduct.getSupplierPrice()){
                        newProduct.setStatus("increase");
                        currProductsMultiMap.removeAll(currentProduct.getSku());
                        currProductsMultiMap.put(newProduct.getSku(), newProduct);
                    }else if(currentProduct.getSupplierPrice() > newProduct.getSupplierPrice()){
                        newProduct.setStatus("decrease");
                        currProductsMultiMap.removeAll(currentProduct.getSku());
                        currProductsMultiMap.put(newProduct.getSku(), newProduct);
                    }
                    if(currentProduct.getQuantity() == 0 && newProduct.getQuantity() > 0){
                        String temp = StringUtils.isBlank(newProduct.getStatus()) ? "stocked " : newProduct.getStatus() + "& stocked";
                        newProduct.setStatus(temp);
                        currProductsMultiMap.removeAll(currentProduct.getSku());
                        currProductsMultiMap.put(newProduct.getSku(), newProduct);
                    }
                    if(currentProduct.getQuantity() > 0 && newProduct.getQuantity() == 0){
                        newProduct.setStatus("out");
                        currProductsMultiMap.removeAll(currentProduct.getSku());
                        currProductsMultiMap.put(newProduct.getSku(), newProduct);
                    }
                    if(newProduct.getStatus() == null){
                        //no changes
                        currProductsMultiMap.removeAll(currentProduct.getSku());
                    }
                }
            }
        };

        currentProductList.stream().forEach(applyChanges);

        List<InventoryEntity> itemsChangedList = new ArrayList<>();
        for(Map.Entry entry : currProductsMultiMap.entries()){
            itemsChangedList.add((InventoryEntity) entry.getValue());
        }

        return itemsChangedList;
    }
}
