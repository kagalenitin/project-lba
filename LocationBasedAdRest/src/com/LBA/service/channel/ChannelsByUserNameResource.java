package com.LBA.service.channel;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.model.ChannelModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 * @author payalpatel
 */
public class ChannelsByUserNameResource extends BaseResource {

	ChannelModel channelModel = new ChannelModel();
	String userName;

	@Override
	protected void doInit() throws ResourceException {

		this.userName = (String) getRequest().getAttributes().get("userName");
		try {
			setExisting(this.userName != null);
		} catch (NullPointerException e) {
			this.userName = null;
		}
	}

	/**
	 * Generate an XML representation of an error response.
	 * 
	 * @param errorMessage
	 *            the error message.
	 * @param errorCode
	 *            the error code.
	 */
	private Representation generateErrorRepresentation(String errorMessage,
			String errorCode) {
		DomRepresentation result = null;
		// This is an error
		// Generate the output representation
		try {
			result = new DomRepresentation(MediaType.TEXT_XML);
			// Generate a DOM document representing the list of
			// items.
			Document d = result.getDocument();

			Element eltError = d.createElement("error");

			Element eltCode = d.createElement("code");
			eltCode.appendChild(d.createTextNode(errorCode));
			eltError.appendChild(eltCode);

			Element eltMessage = d.createElement("message");
			eltMessage.appendChild(d.createTextNode(errorMessage));
			eltError.appendChild(eltMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
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
			Element r = d.createElement("channels");
			d.appendChild(r);

			ArrayList<ChannelBean> channels = channelModel
					.getChannelByUserName(userName);
			for (int i = 0; i < channels.size(); i++) {

				// for (Item item : getItems().values()) {
				Element eltItem = d.createElement("channel");

				Element eltProductId = d.createElement("channelId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(channels.get(i).getChannelid())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("channelName");
				eltName.appendChild(d.createTextNode(channels.get(i)
						.getChannelname()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("channelDesc");
				eltDescription.appendChild(d.createTextNode(channels.get(i)
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
