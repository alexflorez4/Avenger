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
    private Float supplierPrice;
    private Float shadesSellingPrice;
    private Float weight;
    private Float shippingCost;
    private Timestamp lastUpdate;

    public InventoryEntity() {
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
    public Float getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(Float supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    @Basic
    @Column(name = "shadesSellingPrice")
    public Float getShadesSellingPrice() {
        return shadesSellingPrice;
    }

    public void setShadesSellingPrice(Float shadesSellingPrice) {
        this.shadesSellingPrice = shadesSellingPrice;
    }

    @Basic
    @Column(name = "weight")
    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "shippingCost")
    public Float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Float shippingCost) {
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

    @Override
    public String toString() {
        return  "sku: " + sku + " - Supplier Id:" + supplierId + " - Quantity: " + quantity +
                " - Supplier Product Id:" + supplierProductId + " - Supplier Price: " + supplierPrice +
                " - Shades Selling Price: " + shadesSellingPrice + "  Weight: " + weight +
                " - Shipping cost: " + shippingCost + " - last Update:" + lastUpdate;
    }
}
