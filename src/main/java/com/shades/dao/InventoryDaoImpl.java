package com.shades.dao;

import Entities.InventoryEntity;
import Entities.OrderEntity;
import Entities.SellerEntity;
import com.shades.exceptions.ShadesException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class InventoryDaoImpl implements InventoryDao {

    private static final Logger logger = Logger.getLogger(InventoryDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    public void updateInventory(List<InventoryEntity> itemsList) {

        logger.info("Updating inventory. " + itemsList.size() + " items changed.");

        Query query = em.createNativeQuery("" +
                "INSERT IGNORE INTO aws_db_shades1.Inventory(sku, supplierId, quantity, supplierProductId, supplierPrice, shadesSellingPrice, weight, shippingCost, lastUpdate, status)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE quantity = ?, supplierPrice = ?, shadesSellingPrice = ?, lastUpdate = ?, status = ?");

            itemsList.stream().forEach((entity) -> {
                query.setParameter(1, entity.getSku());
                query.setParameter(2, entity.getSupplierId());
                query.setParameter(3, entity.getQuantity());
                query.setParameter(4, entity.getSupplierProductId());
                query.setParameter(5, entity.getSupplierPrice());
                query.setParameter(6, entity.getShadesSellingPrice());
                query.setParameter(7, entity.getWeight());
                query.setParameter(8, entity.getShippingCost());
                query.setParameter(9, new Timestamp(System.currentTimeMillis()));
                query.setParameter(10, entity.getStatus());

                query.setParameter(11, entity.getQuantity());
                query.setParameter(12, entity.getSupplierPrice());
                query.setParameter(13, entity.getShadesSellingPrice());
                query.setParameter(14, entity.getLastUpdate());
                query.setParameter(15, entity.getStatus());

                query.executeUpdate();
                System.out.println(entity.getSku() + " - " + entity.getStatus());
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
    public void placeNewOrder(OrderEntity order) throws ShadesException {

        InventoryEntity item = findProductDetails(order.getSku());

        Query q =  em.createNativeQuery("INSERT IGNORE INTO Orders (" +
                "orderId, orderDate, sellerId, marketId, supplierId, marketOrderId, sku, marketListingId, asin, " +
                "quantity, buyerName, street, street2, city, state, other, zipCode, country, supplierPrice,\n" +
                "shadesPrice, shippingCost, totalPriceShades, marketSoldAmount, currency, observations)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE orderDate = ?, quantity = ?, buyerName = ? , street = ?, street2=?, " +
                "city = ?, state = ?, other = ?, zipCode = ?, country = ?, totalPriceShades = ?, observations = ?");

        q.setParameter(1, order.getOrderId());
        q.setParameter(2, new Timestamp(System.currentTimeMillis()));
        q.setParameter(3, order.getSellerId());
        q.setParameter(4, order.getMarketId());
        q.setParameter(5, item.getSupplierId()); //Supplier Id
        q.setParameter(6, order.getMarketOrderId());
        q.setParameter(7, order.getSku());
        q.setParameter(8, order.getMarketListingId());
        q.setParameter(9, order.getAsin());
        q.setParameter(10, order.getQuantity());
        q.setParameter(11, order.getBuyerName());
        q.setParameter(12, order.getStreet());
        q.setParameter(13, order.getStreet2());
        q.setParameter(14, order.getCity());
        q.setParameter(15, order.getState());
        q.setParameter(16, order.getOther());
        q.setParameter(17, order.getZipCode());
        q.setParameter(18, order.getCountry());
        q.setParameter(19, item.getSupplierPrice()); //Supplier price
        q.setParameter(20, item.getShadesSellingPrice()); //Shades price
        q.setParameter(21, item.getShippingCost()); //Shipping cost
        q.setParameter(22, (item.getShadesSellingPrice() * item.getQuantity()) + item.getShippingCost()); //Total Price Shades
        q.setParameter(23, order.getMarketSoldAmount());
        q.setParameter(24, order.getCurrency());
        q.setParameter(25, order.getObservations());

        q.setParameter(26, new Timestamp(System.currentTimeMillis()));
        q.setParameter(27, order.getQuantity());
        q.setParameter(28, order.getBuyerName());
        q.setParameter(29, order.getStreet());
        q.setParameter(30, order.getStreet2());
        q.setParameter(31, order.getCity());
        q.setParameter(32, order.getState());
        q.setParameter(33, order.getOther());
        q.setParameter(34, order.getZipCode());
        q.setParameter(35, order.getCountry());
        q.setParameter(36, (item.getShadesSellingPrice() * item.getQuantity()) + item.getShippingCost()); //Total Price Shades
        q.setParameter(37, order.getObservations());

        q.executeUpdate();
    }

    @Override
    public int getSellerId(String sellerName) throws ShadesException {

        try {
            Query q = em.createNativeQuery("SELECT * FROM Seller WHERE username = ?", SellerEntity.class);
            q.setParameter(1, sellerName);
            SellerEntity se = (SellerEntity) q.getSingleResult();
            return se.getSellerId();
        }catch (NoResultException nre){
            throw new ShadesException("No sellerName id found as " + sellerName);
        }

    }

    @Override
    public int getNextOrderId() throws ShadesException {
        try {
            Query q = em.createNativeQuery("SELECT max(orderId) from Orders;");
            int lastOrder = (int) q.getSingleResult();
            return lastOrder +1;
        }catch (NoResultException nre){
            throw new ShadesException("Error trying to retrieve max order id.");
        }
    }

    @Override
    public List<InventoryEntity> getProductsBySupplier(int supplierId) {
        Query q = em.createNativeQuery("SELECT * FROM Inventory WHERE supplierId = ?", InventoryEntity.class);
        q.setParameter(1, supplierId);
        List<InventoryEntity> list = q.getResultList();
        return list;
    }

    @Override
    public List<OrderEntity> getPendingOrdersBySeller(int sellerId) {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE sellerId = ? AND trackingId IS NULL", OrderEntity.class);
        q.setParameter(1, sellerId);
        List<OrderEntity> orders = q.getResultList();
        return orders;
    }

    @Override
    public List<OrderEntity> getCompletedOrdersBySeller(int sellerId) {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE sellerId = ? AND trackingId IS NOT NULL", OrderEntity.class);
        q.setParameter(1, sellerId);
        List<OrderEntity> orders = q.getResultList();
        return orders;
    }

    @Override
    public OrderEntity getOrderById(int orderId){
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE  orderId = ?", OrderEntity.class);
        q.setParameter(1, orderId);
        return (OrderEntity) q.getSingleResult();
    }

    @Override
    public List<OrderEntity> getAllNewOrders() {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE trackingId IS NULL AND processed = 0", OrderEntity.class);
        List<OrderEntity> orders = q.getResultList();
        return orders;
    }

    @Override
    public void updateOrder(String field, String value, String[] orderIds) {
        Query q = em.createNativeQuery("UPDATE Orders SET " + field + "=" + value + " WHERE orderId = ?", OrderEntity.class);

        for(String orderId: orderIds){
            q.setParameter(1, orderId);
            q.executeUpdate();
        }
    }

    @Override
    public List<OrderEntity> getStagedOrders() {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE trackingId IS NULL AND processed = 1", OrderEntity.class);
        List<OrderEntity> orders = q.getResultList();
        return orders;
    }

    @Override
    public void updateTrackingInfo(List<OrderEntity> orderList) {

        Query q = em.createNativeQuery("UPDATE Orders SET shippingCost = ?, trackingId = ?, totalPriceShades = ? WHERE orderId = ?", OrderEntity.class);

        for(OrderEntity order: orderList){
            OrderEntity temp = getOrderById(order.getOrderId());
            q.setParameter(1, order.getShippingCost());
            q.setParameter(2, order.getTrackingId());
            q.setParameter(3, temp.getShadesPrice() + order.getShippingCost());
            q.setParameter(4, order.getOrderId());
            q.executeUpdate();
        }
    }
}
