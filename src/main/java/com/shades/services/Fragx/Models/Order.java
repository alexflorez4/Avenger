package com.shades.services.fragx.Models;

import java.util.List;

/**
 * An Order class
 */
public class Order {
	/**
	 * A ShippingAddress object that contains shipping address information.
	 */
	private ShippingAddress ShippingAddress;
	/**
	 * An enum type describing the shipping method.
	 */
	private ShippingMethod ShippingMethod;
	/**
	 * External order ID
	 */
	private String ReferenceId;
	/**
	 * If true, order is dropship.
	 */
	private boolean IsDropship;
	/**
	 * If true, order is gift wrapped.
	 */
	private boolean IsGiftWrapped;
	/**
	 * Message included with gift.
	 */
	private String GiftWrapMessage;
	/**
	 * A list of OrderItem objects.
	 */
	private List<OrderItem> OrderItems;

	/**
	 * Initializes a new Order object.
	 * 
	 * @param ShippingAddress
	 *            A Shipping Address object that contains shipping address
	 *            information
	 * @param ShippingMethod
	 *            An enum type describing the shipping method
	 * @param OrderItems
	 *            A list of OrderItem objects
	 * @param ReferenceId
	 *            External order ID
	 * @param IsDropship
	 *            If true, order is dropship.
	 * @param IsGiftWrapped
	 *            If true, order is gift wrapped
	 * @param GiftWrapMessage
	 *            Message included with gift
	 */
	public Order(ShippingAddress ShippingAddress, ShippingMethod ShippingMethod, List<OrderItem> OrderItems,
			String ReferenceId, boolean IsDropship, boolean IsGiftWrapped, String GiftWrapMessage) {
		this.ShippingAddress = ShippingAddress;
		this.ShippingMethod = ShippingMethod;
		this.OrderItems = OrderItems;
		this.ReferenceId = ReferenceId;
		this.IsDropship = IsDropship;
		this.IsGiftWrapped = IsGiftWrapped;
		this.GiftWrapMessage = GiftWrapMessage;
	}
	
	public Order() {
	}

	/**
	 * Return the object as a string.
	 * 
	 * @return A string containing an order's properties.
	 */
	public String toString() {
		String orderItemList = "";
		for (OrderItem part : OrderItems) {
			orderItemList += "\n" + part.toString();
		}
		return "Shipping Address : " + ShippingAddress + "\nShipping Method : " + ShippingMethod + "\nReferenceId : "
				+ ReferenceId + "\nIs Dropship : " + IsDropship + "\nIs Gift Wrapped : " + IsGiftWrapped
				+ "\nGift Wrap Message : " + GiftWrapMessage + "\nOrder Items : " + orderItemList + "\n";
	}

	public void setShippingAddress(ShippingAddress ShippingAddress) {
		this.ShippingAddress = ShippingAddress;
	}

	public void setShippingMethod(ShippingMethod ShippingMethod) {
		this.ShippingMethod = ShippingMethod;
	}

	public void setReferenceId(String ReferenceId) {
		this.ReferenceId = ReferenceId;
	}

	public void setIsDropship(boolean IsDropship) {
		this.IsDropship = IsDropship;
	}

	public void setIsGiftWrapped(boolean IsGiftWrapped) {
		this.IsGiftWrapped = IsGiftWrapped;
	}

	public void setGiftWrapMessage(String GiftWrapMessage) {
		this.GiftWrapMessage = GiftWrapMessage;
	}

	public void setOrderItems(List<OrderItem> OrderItems) {
		this.OrderItems = OrderItems;
	}

	public List<OrderItem> getOrderItems() {
		return OrderItems;
	}

}
