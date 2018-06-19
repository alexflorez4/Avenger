package com.shades.services.fragx.Exceptions;

/**
 * Created by Yishuo on 6/30/2017.
 */
public class HttpErrorException extends Exception {
    private String message;
    public HttpErrorException(String message)
    {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}