package com.shades.services.fragx.Exceptions;

/**
 * Created by Yishuo on 6/29/2017.
 */
public class NullResponseException extends Exception {
    private String message;
    public NullResponseException(String message)
    {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}