package Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class InventoryEntityPK implements Serializable {
    private String sku;
    private int supplierId;

    public InventoryEntityPK() {
    }

    @Column(name = "sku")
    @Id
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Column(name = "supplierId")
    @Id
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryEntityPK that = (InventoryEntityPK) o;

        if (supplierId != that.supplierId) return false;
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + supplierId;
        return result;
    }
}
