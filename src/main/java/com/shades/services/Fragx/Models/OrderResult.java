package com.shades.services.fragx.Models;

import java.util.List;

/**
 * An OrderResult class
 */
/*
An OrderResult class.
DataField:
    String orderId : Order Id
    double grandTotal :
 */
public class OrderResult {
    /**
     * Order Id
     */
    private String OrderId;
    /**
     * Sum of SubTotal and ShippingCharge
     */
    private double GrandTotal;
    /**
     * Total items price
     */
    private double SubTotal;
    /**
     * Shipping cost
     */
    private double ShippingCharge;
    /**
     * Result code for placing an order
     */
    private ResultCode ResultCode;
    /**
     * Result message for placing an order
     */
    private String Message;
    /**
     * Array of OrderResultItem object
     */
    private List<OrderResultItem> OrderResultItems;

    /**
     * Initializes a new OrderResult object.
     * @param OrderId Order Id
     * @param GrandTotal Sum of SubTotal and ShippingCharge
     * @param SubTotal Total items price
     * @param ShippingCharge Shipping cost
     * @param ResultCode Result code for placing an order
     * @param Message Result message for placing an order
     * @param OrderResultItems Array of OrderResultItem object
     */
    public OrderResult(String OrderId, double GrandTotal, double SubTotal, double ShippingCharge, ResultCode ResultCode, String Message, List<OrderResultItem> OrderResultItems) {
        this.OrderId = OrderId;
        this.GrandTotal = GrandTotal;
        this.SubTotal = SubTotal;
        this.ShippingCharge = ShippingCharge;
        this.ResultCode = ResultCode;
        this.Message = Message;
        this.OrderResultItems = OrderResultItems;
    }
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */    public String toString(){
        String orderResultList = "";
        for (OrderResultItem a : OrderResultItems)
        {
            orderResultList += a.toString();
        }
        return "OrderId : " + OrderId + "\nGrand Total : " + GrandTotal + "\nSub Total : " + SubTotal + "\nShipping Charge : " + ShippingCharge + "\nResult Code : " + ResultCode + "\nMessage : " + Message + "\nOrder Result Items : " + orderResultList + "\n";

    }
}
