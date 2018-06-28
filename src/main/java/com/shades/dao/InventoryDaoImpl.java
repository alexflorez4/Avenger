package com.shades.dao;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Repository
public class InventoryDaoImpl implements InventoryDao {

    private static final Logger logger = Logger.getLogger(InventoryDaoImpl.class);

    @PersistenceContext //(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    public void updateInventory(Set<InventoryEntity> inventorySet) {

        logger.info("DAO: Updating inventory");
        logger.info("FragX quantity1: " + inventorySet.size());
        //Set<InventoryEntity> inventorySetClone = new
        Query resetInventoryQry = em.createNativeQuery("" +
                "UPDATE aws_db_shades1.Inventory SET quantity = ? WHERE supplierId = ?");
        resetInventoryQry.setParameter(1, 0);
        logger.info("Supplier id: " + inventorySet.stream().findFirst().get().getSupplierId());
        logger.info("FragX quantity2: " + inventorySet.size());
        resetInventoryQry.setParameter(2, inventorySet.stream().findFirst().get().getSupplierId());
        resetInventoryQry.executeUpdate();

        Query query = em.createNativeQuery("" +
                "INSERT IGNORE INTO aws_db_shades1.Inventory(sku, supplierId, quantity, supplierProductId, supplierPrice, shadesSellingPrice, weight, shippingCost, lastUpdate)" +
                "VALUES (?,?,?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE quantity = ?, supplierPrice = ?, shadesSellingPrice = ?, lastUpdate = ?");

            inventorySet.stream().forEach((entity) -> {
                query.setParameter(1, entity.getSku());
                query.setParameter(2, entity.getSupplierId());
                query.setParameter(3, entity.getQuantity());
                query.setParameter(4, entity.getSupplierProductId());
                query.setParameter(5, entity.getSupplierPrice());
                query.setParameter(6, entity.getShadesSellingPrice());
                query.setParameter(7, entity.getWeight());
                query.setParameter(8, entity.getShippingCost());
                query.setParameter(9, entity.getLastUpdate());

                query.setParameter(10, entity.getQuantity());
                query.setParameter(11, entity.getSupplierPrice());
                query.setParameter(12, entity.getShadesSellingPrice());
                query.setParameter(13, entity.getLastUpdate());

                query.executeUpdate();
                logger.info("inserting: " + entity.getSku());
            }

        );
    }

    @Override
    public List<String> getAllProductsSet() {
        Query q = em.createNativeQuery("SELECT sku FROM Inventory");
        return q.getResultList();
    }

    @Override
    public InventoryEntity findProductDetails(String sku) throws ShadesException {

        Query q = em.createNativeQuery("SELECT * FROM Inventory WHERE sku = ?", InventoryEntity.class);
        q.setParameter(1, sku);

        try {
            InventoryEntity ie = (InventoryEntity)q.getSingleResult();
            return ie;
        }catch (NoResultException nre){
            throw new ShadesException("No items where found with SKU " + sku);
        }catch (NonUniqueResultException nur){
            throw new ShadesException("More than 1 sku were found with SKU " + sku);
        }catch (Exception e){
            throw new ShadesException("An error has occurred. Error: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void placeNewOrder(OrderEntity order) {
        em.persist(order);
    }
}
