package com.shades.dao;


import Entities.InventoryEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Set;
import java.util.function.Consumer;


@Repository
public class InventoryDaoImpl implements InventoryDao {

    private static final Logger logger = Logger.getLogger(InventoryDaoImpl.class);

    @PersistenceContext //(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    public void insertAzInventory(Set<InventoryEntity> inventorySet) {

        logger.info("DAO: inserting inventory");

        Query query = em.createNativeQuery("" +
                "INSERT IGNORE INTO aws_db_shades1.Inventory(sku, supplierId, quantity, supplierPrice, shadesSellingPrice, weight, shippingCost, lastUpdate) " +
                "VALUES (?,?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE quantity = ?, supplierPrice = ?, shadesSellingPrice = ?, lastUpdate = ?");
                //"ON DUPLICATE KEY UPDATE quantity = ?, supplierPrice = ?");

        inventorySet.stream().forEach((entity) -> {
            query.setParameter(1, entity.getSku());
            query.setParameter(2, entity.getSupplierId());
            query.setParameter(3, entity.getQuantity());
            query.setParameter(4, entity.getSupplierPrice());
            query.setParameter(5, entity.getShadesSellingPrice());
            query.setParameter(6, entity.getWeight());
            query.setParameter(7, entity.getShippingCost());
            query.setParameter(8, entity.getLastUpdate());

            query.setParameter(9, entity.getQuantity());
            query.setParameter(10, entity.getSupplierPrice());
            query.setParameter(11, entity.getShadesSellingPrice());
            query.setParameter(12, entity.getLastUpdate());

            query.executeUpdate();
            logger.info("inserting: " + entity.getSku());
        }

        );

        /*for(InventoryEntity entity : inventorySet){
            Query query = em.createNativeQuery("" +
                    "INSERT IGNORE INTO aws_db_shades1.Inventory(sku, supplierId, quantity) " +
                    "VALUES (?,?,?)" +
                    "ON DUPLICATE KEY UPDATE quantity = ?");
            query.setParameter(1, entity.getSku());
            query.setParameter(2, entity.getSupplierId());
            query.setParameter(3, entity.getQuantity());
            query.setParameter(4, entity.getQuantity());
            query.executeUpdate();
        }*/

    }
}
