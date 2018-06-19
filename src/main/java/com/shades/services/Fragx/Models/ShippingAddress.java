package com.shades.services.fragx.Models;

/**
 * Shipping
 */
public class ShippingAddress {
    /**
     * First name
     */
    private String FirstName;
    /**
     * Last name
     */
    private String LastName;
    /**
     * Address 1
     */
    private String Address1;
    /**
     * Address 2
     */
    private String Address2;
    /**
     * City
     */
    private String City;
    /**
     * State
     */
    private String State;
    /**
     * Zipcode
     */
    private String ZipCode;
    /**
     * Country
     */
    private String Country;
    /**
     * Phone
     */
    private String Phone;

    /**
     * Initializes a new ShippingAddress object.
     * @param FirstName First name
     * @param LastName Last name
     * @param Address1 Address 1
     * @param Address2 Address 2
     * @param City City
     * @param State State
     * @param ZipCode Zipcode
     * @param Country Country
     * @param Phone Phone
     */
    public ShippingAddress(String FirstName, String LastName, String Address1, String Address2, String City, String State, String ZipCode, String Country, String Phone){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Address1 = Address1;
        this.Address2 = Address2;
        this.City = City;
        this.State = State;
        this.ZipCode = ZipCode;
        this.Country = Country;
        this.Phone = Phone;
    }
    
    public ShippingAddress(){}
    /**
     * Return the object as a string.
     * @return A string containing an order's properties.
     */
	public String toString() {
		return "First Name : " + FirstName + "\nLast Name : " + LastName + "\nAddress 1 : " + Address1
				+ "\nAddress 2 : " + Address2 + "\nCity : " + City + "\nState : " + State + "\nZipcode : " + ZipCode
				+ "\nCountry : " + Country + "\nPhone : " + Phone;
	}

	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}

	public void setLastName(String LastName) {
		this.LastName = LastName;
	}

	public void setAddress1(String Address1) {
		this.Address1 = Address1;
	}

	public void setAddress2(String Address2) {
		this.Address2 = Address2;
	}

	public void setCity(String City) {
		this.City = City;
	}

	public void setState(String State) {
		this.State = State;
	}

	public void setZipCode(String ZipCode) {
		this.ZipCode = ZipCode;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}

	public void set(String Phone) {
		this.Phone = Phone;
	}

}
