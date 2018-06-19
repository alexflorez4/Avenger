/**
 * Created by Yishuo on 6/19/2017.
 */

package com.shades.services.fragx.Exceptions;

public class BadItemIdException extends Exception {
    private String message;
    public BadItemIdException(String message)
    {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}
