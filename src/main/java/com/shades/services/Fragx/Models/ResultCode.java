package com.shades.services.fragx.Models;

/**
 * Result Codes that can be used
 */
public enum ResultCode {
    /**
     * Order failed (0)
     */
    FAILED(0),
    /**
     * Order successful (1)
     */
    SUCCESS(1),
    /**
     * Order successful, but warning has occured (2)
     */
    SUCCESS_WITH_WARNING(2);

    /**
     * Value of ResultCode
     */
    private int value;

    /**
     * Defines a ResultCode.
     * @param value Value to be assigned to ResultCode
     */
    ResultCode(int value){
        this.value = value;
    }
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    @Override
    public String toString(){
        switch(this){
            case FAILED: return "0";
            case SUCCESS: return "1";
            case SUCCESS_WITH_WARNING: return "2";
            default: return "-1";
        }
    }
}

