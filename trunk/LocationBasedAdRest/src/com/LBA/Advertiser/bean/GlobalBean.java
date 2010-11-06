package com.LBA.Advertiser.bean;

public class GlobalBean {
	public static String usersession;

	/**
	 * @return the usersession
	 */
	public static String getUsersession() {
		return usersession;
	}

	/**
	 * @param usersession
	 *            the usersession to set
	 */
	public static void setUsersession(String usersession) {
		GlobalBean.usersession = usersession;
	}

}
