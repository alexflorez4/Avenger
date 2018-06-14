package Entities;

import javax.persistence.*;


@Entity
@Table(name = "Seller", schema = "aws_db_shades1", catalog = "")
public class SellerEntity {
    private int sellerId;
    private String name;
    private String email;

    public SellerEntity() {
    }

    public SellerEntity(int sellerId, String name, String email) {
        this.sellerId = sellerId;
        this.name = name;
        this.email = email;
    }

    @Id
    @Column(name = "sellerId")
    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SellerEntity that = (SellerEntity) o;

        if (sellerId != that.sellerId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sellerId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
