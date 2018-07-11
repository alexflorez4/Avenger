package com.shades.dao;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;

import java.util.List;
import java.util.Set;


public interface InventoryDao {

    void updateInventory(Set<InventoryEntity> inventorySet);

    List<String> getAllProductsSet();

    InventoryEntity findProductDetails(String sku) throws ShadesException;

    void placeNewOrder(OrderEntity order);

    int getUserId(String seller) throws ShadesException;
}
