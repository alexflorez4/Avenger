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


}
