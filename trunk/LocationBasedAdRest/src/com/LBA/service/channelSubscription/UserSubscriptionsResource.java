package com.LBA.service.channelSubscription;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.bean.ChannelSubscriptionBean;
import com.LBA.Advertiser.model.ChannelSubscriptionModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 */
public class UserSubscriptionsResource extends BaseResource {

	ChannelSubscriptionModel usersubscriptionModel = new ChannelSubscriptionModel();
	ChannelSubscriptionBean userSubscription = new ChannelSubscriptionBean();

	/** The sequence of characters that identifies the resource. */
	String username;

	@Override
	protected void doInit() throws ResourceException {
		this.username = (String) getRequest().getAttributes().get("username");
		try {
			setExisting(this.username != null);
		} catch (NullPointerException e) {
			this.username = null;
		}
	}

	/**
	 * Returns a listing of all registered items.
	 */

	@Get("xml")
	public DomRepresentation toXml() {
		// Generate the right representation according to its media type.
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);

			// Generate a DOM document representing the list of
			// items.
			Document d = representation.getDocument();
			Element r = d.createElement("usersubscriptions");
			d.appendChild(r);

			ArrayList<ChannelBean> channelBean = usersubscriptionModel
					.getChannelBySubscription(username);
			for (int i = 0; i < channelBean.size(); i++) {

				System.out.println(i + " check"
						+ channelBean.get(i).getChannelid());
				Element eltItem = d.createElement("channel");

				Element eltChannelId = d.createElement("channelId");
				eltChannelId.appendChild(d.createTextNode(String
						.valueOf(channelBean.get(i).getChannelid())));
				eltItem.appendChild(eltChannelId);

				Element eltName = d.createElement("channelName");
				eltName.appendChild(d.createTextNode(channelBean.get(i)
						.getChannelname()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("channelDesc");
				eltDescription.appendChild(d.createTextNode(channelBean.get(i)
						.getChanneldescription()));
				eltItem.appendChild(eltDescription);

				r.appendChild(eltItem);
			}
			d.normalizeDocument();

			// Returns the XML representation of this document.
			return representation;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
