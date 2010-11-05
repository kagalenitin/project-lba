package com.lba.beans;

import java.io.Serializable;

public class AdMerchantAdBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public String adID;
	public String latitude;
	public String longitude;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String adName;
	public String adDesc;
	public String adStartDate;
	public String adEndDate;

	/**
	 * @return the adID
	 */
	public String getAdID() {
		return adID;
	}

	/**
	 * @param adID
	 *            the adID to set
	 */
	public void setAdID(String adID) {
		this.adID = adID;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAdName() {
		return adName;
	}

	/**
	 * @param adName
	 *            the adName to set
	 */
	public void setAdName(String adName) {
		this.adName = adName;
	}

	/**
	 * @return the adDesc
	 */
	public String getAdDesc() {
		return adDesc;
	}

	/**
	 * @param adDesc
	 *            the adDesc to set
	 */
	public void setAdDesc(String adDesc) {
		this.adDesc = adDesc;
	}

	/**
	 * @return the adStartDate
	 */
	public String getAdStartDate() {
		return adStartDate;
	}

	/**
	 * @param adStartDate
	 *            the adStartDate to set
	 */
	public void setAdStartDate(String adStartDate) {
		this.adStartDate = adStartDate;
	}

	/**
	 * @return the adEndDate
	 */
	public String getAdEndDate() {
		return adEndDate;
	}

}
