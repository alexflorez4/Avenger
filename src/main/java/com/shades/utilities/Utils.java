package com.shades.utilities;


import Entities.InventoryEntity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.shades.exceptions.ShadesException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static Double shadesPrices(Double supplierPrice){
        return supplierPrice + (supplierPrice * 0.15);
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

    public static File saveMultipartFileToServer(MultipartFile multipartFile) throws IOException {

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "uploads");

        if (!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + multipartFile);

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        byte[] bytes = multipartFile.getBytes();
        stream.write(bytes);
        stream.close();
        return serverFile;
    }

    public static List<InventoryEntity> getInventoryComparison(List<InventoryEntity> userItems, List<InventoryEntity> currentProductsList) {

        //Existing inventory to multimap
        Multimap<String, InventoryEntity> currProductsMultiMap = ArrayListMultimap.create();
        currentProductsList.stream().forEach(inventoryEntity -> currProductsMultiMap.put(inventoryEntity.getSku(), inventoryEntity));

        Consumer<InventoryEntity> compare = userItem -> {

            if(!currProductsMultiMap.containsKey(userItem.getSku())){
                userItem.setStatus("SKU not found");
            }else{
                Collection<InventoryEntity> collection = currProductsMultiMap.get(userItem.getSku());
                InventoryEntity databaseProduct = collection.stream().findAny().get();
                if(databaseProduct.getShadesSellingPrice() > userItem.getShadesSellingPrice()){
                    userItem.setStatus("Increase to " + databaseProduct.getShadesSellingPrice());
                }else if(userItem.getShadesSellingPrice() > databaseProduct.getShadesSellingPrice()){
                    userItem.setStatus("Decrease to "  + databaseProduct.getShadesSellingPrice());
                }
                if(databaseProduct.getQuantity() == 0 && userItem.getQuantity() > 0){
                    userItem.setStatus("Out of Stock");
                }
                if(databaseProduct.getQuantity() > 0 && userItem.getQuantity() == 0){
                    String temp = StringUtils.isBlank(userItem.getStatus()) ? "Stocked" : userItem.getStatus() + " & Stocked";
                    userItem.setStatus(temp);
                }
            }
        };

        userItems.stream().forEach(compare);
        return userItems;
    }

    public static List<InventoryEntity> getAllInventoryComparison(List<InventoryEntity> userProductList, List<InventoryEntity> databaseProductList){

        Multimap<String, InventoryEntity> resultProductMultimap = ArrayListMultimap.create();

        Multimap<String, InventoryEntity> userProductsMultimap = ArrayListMultimap.create();
        userProductList.stream().forEach(inventoryEntity -> userProductsMultimap.put(inventoryEntity.getSku(), inventoryEntity));

        //Existing inventory to multimap
        Multimap<String, InventoryEntity> dbProductsMultiMap = ArrayListMultimap.create();
        databaseProductList.stream().forEach(inventoryEntity -> dbProductsMultiMap.put(inventoryEntity.getSku(), inventoryEntity));

        Consumer<InventoryEntity> compare = dbItem -> {
        InventoryEntity resultItem = new InventoryEntity();
        resultItem.setSku(dbItem.getSku());
        resultItem.setShadesSellingPrice(dbItem.getShadesSellingPrice());

            if(userProductsMultimap.containsKey(dbItem.getSku())){

                Collection<InventoryEntity> item = userProductsMultimap.get(dbItem.getSku());
                InventoryEntity userItem = item.stream().findAny().get();
                resultItem.setQuantity(dbItem.getQuantity() == null ? 0 : dbItem.getQuantity());


                if(dbItem.getShadesSellingPrice() > userItem.getShadesSellingPrice()){
                    resultItem.setStatus("Increased to " + dbItem.getShadesSellingPrice());
                }else if(userItem.getShadesSellingPrice() > dbItem.getShadesSellingPrice()){
                    resultItem.setStatus("Decreased to " + dbItem.getShadesSellingPrice());
                }
                if(dbItem.getQuantity() == 0 && userItem.getQuantity() > 0){
                    resultItem.setStatus("Out of Stock");
                }
                if(dbItem.getQuantity() > 0 && userItem.getQuantity() == 0 ){
                    String temp = StringUtils.isBlank(dbItem.getStatus()) ? "Stocked" : dbItem.getStatus() + " & Stocked";
                    resultItem.setStatus(temp);
                }
            }else {
                resultItem.setQuantity(dbItem.getQuantity());
                resultItem.setStatus("SKU not listed");
            }
            resultProductMultimap.put(resultItem.getSku(), resultItem);
        };

        databaseProductList.stream().forEach(compare);

        return resultProductMultimap.values().stream().collect(Collectors.toList());
    }
}
