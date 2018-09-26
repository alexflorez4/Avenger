package Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by alexf on 8/27/2018.
 */
public class AsinEntityPK implements Serializable {
    private String sku;
    private String asin;

    @Column(name = "sku")
    @Id
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Column(name = "asin")
    @Id
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AsinEntityPK that = (AsinEntityPK) o;

        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;
        if (asin != null ? !asin.equals(that.asin) : that.asin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (asin != null ? asin.hashCode() : 0);
        return result;
    }
}
