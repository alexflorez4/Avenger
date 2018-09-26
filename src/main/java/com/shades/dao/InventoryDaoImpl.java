package com.shades.dao;

import Entities.AsinEntity;
import Entities.InventoryEntity;
import Entities.OrderEntity;
import Entities.SellerEntity;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.shades.exceptions.ShadesException;
import com.shades.utilities.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class InventoryDaoImpl implements InventoryDao {

    private static final Logger logger = Logger.getLogger(InventoryDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    public void updateInventory(List<InventoryEntity> itemsList) {

        logger.info("Updating inventory. " + itemsList.size() + " items changed.");

        Query query = em.createNativeQuery("" +
                "INSERT IGNORE INTO aws_db_shades1.Inventory(sku, supplierId, quantity, supplierProductId, supplierPrice, shadesSellingPrice, weight, shippingCost, lastUpdate, status, suggestedPrice)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE quantity = ?, supplierPrice = ?, shadesSellingPrice = ?, lastUpdate = ?, status = ?, suggestedPrice = ?");

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
                query.setParameter(11, entity.getSuggestedPrice());

                query.setParameter(12, entity.getQuantity());
                query.setParameter(13, entity.getSupplierPrice());
                query.setParameter(14, entity.getShadesSellingPrice());
                query.setParameter(15, entity.getLastUpdate());
                query.setParameter(16, entity.getStatus());
                query.setParameter(17, entity.getSuggestedPrice());

                query.executeUpdate();
                System.out.println(entity.getSku() + " - " + entity.getStatus());
            }
        );
    }

    @Override
    public List<String> getAllSKUs() {
        Query q = em.createNativeQuery("SELECT sku FROM Inventory");
        return q.getResultList();
    }

    @Override
    public List<InventoryEntity> getAllProducts() {
        Query q = em.createNativeQuery("SELECT * FROM Inventory WHERE blocked = 0 OR blocked IS NULL;", InventoryEntity.class);
        return q.getResultList();
    }

    @Override
    public List<AsinEntity> getAllAsin(){
        Query q = em.createNativeQuery("SELECT * FROM Asin  WHERE blocked = 0 OR blocked IS NULL", AsinEntity.class);
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
    public List<OrderEntity> checkPriorBuyer(String buyerName, String buyerAddress, int currentOrder) {
        System.out.println(buyerName + " - " + currentOrder);
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE  (buyerName LIKE ? OR street LIKE ?) AND orderId <> ?", OrderEntity.class);
        q.setParameter(1, buyerName);
        q.setParameter(2, buyerAddress);
        q.setParameter(3, currentOrder);
        return q.getResultList();
    }

    @Override
    public void placeNewOrder(OrderEntity order) throws ShadesException {

        /** Begin: Check for inventory */
        InventoryEntity item = findProductDetails(order.getSku());
        if(item.getQuantity() < 1){
            throw new ShadesException("Sku " + item.getSku() + " is out of stock");
        }

        /** Begin: Check for recurrent buyers*/
        List<OrderEntity> matchingOrders = checkPriorBuyer("%"+ StringUtils.trim(order.getBuyerName())+"%",
                "%"+StringUtils.trim(order.getStreet())+"%", order.getOrderId());
        if(matchingOrders.size() > 0){
            Set<String> newStringSet = new HashSet<>();

            String orderObs = StringUtils.isNotBlank(order.getWarning()) ? order.getWarning() : StringUtils.EMPTY;
            //orderObs = StringUtils.replace(orderObs, "WARNING CHECK ORDER No. ", StringUtils.EMPTY);
            List<String> results = Splitter.on("-").trimResults().omitEmptyStrings().splitToList(orderObs);
            results.stream().forEach(s -> newStringSet.add(s));
            matchingOrders.stream().forEach(s -> newStringSet.add(String.valueOf(s.getOrderId())));

            String updatedObservations = Joiner.on(" - ").skipNulls().join(newStringSet);

            /*StringBuilder sb = new StringBuilder("WARNING CHECK ORDER No. ");
            for(OrderEntity tempOrder : matchingOrders){
                sb.append(tempOrder.getOrderId() + " - ");
            }*/
            order.setWarning(updatedObservations);
        }

        Double itemWeight = item.getWeight() == null ? 0.0 : item.getWeight();
        Double shipping = Utils.calculateShippingCost(item.getSupplierId(), order.getQuantity(), itemWeight);
        order.setShippingCost(shipping);

        Query q =  em.createNativeQuery("INSERT IGNORE INTO Orders (" +
                "orderId, orderDate, sellerId, marketId, supplierId, marketOrderId, sku, marketListingId, asin, " +
                "quantity, buyerName, street, street2, city, state, zipCode, country, supplierPrice,\n" +
                "shadesPrice, shippingService, shippingCost, totalPriceShades, marketSoldAmount, currency, observations, sellerName, warning)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE quantity = ?, buyerName = ? , street = ?, street2=?, " +
                "city = ?, state = ?, shippingService = ?, zipCode = ?, country = ?, totalPriceShades = ?, observations = ?, shippingCost = ?");

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
        q.setParameter(16, order.getZipCode());
        q.setParameter(17, order.getCountry());
        q.setParameter(18, item.getSupplierPrice()); //Supplier price
        q.setParameter(19, item.getShadesSellingPrice()); //Shades price
        q.setParameter(20, order.getShippingService());
        q.setParameter(21, order.getShippingCost()); //Shipping cost
        q.setParameter(22, (item.getShadesSellingPrice() * order.getQuantity()) + item.getShippingCost()); //Total Price Shades
        q.setParameter(23, order.getMarketSoldAmount());
        q.setParameter(24, order.getCurrency());
        q.setParameter(25, order.getObservations());
        q.setParameter(26, order.getSellerName());
        q.setParameter(27, order.getWarning());

        q.setParameter(28, order.getQuantity());
        q.setParameter(29, order.getBuyerName());
        q.setParameter(30, order.getStreet());
        q.setParameter(31, order.getStreet2());
        q.setParameter(32, order.getCity());
        q.setParameter(33, order.getState());
        q.setParameter(34, order.getShippingService());
        q.setParameter(35, order.getZipCode());
        q.setParameter(36, order.getCountry());
        q.setParameter(37, (item.getShadesSellingPrice() * order.getQuantity()) + item.getShippingCost()); //Total Price Shades
        q.setParameter(38, order.getObservations());
        q.setParameter(39, order.getShippingCost());

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
    public List<OrderEntity> getAllCompletedOrders() {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE trackingId IS NOT NULL", OrderEntity.class);
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
    public List<OrderEntity> getOrdersForInvoice(String startDate, String endDate) {
        Query q = em.createNativeQuery("SELECT * FROM Orders WHERE trackingId IS NOT NULL AND orderDate BETWEEN ? AND ?", OrderEntity.class);
        q.setParameter(1, startDate);
        q.setParameter(2, endDate);
        List<OrderEntity> orders = q.getResultList();
        return orders;
    }

    @Override
    public void updateTrackingInfo(List<OrderEntity> orderList) {

        orderList.stream().forEach(order -> {
            OrderEntity dbOrder = getOrderById(order.getOrderId());
            if(order.getSupplierOrderId() == null && dbOrder.getSupplierOrderId() != null){
                order.setSupplierOrderId(dbOrder.getSupplierOrderId());
            }
            if(order.getTrackingId() == null && dbOrder.getTrackingId() != null){
                order.setTrackingId(dbOrder.getTrackingId());
            }

            if(order.getSupplierPrice() == null && dbOrder.getSupplierPrice() != null){
                order.setSupplierPrice(dbOrder.getSupplierPrice());
            }

            if(order.getShippingCost() == null && dbOrder.getShippingCost() != null){
                order.setShippingCost(dbOrder.getShippingCost());
            }else if(order.getShippingCost() == null){
                order.setShippingCost(0.0);
            }

            order.setShadesPrice(Utils.shadesPrices(dbOrder.getSupplierId(), order.getSupplierPrice()));
            order.setTotalPriceShades(order.getShadesPrice() + order.getShippingCost());
        });

        Query q = em.createNativeQuery("UPDATE Orders SET supplierOrderId=?, trackingId=?, shippingCost=?, supplierPrice=?," +
                "shadesPrice=?, totalPriceShades=? WHERE orderId = ?");

        orderList.stream().forEach(order -> {
            q.setParameter(1, order.getSupplierOrderId());
            q.setParameter(2, order.getTrackingId());
            q.setParameter(3, order.getShippingCost());
            q.setParameter(4, order.getSupplierPrice());
            q.setParameter(5, order.getShadesPrice());
            q.setParameter(6, order.getTotalPriceShades());
            q.setParameter(7, order.getOrderId());
            q.executeUpdate();
        });
    }

    public void insertAsin(List<AsinEntity> asins){
        Query q = em.createNativeQuery("INSERT IGNORE INTO Asin (sku, asin) VALUES (?, ?)");

        asins.stream().forEach((asinEntity -> {
            q.setParameter(1, asinEntity.getSku());
            q.setParameter(2, asinEntity.getAsin());
            q.executeUpdate();
        })
        );
    }
}
