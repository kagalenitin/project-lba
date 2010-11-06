package com.LBA.Advertiser.bean;

import java.io.Serializable;

public class ChannelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String channelname;
	public String channeldescription;
	public String image;
	public String channelid;

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getChanneldescription() {
		return channeldescription;
	}

	public void setChanneldescription(String channeldescription) {
		this.channeldescription = channeldescription;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}