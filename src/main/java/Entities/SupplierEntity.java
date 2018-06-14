package Entities;

import javax.persistence.*;

/**
 * Created by alexf on 6/11/2018.
 */
@Entity
@Table(name = "Supplier", schema = "aws_db_shades1", catalog = "")
public class SupplierEntity {
    private int supplierId;
    private String supplierName;

    public SupplierEntity() {
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
    @Column(name = "supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierEntity that = (SupplierEntity) o;

        if (supplierId != that.supplierId) return false;
        if (supplierName != null ? !supplierName.equals(that.supplierName) : that.supplierName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplierId;
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        return result;
    }
}
