package Entities;

import javax.persistence.*;

/**
 * Created by alexf on 6/11/2018.
 */
@Entity
@Table(name = "Market", schema = "aws_db_shades1", catalog = "")
public class MarketEntity {
    private int marketId;
    private String marketName;

    public MarketEntity() {
    }

    @Id
    @Column(name = "marketId")
    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    @Basic
    @Column(name = "marketName")
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketEntity that = (MarketEntity) o;

        if (marketId != that.marketId) return false;
        if (marketName != null ? !marketName.equals(that.marketName) : that.marketName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = marketId;
        result = 31 * result + (marketName != null ? marketName.hashCode() : 0);
        return result;
    }
}
