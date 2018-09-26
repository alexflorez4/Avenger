package Entities;

import javax.persistence.*;


@Entity
@Table(name = "Asin", schema = "aws_db_shades1", catalog = "")
@IdClass(AsinEntityPK.class)
public class AsinEntity {
    private String sku;
    private String asin;

    public AsinEntity() {
    }

    public AsinEntity(String sku, String asin) {
        this.sku = sku;
        this.asin = asin;
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
    @Column(name = "asin")
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }




}
