package com.shades.services.fragx.Models;

/**
 * An OrderResultItem class
 */
public class OrderResultItem {
    /**
     * Item price
     */
    private double UnitPrice;
    /**
     * Product name
     */
    private String Name;

    /**
     * Initializes a new OrderResultItem object.
     * @param UnitPrice Item price
     * @param Name Product name
     */
    public OrderResultItem(double UnitPrice, String Name) {
        this.UnitPrice = UnitPrice;
        this.Name = Name;
    }
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        return "Unit Price : " + UnitPrice + "\nName : " + Name + "\n";
    }
}
