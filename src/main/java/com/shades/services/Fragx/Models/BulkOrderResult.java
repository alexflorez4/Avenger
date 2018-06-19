package com.shades.services.fragx.Models;

import java.util.List;

/**
 * A BulkOrderResult class
 */
public class BulkOrderResult {
    /**
     * Bulk order id
     */
    private String BulkOrderId;
    /**
     * Order total
     */
    private double BulkOrderTotal;
    /**
     * An array of OrderResult objects
     */
    private List<OrderResult> OrderResults;
    /**
     * Result of the bulk order
     */
    private String Message;

    /**
     * Initializes a new BulkOrderResult object.
     * @param BulkOrderId Bulk order id
     * @param BulkOrderTotal Order total
     * @param OrderResults An array of OrderResult objects
     * @param Message Result of the bulk order
     */
    public BulkOrderResult(String BulkOrderId, double BulkOrderTotal, List<OrderResult> OrderResults, String Message) {
        this.BulkOrderId = BulkOrderId;
        this.BulkOrderTotal = BulkOrderTotal;
        this.OrderResults = OrderResults;
        this.Message = Message;
    }

    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        String orderResultList = "";
        for (OrderResult parts : OrderResults) {
            orderResultList += "\n" + parts.toString();
        }
        return "BulkOrderId : " + BulkOrderId + "\nTotal : " + BulkOrderTotal + "\nOrder Results : " + orderResultList + "Message : " + Message + "\n";
    }
}
