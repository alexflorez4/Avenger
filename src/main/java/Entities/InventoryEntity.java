package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Inventory", schema = "aws_db_shades1", catalog = "")
@IdClass(InventoryEntityPK.class)
public class InventoryEntity {
    private String sku;
    private int supplierId;
    private Integer quantity;
    private String supplierProductId;
    private Double supplierPrice;
    private Double shadesSellingPrice;
    private Double weight;
    private Double shippingCost;
    private Timestamp lastUpdate;
    private String status;
    private Double suggestedPrice;

    public InventoryEntity() {
    }

    public InventoryEntity(String sku, Integer quantity, int supplierId, Double supplierPrice, Double shadesSellingPrice, Double shippingCost, String status, Timestamp lastUpdate, Double weight, String supplierProductId) {
        this.sku = sku;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.supplierPrice = supplierPrice;
        this.shadesSellingPrice = shadesSellingPrice;
        this.shippingCost = shippingCost;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.weight = weight;
        this.supplierProductId = supplierProductId;
    }

    @Id
    @Column(name = "sku")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Id
    @Column(name = "supplierId")
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "supplierProductId")
    public String getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(String supplierProductId) {
        this.supplierProductId = supplierProductId;
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
    @Column(name = "shadesSellingPrice")
    public Double getShadesSellingPrice() {
        return shadesSellingPrice;
    }

    public void setShadesSellingPrice(Double shadesSellingPrice) {
        this.shadesSellingPrice = shadesSellingPrice;
    }

    @Basic
    @Column(name = "weight")
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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
    @Column(name = "lastUpdate")
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "suggestedPrice")
    public Double getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(Double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    @Override
    public String toString() {
        return  "sku: " + sku + " - Supplier Id:" + supplierId + " - Quantity: " + quantity +
                " - Supplier Product Id:" + supplierProductId + " - Supplier Price: " + supplierPrice +
                " - Shades Selling Price: " + shadesSellingPrice + "  Weight: " + weight +
                " - Shipping cost: " + shippingCost + " - last Update:" + lastUpdate + " - Status " + status;
    }

}
