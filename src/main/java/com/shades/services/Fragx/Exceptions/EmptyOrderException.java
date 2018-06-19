package com.shades.services.fragx.Exceptions;

/**
 * Created by Yishuo on 6/19/2017.
 */
public class EmptyOrderException extends Exception {
    private String message;
    public EmptyOrderException(String message)
    {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}