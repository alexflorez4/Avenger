package com.shades.dao;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;

import java.util.List;
import java.util.Set;


public interface InventoryDao {

    void updateInventory(List<InventoryEntity> inventorySet);

    List<String> getAllSKUs();

    List<InventoryEntity> getAllProducts();

    InventoryEntity findProductDetails(String sku) throws ShadesException;

    void placeNewOrder(OrderEntity order) throws ShadesException;

    int getSellerId(String seller) throws ShadesException;

    int getNextOrderId() throws ShadesException;

    List<InventoryEntity> getProductsBySupplier(int i);

    List<OrderEntity> getPendingOrdersBySeller(int sellerId);

    OrderEntity getOrderById(int orderId);

    List<OrderEntity> getCompletedOrdersBySeller(int sellerId);

    List<OrderEntity> getAllNewOrders();

    void updateOrder(String field, String value, String[] orderIds);

    List<OrderEntity> getStagedOrders();

    void updateTrackingInfo(List<OrderEntity> orderList);

    List<OrderEntity> getAllCompletedOrders();

    List<OrderEntity> getOrdersForInvoice(String start, String end);
}
