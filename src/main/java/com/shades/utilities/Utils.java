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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.shades.utilities.Enumerations.Suppliers.AZEnum;
import static com.shades.utilities.Enumerations.Suppliers.FXEnum;
import static com.shades.utilities.Enumerations.Suppliers.TDEnum;

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

    public static Double shadesPrices(int supplier, Double supplierPrice){

        if(supplier == 501 || supplier == 503){
            return shadesPriceForFragXProducts(supplierPrice);
        }else{
            Double shadesSellingPrice;
            shadesSellingPrice = supplierPrice + (supplierPrice * 0.15);
            return shadesSellingPrice;
        }
    }

    public static Double shadesPriceForFragXProducts(Double supplierPrice){
        Double shadesSellingPrice;
        if(supplierPrice <= 20){
            shadesSellingPrice = supplierPrice + 1;
        }else{
            shadesSellingPrice = supplierPrice + (supplierPrice * 0.05);
        }

        return shadesSellingPrice;
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

                Double suggestedPrice = Utils.getProductRecommendedPrice(databaseProduct.getShadesSellingPrice(), databaseProduct.getShippingCost());
                if(suggestedPrice > userItem.getShadesSellingPrice()){
                    userItem.setStatus("Alert - Under priced");
                    userItem.setSuggestedPrice(suggestedPrice);
                }
                if(databaseProduct.getQuantity() == 0 && userItem.getQuantity() > 0){
                    userItem.setStatus("Out of Stock");
                }
                if(databaseProduct.getQuantity() > 0 && userItem.getQuantity() == 0){
                    String temp = StringUtils.isBlank(userItem.getStatus()) ? "Stocked" : userItem.getStatus() + " & Stocked";
                    userItem.setStatus(temp);
                }
            }
            // TODO: 9/6/2018 temporary fix to hold the old sku.  need to fix this. 
            userItem.setSku(userItem.getSupplierProductId());
        };

        userItems.stream().forEach(compare);
        return userItems;
    }

    public static List<InventoryEntity> getInventoryComparison(List<InventoryEntity> userItems, List<InventoryEntity> currentProductsList, Integer margin) {

        //Existing inventory to multimap
        Multimap<String, InventoryEntity> currProductsMultiMap = ArrayListMultimap.create();
        currentProductsList.stream().forEach(inventoryEntity -> currProductsMultiMap.put(inventoryEntity.getSku(), inventoryEntity));

        Consumer<InventoryEntity> compare = userItem -> {

            if(!currProductsMultiMap.containsKey(userItem.getSku())){
                userItem.setStatus("SKU " + userItem.getSku() + "not found");
            }else{
                Collection<InventoryEntity> collection = currProductsMultiMap.get(userItem.getSku());
                InventoryEntity databaseProduct = collection.stream().findAny().get();

                Double suggestedPrice = Utils.getProductRecommendedPrice(databaseProduct.getShadesSellingPrice(), databaseProduct.getShippingCost(), margin);
                if(suggestedPrice > userItem.getShadesSellingPrice()){
                    userItem.setStatus("Under priced");
                }else if((userItem.getShadesSellingPrice() * 100)/suggestedPrice > 1.30 ){
                    userItem.setStatus("Over priced");
                }
                if(databaseProduct.getQuantity() == 0 && userItem.getQuantity() > 0){
                    userItem.setStatus("Out of Stock");
                }
                if(databaseProduct.getQuantity() > 0 && userItem.getQuantity() == 0){
                    String temp = StringUtils.isBlank(userItem.getStatus()) ? "Stocked" : userItem.getStatus() + " & Stocked";
                    userItem.setStatus(temp);
                }

                userItem.setSuggestedPrice(suggestedPrice);
            }
            userItem.setSku(userItem.getSupplierProductId());
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

    public static String parseSku(String sku){

        String parsedSku = StringUtils.remove(sku, "SKU:").trim();

        if(Pattern.matches("\\w+-\\d+\\s\\d+", parsedSku)){
            parsedSku = StringUtils.removeAll(parsedSku, "\\s\\d+");
        } else if(Pattern.matches("\\w+-\\d+_\\d+", parsedSku)){
            parsedSku = StringUtils.removeAll(parsedSku, "[_]+\\d+");
        }else if(Pattern.matches("\\w+-\\d+-\\d+", parsedSku)){
            parsedSku = StringUtils.substringBeforeLast(parsedSku, "-");
        }else {
            parsedSku = StringUtils.removeAll(parsedSku, "(\\s+)(\\d+)|(\\s+)([Xx])(\\d+)").trim();
            parsedSku = StringUtils.removePattern(parsedSku, "[_]+\\d++").trim();
            parsedSku = StringUtils.removePattern(parsedSku, "[-]+\\d++").trim();
        }
        return parsedSku;
    }

    public static Double getProductRecommendedPrice(Double shadesSalePrice, Double shippingCost){
        Double profit15Percent = (shadesSalePrice * 0.15) + shadesSalePrice;
        Double includedShipping = profit15Percent + shippingCost;
        Double marketProfit = (includedShipping * 0.18) + includedShipping;
        return marketProfit;
    }

    public static Double getProductRecommendedPrice(Double shadesSalePrice, Double shippingCost, Integer margin){
        Double profit = (shadesSalePrice * (margin/100)) + shadesSalePrice;
        Double includedShipping = profit + shippingCost;
        Double marketProfit = (includedShipping * 0.18) + includedShipping;
        return marketProfit;
    }

    public static String cellNumberToString(Double numericCellValue) {
        DecimalFormat format = new DecimalFormat("0");
        return String.valueOf(format.format(numericCellValue));
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Double calculateShippingCost(int supplierId, int quantity, double weight){
        logger.info("Calculating shipping cost" + supplierId + " " + quantity + " " + weight);
        Double shippingCost;
        
        if(supplierId == 501){
            shippingCost = 5.95;
        }else if(supplierId == 503){
            shippingCost = 6.50;
        }else{
            if(weight <= 1){
               shippingCost = 7.76;
            }else if(weight <= 2){
               shippingCost = 10.80;
            }else if(weight <= 3){
               shippingCost = 15.34;
            }else if(weight <= 4){
               shippingCost = 18.15;
            }else if(weight <= 5){
               shippingCost = 21.03;
            }else if(weight <= 6){
               shippingCost = 24.07;
            }else if(weight <= 7){
               shippingCost = 27.04;
            }else if(weight <= 8){
               shippingCost = 30.36;
            }else if(weight <= 9){
               shippingCost = 33.75;
            }else if(weight <= 10){
               shippingCost = 36.71;
            }else if(weight <= 11){
               shippingCost = 39.76;
            }else if(weight <= 12){
               shippingCost = 42.65;
            }else if(weight <= 13){
               shippingCost = 44.16;
            }else if(weight <= 14){
               shippingCost = 46.35;
            }else if(weight <= 15){
               shippingCost = 47.57;
            }else if(weight <= 16){
               shippingCost = 50.19;
            }else if(weight <= 17){
               shippingCost = 52.85;
            }else if(weight <= 18){
               shippingCost = 55.5;
            }else if(weight <= 19){
               shippingCost = 58.13;
            }else if(weight <= 20){
               shippingCost = 60.82;
            }else if(weight <= 21){
               shippingCost = 61.6;
            }else if(weight <= 22){
               shippingCost = 62.31;
            }else if(weight <= 23){
               shippingCost = 62.69;
            }else if(weight <= 24){
               shippingCost = 64.21;
            }else if(weight <= 25){
               shippingCost = 65.33;
            }else {
               shippingCost = 99.00;
            }
        }

        if(quantity > 1){
            shippingCost += (quantity-1);
        }

        logger.info("End of Calculating shipping cost" + shippingCost);

        return shippingCost;
    }
}
