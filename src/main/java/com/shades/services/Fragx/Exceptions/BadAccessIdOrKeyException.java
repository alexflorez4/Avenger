package com.shades.services.fragx.Exceptions;

/**
 * Invalid access ID or access key will result in this exception being thrown.
 */
public class BadAccessIdOrKeyException extends Exception {
    private String message;
    public BadAccessIdOrKeyException(String message)
    {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}
