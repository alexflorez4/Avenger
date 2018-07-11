package Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


public class SellerEntityPK implements Serializable {
    private String username;
    private int sellerId;

    @Column(name = "username")
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "sellerId")
    @Id
    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SellerEntityPK that = (SellerEntityPK) o;

        if (sellerId != that.sellerId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + sellerId;
        return result;
    }
}
