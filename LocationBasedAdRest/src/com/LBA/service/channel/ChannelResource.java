package com.LBA.service.channel;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.model.ChannelModel;
import com.LBA.LBAResource.BaseResource;

public class ChannelResource extends BaseResource {

	/** The underlying Item object. */
	ChannelBean channel;
	ChannelModel channelModel = new ChannelModel();

	/** The sequence of characters that identifies the resource. */
	String channelId;

	@Override
	protected void doInit() throws ResourceException {
		// Get the "itemName" attribute value taken from the URI template
		// /items/{itemName}.
		this.channelId = (String) getRequest().getAttributes().get("channelId");
		// Get the item directly from the "persistence layer".
		try {
			this.channel = channelModel.getChannelDetails(channelId);
			setExisting(this.channel != null);
		} catch (NullPointerException e) {
			this.channel = null;
		}
	}

	/**
	 * Handle PUT requests.
	 * 
	 * @throws IOException
	 */
	@Put
	public void storeItem(Representation entity) throws IOException {
		// The PUT request updates or creates the resource.
		if (channel == null) {
			channel = new ChannelBean();
			Form form = new Form(entity);
			channel.setChannelname(form.getFirstValue("channelName"));
			channel.setChanneldescription(form.getFirstValue("channelDesc"));
			channelModel.setChannel(channel);
		}
		// if (getItems().putIfAbsent(item.getName(), item) == null) {
		setStatus(Status.SUCCESS_CREATED);
		// } else {
		setStatus(Status.SUCCESS_OK);
		// }
	}

	@Get("xml")
	public Representation toXml() {
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the item.
			Document d = representation.getDocument();

			if (channel != null) {

				Element eltItem = d.createElement("channel");
				d.appendChild(eltItem);

				Element eltProductId = d.createElement("channelId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(channel.getChannelid())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("channelName");
				eltName.appendChild(d.createTextNode(channel.getChannelname()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("channelDesc");
				eltDescription.appendChild(d.createTextNode(channel
						.getChanneldescription()));
				eltItem.appendChild(eltDescription);

				d.normalizeDocument();
			}

			// Returns the XML representation of this document.
			return representation;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
