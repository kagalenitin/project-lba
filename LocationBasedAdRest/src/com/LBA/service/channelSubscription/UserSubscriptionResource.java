package com.LBA.service.channelSubscription;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.LBA.Advertiser.bean.ChannelSubscriptionBean;
import com.LBA.Advertiser.model.ChannelSubscriptionModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 * @author payalpatel
 */
public class UserSubscriptionResource extends BaseResource {

	ChannelSubscriptionModel usersubscriptionModel = new ChannelSubscriptionModel();
	ChannelSubscriptionBean userSubscription = new ChannelSubscriptionBean();

	/** The sequence of characters that identifies the resource. */
	String channelId;
	String username;
	int count;

	@Override
	protected void doInit() throws ResourceException {
		this.username = (String) getRequest().getAttributes().get("username");
		this.channelId = (String) getRequest().getAttributes().get("channelId");
		userSubscription.setChanneld(channelId);
		userSubscription.setUserId(username);
		count = usersubscriptionModel
				.getSubscriptionByUserByChannel(userSubscription);
		try {
			setExisting(this.userSubscription != null);
		} catch (NullPointerException e) {
			this.userSubscription = null;
		}
	}

	/**
	 * Handle PUT requests.
	 * 
	 * @throws IOException
	 */
	@Put
	public void storeItem(Representation entity) throws IOException {
		System.out.println("PUT");
		// The PUT request updates or creates the resource.
		if (count == 0) {
			usersubscriptionModel.insertUserSubscription(userSubscription);
		}
		setStatus(Status.SUCCESS_CREATED);
		setStatus(Status.SUCCESS_OK);
	}

	@Get
	public void storeItemGet(Representation entity) throws IOException {
		System.out.println("Get");
		// The PUT request updates or creates the resource.
		if (count == 0) {
			usersubscriptionModel.insertUserSubscription(userSubscription);
		}
		setStatus(Status.SUCCESS_CREATED);
		setStatus(Status.SUCCESS_OK);
	}

	/**
	 * Handle DELETE requests.
	 */
	@Delete
	public void removeItem() {
		if (count != 0) {
			// Remove the item from the list.
			usersubscriptionModel.deleteUserSubscription(userSubscription);
		} else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		}

		// Tells the client that the request has been successfully fulfilled.
		setStatus(Status.SUCCESS_NO_CONTENT);
	}
}
