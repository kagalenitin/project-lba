package com.lba.beans;

import java.io.Serializable;

public class ProductBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public String advertiserName;
	public String productName;
	public String productdescription;
	public String username;
	public double price;
	public int count;

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the advertiserName
	 */
	public String getAdvertiserName() {
		return advertiserName;
	}

	/**
	 * @param advertiserName
	 *            the advertiserName to set
	 */
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the productDesc
	 */

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	public String getProductdescription() {
		return productdescription;
	}

	public void setProductdescription(String productdescription) {
		this.productdescription = productdescription;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}
