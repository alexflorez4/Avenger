package com.shades.dao;


import Entities.InventoryEntity;

import java.util.List;
import java.util.Set;


public interface InventoryDao {

    void updateInventory(Set<InventoryEntity> inventorySet);

    List<String> getAllProductsSet();
}
