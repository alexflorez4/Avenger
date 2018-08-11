package Entities;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "Orders", schema = "aws_db_shades1")
public class OrderEntity {

    private int orderId;
    private Timestamp orderDate;
    private int sellerId;
    private String sellerName;
    private Integer marketId;
    private Integer supplierId;
    private String marketOrderId;
    private String supplierOrderId;
    private String sku;
    private String marketListingId;
    private String asin;
    private int quantity;
    private String buyerName;
    private String street;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String trackingId;
    private Double supplierPrice;
    private Double shadesPrice;
    private String shippingService;
    private Double shippingCost;
    private Double totalPriceShades;
    private Double marketSoldAmount;
    private String currency;
    private String observations;
    private int processed;


    public OrderEntity() {
    }


    public OrderEntity(OrderEntity order) {
        this.orderId = order.getOrderId();
        this.sellerId = order.getSellerId();
        this.sellerName = order.getSellerName();
        this.marketId = order.getMarketId();
        this.marketOrderId = order.getMarketOrderId();
        this.buyerName = order.getBuyerName();
        this.street = order.getStreet();
        this.street2 = order.getStreet2();
        this.city = order.getCity();
        this.state = order.getState();
        this.zipCode = order.getZipCode();
        this.country = order.getCountry();
        this.shippingService = order.getShippingService();
    }

    @Id
    @Column(name = "orderId")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "orderDate")
    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "sellerId")
    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    @Basic
    @Column(name = "sellerName")
    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Basic
    @Column(name = "marketId")
    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    @Basic
    @Column(name = "supplierId")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "marketOrderId")
    public String getMarketOrderId() {
        return marketOrderId;
    }

    public void setMarketOrderId(String marketOrderId) {
        this.marketOrderId = marketOrderId;
    }

    @Basic
    @Column(name = "supplierOrderId")
    public String getSupplierOrderId() {
        return supplierOrderId;
    }

    public void setSupplierOrderId(String supplierOrderId) {
        this.supplierOrderId = supplierOrderId;
    }

    @Basic
    @Column(name = "sku")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Basic
    @Column(name = "marketListingId")
    public String getMarketListingId() {
        return marketListingId;
    }

    public void setMarketListingId(String marketListingId) {
        this.marketListingId = marketListingId;
    }

    @Basic
    @Column(name = "asin")
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @Basic
    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "buyerName")
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Basic
    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "street2")
    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "shippingService")
    public String getShippingService() {
        return shippingService;
    }

    public void setShippingService(String other) {
        this.shippingService = other;
    }

    @Basic
    @Column(name = "zipCode")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "trackingId")
    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @Basic
    @Column(name = "supplierPrice")
    public Double getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(Double supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    @Basic
    @Column(name = "shadesPrice")
    public Double getShadesPrice() {
        return shadesPrice;
    }

    public void setShadesPrice(Double shadesPrice) {
        this.shadesPrice = shadesPrice;
    }

    @Basic
    @Column(name = "shippingCost")
    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    @Basic
    @Column(name = "totalPriceShades")
    public Double getTotalPriceShades() {
        return totalPriceShades;
    }

    public void setTotalPriceShades(Double totalPriceShades) {
        this.totalPriceShades = totalPriceShades;
    }

    @Basic
    @Column(name = "marketSoldAmount")
    public Double getMarketSoldAmount() {
        return marketSoldAmount;
    }

    public void setMarketSoldAmount(Double marketSoldAmount) {
        this.marketSoldAmount = marketSoldAmount;
    }

    @Basic
    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Basic
    @Column(name = "observations")
    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Basic
    @Column(name = "processed")
    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int proccessed) {
        this.processed = proccessed;
    }

    @Override
    public String toString() {
        return  "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", sellerId=" + sellerId +
                ", marketId=" + marketId +
                ", supplierId=" + supplierId +
                ", marketOrderId='" + marketOrderId + '\'' +
                ", supplierOrderId='" + supplierOrderId + '\'' +
                ", sku='" + sku + '\'' +
                ", marketListingId='" + marketListingId + '\'' +
                ", asin='" + asin + '\'' +
                ", quantity=" + quantity +
                ", buyerName='" + buyerName + '\'' +
                ", street='" + street + '\'' +
                ", street2='" + street2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", other='" + shippingService + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", supplierPrice=" + supplierPrice +
                ", shadesPrice=" + shadesPrice +
                ", shippingCost=" + shippingCost +
                ", totalPriceShades=" + totalPriceShades +
                ", marketSoldAmount=" + marketSoldAmount +
                ", currency='" + currency + '\'' +
                ", observations='" + observations + '\'' +
                '}';
    }
}
