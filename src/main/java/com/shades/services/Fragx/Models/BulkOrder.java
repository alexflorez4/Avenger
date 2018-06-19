package com.shades.services.fragx.Models;

import java.util.List;

/**
 * Bulk Order class
 */

public class BulkOrder {
    /**
     * A list of Order objects
     */
    private List<Order> Orders;
    /**
     * If true, user will have to specify a billing address.  If false, the service will use default billing address
     * of the account.
     */
    private boolean BillingInfoSpecified;

    /**
     * Initializes a new BulkOrder object
     * @param Orders A list of Order objects.
     * @param BillingInfoSpecified If true, user will have to specify a billing addrses.  If false, the service will use default billing
     *                             address of the account.
     */
    public BulkOrder(List<Order> Orders, boolean BillingInfoSpecified){
        this.Orders = Orders;
        this.BillingInfoSpecified = BillingInfoSpecified;
    }
    
    public BulkOrder(){
    }
    
    
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        String orderPart = "";
        for (Order stringPart : Orders){
            orderPart += "\n" + stringPart.toString();
        }
        return "Orders : " + orderPart + "\nBilling Info Specified : " + BillingInfoSpecified + "\n";
    }

    public List<Order> getOrders() {
        return Orders;
    }
    
    public void setOrders(List<Order> Orders){
    	this.Orders = Orders;
    }
    
    public void setBillingInfoSpecified(boolean BillingInfoSpecified){
    	this.BillingInfoSpecified = BillingInfoSpecified;
    }
    

}
