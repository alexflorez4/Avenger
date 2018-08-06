package Entities;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "Orders", schema = "aws_db_shades1")
public class OrderEntity {
    private int orderId;
    private Timestamp orderDate;
    private int sellerId;
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
    private String other;
    private String zipCode;
    private String country;
    private String trackingId;
    private Double supplierPrice;
    private Double shadesPrice;
    private Double shippingCost;
    private Double totalPriceShades;
    private Double marketSoldAmount;
    private String currency;
    private String observations;
    private int processed;

    public OrderEntity() {
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
    @Column(name = "other")
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        if (orderId != that.orderId) return false;
        if (sellerId != that.sellerId) return false;
        if (quantity != that.quantity) return false;
        if (orderDate != null ? !orderDate.equals(that.orderDate) : that.orderDate != null) return false;
        if (marketId != null ? !marketId.equals(that.marketId) : that.marketId != null) return false;
        if (supplierId != null ? !supplierId.equals(that.supplierId) : that.supplierId != null) return false;
        if (marketOrderId != null ? !marketOrderId.equals(that.marketOrderId) : that.marketOrderId != null)
            return false;
        if (supplierOrderId != null ? !supplierOrderId.equals(that.supplierOrderId) : that.supplierOrderId != null)
            return false;
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;
        if (marketListingId != null ? !marketListingId.equals(that.marketListingId) : that.marketListingId != null)
            return false;
        if (asin != null ? !asin.equals(that.asin) : that.asin != null) return false;
        if (buyerName != null ? !buyerName.equals(that.buyerName) : that.buyerName != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (street2 != null ? !street2.equals(that.street2) : that.street2 != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (other != null ? !other.equals(that.other) : that.other != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        if (trackingId != null ? !trackingId.equals(that.trackingId) : that.trackingId != null) return false;
        if (supplierPrice != null ? !supplierPrice.equals(that.supplierPrice) : that.supplierPrice != null)
            return false;
        if (shadesPrice != null ? !shadesPrice.equals(that.shadesPrice) : that.shadesPrice != null) return false;
        if (shippingCost != null ? !shippingCost.equals(that.shippingCost) : that.shippingCost != null) return false;
        if (totalPriceShades != null ? !totalPriceShades.equals(that.totalPriceShades) : that.totalPriceShades != null)
            return false;
        if (marketSoldAmount != null ? !marketSoldAmount.equals(that.marketSoldAmount) : that.marketSoldAmount != null)
            return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (observations != null ? !observations.equals(that.observations) : that.observations != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + sellerId;
        result = 31 * result + (marketId != null ? marketId.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        result = 31 * result + (marketOrderId != null ? marketOrderId.hashCode() : 0);
        result = 31 * result + (supplierOrderId != null ? supplierOrderId.hashCode() : 0);
        result = 31 * result + (sku != null ? sku.hashCode() : 0);
        result = 31 * result + (marketListingId != null ? marketListingId.hashCode() : 0);
        result = 31 * result + (asin != null ? asin.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (buyerName != null ? buyerName.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (street2 != null ? street2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (other != null ? other.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (trackingId != null ? trackingId.hashCode() : 0);
        result = 31 * result + (supplierPrice != null ? supplierPrice.hashCode() : 0);
        result = 31 * result + (shadesPrice != null ? shadesPrice.hashCode() : 0);
        result = 31 * result + (shippingCost != null ? shippingCost.hashCode() : 0);
        result = 31 * result + (totalPriceShades != null ? totalPriceShades.hashCode() : 0);
        result = 31 * result + (marketSoldAmount != null ? marketSoldAmount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (observations != null ? observations.hashCode() : 0);
        return result;
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
                ", other='" + other + '\'' +
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
