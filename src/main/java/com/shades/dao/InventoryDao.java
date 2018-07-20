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

    void placeNewOrder(OrderEntity order) throws ShadesException;

    int getSellerId(String seller) throws ShadesException;

    int getNextOrderId() throws ShadesException;

    List<InventoryEntity> getProductsBySupplier(int i);

    List<OrderEntity> getPendingOrdersBySeller(int sellerId);

    OrderEntity getOrderById(int orderId);
}
