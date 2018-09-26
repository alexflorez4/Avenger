package com.shades.model;


public class AsinItem {

    private String sku;
    private String productIdAsin;
    private int productIdType;
    private double price;
    private String minimumSellerAllowedPrice;
    private String maximumSellerAllowedPrice;
    private int itemCondition;
    private int quantity;
    private char addDelete;
    private char willShipInternationally;
    private int handlingTime;

    public AsinItem(String sku, String productIdAsin, int productIdType, double price, String minimumSellerAllowedPrice,
                    String maximumSellerAllowedPrice, int itemCondition, int quantity, char addDelete,
                    char willShipInternationally, int handlingTime) {
        this.sku = sku;
        this.productIdAsin = productIdAsin;
        this.productIdType = productIdType;
        this.price = price;
        this.minimumSellerAllowedPrice = minimumSellerAllowedPrice;
        this.maximumSellerAllowedPrice = maximumSellerAllowedPrice;
        this.itemCondition = itemCondition;
        this.quantity = quantity;
        this.addDelete = addDelete;
        this.willShipInternationally = willShipInternationally;
        this.handlingTime = handlingTime;
    }

    public String getSku() {
        return sku;
    }

    public String getProductIdAsin() {
        return productIdAsin;
    }

    public int getProductIdType() {
        return productIdType;
    }

    public double getPrice() {
        return price;
    }

    public String getMinimumSellerAllowedPrice() {
        return minimumSellerAllowedPrice;
    }

    public String getMaximumSellerAllowedPrice() {
        return maximumSellerAllowedPrice;
    }

    public int getItemCondition() {
        return itemCondition;
    }

    public int getQuantity() {
        return quantity;
    }

    public char getAddDelete() {
        return addDelete;
    }

    public char getWillShipInternationally() {
        return willShipInternationally;
    }

    public int getHandlingTime() {
        return handlingTime;
    }
}
