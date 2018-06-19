package com.shades.services.fragx.Models;

/**
 * A TrackingRecord object
 */
public class TrackingEvent {
    /**
     * Date at which the order was checked in
     */
    public String EventDate;
    /**
     * Time at which the order was checked in
     */
    public String EventTime;
    /**
     * Location of the package
     */
    public String Location;
    /**
     * Description regarding the package
     */
    public String Description;
    /**
     * Date and time at which the order was checked in
     */
    public String EventDateTime;
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        return "\n-------------------------------------------\nEventDate : " + EventDate
                + "\nEventTime : " + EventTime
                + "\nLocation : " + Location
                + "\nDescription : " + Description
                + "\nEventDateTime : " + EventDateTime;
    }
}
