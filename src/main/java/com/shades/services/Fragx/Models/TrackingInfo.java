package com.shades.services.fragx.Models;

import java.util.List;

/**
 * A TrackingInfo object
 */
public class TrackingInfo {
    /**
     * Carrier used for shipping
     */
    private String Carrier;
    /**
     * Carrier service used for shipping
     */
    private String Service;
    /**
     * Tracking number provided by carrier
     */
    private String TrackingNumber;
    /**
     * Shipped date and time
     */
    private String DateShipped;
    /**
     * Array of an order's tracking record
     */
    private List<TrackingEvent> TrackingRecord;

    /**
     * Initializes a new TrackingInfo object.
     * @param Carrier Carrier used for shipping
     * @param Service Carrier service used for shipping
     * @param TrackingNumber Tracking number provided by carrier
     * @param DateShipped Shipped date and time
     * @param TrackingRecord List of TrackingEvent
     */
    public TrackingInfo(String Carrier, String Service, String TrackingNumber, String DateShipped, List<TrackingEvent> TrackingRecord){
        this.Carrier = Carrier;
        this.Service = Service;
        this.TrackingNumber = TrackingNumber;
        this.DateShipped = DateShipped;
        this.TrackingRecord = TrackingRecord;
    }
    
    public TrackingInfo(){}
    
    public void setCarrier(String Carrier){
		this.Carrier = Carrier;
	}

	public void setService(String Service) {
		this.Service = Service;
	}

	public void setTrackingNumber(String TrackingNumber) {
		this.TrackingNumber = TrackingNumber;
	}

	public void setDateShipped(String DateShipped) {
		this.DateShipped = DateShipped;
	}

	public void setTrackingRecord(List<TrackingEvent> TrackingRecord) {
		this.TrackingRecord = TrackingRecord;
	}

    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
    public String toString(){
        String trackingRecordList = "";
        for(TrackingEvent a : TrackingRecord){
            trackingRecordList += a.toString();
        }
        return "Carrier : " + Carrier
                + "\nService : " + Service
                + "\nTrackingNumber : " + TrackingNumber
                + "\nDateShipped : " + DateShipped
                + "\nTrackingRecord : " + trackingRecordList + "\n";
    }
}
