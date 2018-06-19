package com.shades.services.fragx.Models;

import java.net.URL;

/**
 * A Product class
 */
public class Product {
    /**
     * 6-digit Item #
     */
    private String ItemId;
    /**
     * Product type
     */
    private String Type;
    /**
     * Product size in ounce
     */
    private String Size;
    /**
     * Product size in metric
     */
    private String MetricSize;
    /**
     * Availability of the product, false if out-of-stock
     */
    private boolean Instock;
    /**
     * Retail price in US dollar
     */
    private double RetailPriceUSD;
    /**
     * Wholesale price in US dollar
     */
    private double WholesalePriceUSD;
    /**
     * Wholesale price in British Pound
     */
    private double WholesalePriceGBP;
    /**
     * Wholesale price in Euro
     */
    private double WholesalePriceEUR;
    /**
     * Wholesale price in Australian dollar
     */
    private double WholesalePriceAUD;
    /**
     * Wholesale price in Canadian dollar
     */
    private double WholesalePriceCAD;
    /**
     * Product parent code
     */
    private String ParentCode;
    /**
     * Product name
     */
    private String ProductName;
    /**
     * Brand
     */
    private String BrandName;
    /**
     * Gender
     */
    private String Gender;
    /**
     * Brief description of the product
     */
    private String Description;
    /**
     * Small sized product image url
     */
    private URL SmallImageUrl;
    /**
     * Large sized proudct image url
     */
    private URL LargeImageUrl;

    /**
     * Initializes a new Product object.
     * @param itemId 6-digit item #
     * @param type Product type
     * @param size Product size in ounce
     * @param metricSize Product size in metric
     * @param instock Availability of the product, false if out-of-stock
     * @param retailPriceUSD Retail price in US dollar
     * @param wholesalePriceUSD Wholesale price in US dollar
     * @param wholesalePriceGBP Wholesale price in British Pound
     * @param wholesalePriceEUR Wholesale price in Euro
     * @param wholesalePriceAUD Wholesale price in Australian dollar
     * @param wholesalePriceCAD Wholesale price in Canadian dollar
     * @param parentCode Product parent code
     * @param productName Product name
     * @param brandName Brand
     * @param gender Gender
     * @param description Brief description of the product
     * @param smallImageUrl Small sized product image url
     * @param largeImageUrl Large sized product image url
     */
    public Product(String itemId, String type, String size, String metricSize, boolean instock, double retailPriceUSD, double wholesalePriceUSD, double wholesalePriceGBP, double wholesalePriceEUR, double wholesalePriceAUD, double wholesalePriceCAD, String parentCode, String productName, String brandName, String gender, String description, URL smallImageUrl, URL largeImageUrl){
        this.ItemId = itemId;
        this.Type = type;
        this.Size = size;
        this.MetricSize = metricSize;
        this.Instock = instock;
        this.RetailPriceUSD = retailPriceUSD;
        this.WholesalePriceUSD = wholesalePriceUSD;
        this.WholesalePriceGBP = wholesalePriceGBP;
        this.WholesalePriceEUR = wholesalePriceEUR;
        this.WholesalePriceAUD = wholesalePriceAUD;
        this.WholesalePriceCAD = wholesalePriceCAD;
        this.ParentCode = parentCode;
        this.ProductName = productName;
        this.BrandName = brandName;
        this.Gender = gender;
        this.Description = description;
        this.SmallImageUrl = smallImageUrl;
        this.LargeImageUrl = largeImageUrl;
    }

    
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString()
    {
        return "ItemId : " + ItemId +
                "\nType : " + Type +
                "\nSize : " + Size +
                "\nMetricSize : " + MetricSize +
                "\nInstock : " + Instock +
                "\nRetailPriceUSD : " + RetailPriceUSD +
                "\nWholesalePriceUSD : " + WholesalePriceUSD +
                "\nWholesalePriceGBP : " + WholesalePriceGBP +
                "\nWholesalePriceEUR : " + WholesalePriceEUR +
                "\nWholesalePriceAUD : " + WholesalePriceAUD +
                "\nWholesalePriceCAD : " + WholesalePriceCAD +
                "\nParentCode : " + ParentCode +
                "\nProductName : " + ProductName +
                "\nBrandName : " + BrandName +
                "\nGender : " + Gender +
                "\nDescription : " + Description +
                "\nSmallImageUrl : " + SmallImageUrl +
                "\nLargeImageUrl : " + LargeImageUrl + "\n";
    }


    public String getItemId() {
        return ItemId;
    }

    public String getType() {
        return Type;
    }

    public String getSize() {
        return Size;
    }

    public String getMetricSize() {
        return MetricSize;
    }

    public boolean isInstock() {
        return Instock;
    }

    public double getRetailPriceUSD() {
        return RetailPriceUSD;
    }

    public double getWholesalePriceUSD() {
        return WholesalePriceUSD;
    }

    public double getWholesalePriceGBP() {
        return WholesalePriceGBP;
    }

    public double getWholesalePriceEUR() {
        return WholesalePriceEUR;
    }

    public double getWholesalePriceAUD() {
        return WholesalePriceAUD;
    }

    public double getWholesalePriceCAD() {
        return WholesalePriceCAD;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getBrandName() {
        return BrandName;
    }

    public String getGender() {
        return Gender;
    }

    public String getDescription() {
        return Description;
    }

    public URL getSmallImageUrl() {
        return SmallImageUrl;
    }

    public URL getLargeImageUrl() {
        return LargeImageUrl;
    }
}
