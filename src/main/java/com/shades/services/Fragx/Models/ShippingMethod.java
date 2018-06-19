package com.shades.services.fragx.Models;

/**
 * Shipping Methods that can be used
 */
public enum ShippingMethod{
    /**
     * Ship by ground (0)
     */
    GROUND(0),
    /**
     * Ship by second day air (1)
     */
    SECOND_DAY_AIR(1),
    /**
     * Ship by international standard (3)
     */
    INTERNATIONAL_STANDARD(3),
    /**
     * Ship by international premium (4)
     */
    INTERNATIONAL_PREMIUM(4);
    /**
     * Value of ShippingMethod
     */
    private int value;

    /**
     * Defines a ShippingMethod
     * @param value Value to be assigned for ShippingMethod
     */
    ShippingMethod(int value){
        this.value = value;
    }
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    @Override
    public String toString(){
        switch(this){
            case GROUND : return "0";
            case SECOND_DAY_AIR: return "1";
            case INTERNATIONAL_STANDARD: return "3";
            case INTERNATIONAL_PREMIUM: return "4";
            default: return "-1";
        }
    }
}
