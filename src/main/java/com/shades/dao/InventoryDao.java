package com.shades.dao;


import Entities.InventoryEntity;
import java.util.Set;


public interface InventoryDao {

    void insertAzInventory(Set<InventoryEntity> inventorySet);
}
