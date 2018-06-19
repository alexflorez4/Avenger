package com.shades.services.fragx.Models;

/**
 * An OrderItem class
 */
public class OrderItem {
    /**
     * 6-digit Item #
     */
    private String ItemId;
    /**
     * Number of item ordered
     */
    private int Quantity;

    /**
     * Initializes a new OrderItem object.
     * @param ItemId 6-digit Item #
     * @param Quantity Number of item ordered
     */
    public OrderItem(String ItemId, int Quantity){
        this.ItemId = ItemId;
        this.Quantity = Quantity;
    }
    
    public OrderItem(){
    }
    
    
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        return "Item Id : " + ItemId + "\nQuantity : " + Quantity + "\n";
    }
    
    public void setItemId(String ItemId){
    	this.ItemId = ItemId;
    }
    
    public void setQuantity(int Quantity){
    	this.Quantity = Quantity;
    }
}
