/**
 * 
 */
package com.lba.service;

/**
 * @author payalpatel This class is mainly interact with the web service for
 *         given ipaddress.
 * 
 */
public class ServiceUtil {

	// use below if you are running REST service on tomcat, otherwise comment it.
	// private String ipaddress = "10.185.3.113:8080/LocationBasedAdRest";

	// use below if you are running REST service on restlet engine, otherwise comment it.
	private String ipaddress = "10.185.19.62:8182/LocationBasedAdRest";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public String getAddress() {
		return ipaddress;
	}

}
