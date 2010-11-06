package com.LBA.service.channel;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.model.ChannelModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 */
public class ChannelsResource extends BaseResource {

	ChannelBean channel;
	ChannelModel channelModel = new ChannelModel();

	/**
	 * Handle POST requests: create a new item.
	 */
	@Post
	public Representation acceptItem(Representation entity) {

		Representation result = null;
		// Parse the given representation and retrieve pairs of
		// "name=value" tokens.

		try {

			if (entity != null) {

				Form form = new Form(entity);
				channel = new ChannelBean();

				// Register the new item if one is not already registered.
				channel.setChannelname(form.getFirstValue("channelName"));
				channel.setChanneldescription(form.getFirstValue("channelDesc"));
				channelModel.setChannel(channel);

				// Set the response's status and entity
				setStatus(Status.SUCCESS_CREATED);
				Representation rep = new StringRepresentation(
						"Channel Item created", MediaType.TEXT_PLAIN);
				// Indicates where is located the new resource.
				rep.setLocationRef(getRequest().getResourceRef()
						.getIdentifier()
						+ "/"
						+ form.getFirstValue("channelName"));
				result = rep;
			} else { // Item is already registered.
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				result = generateErrorRepresentation("Channel Item "
						+ " is NULL", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	/*
	 * @Get public Representation toObject() { // Generate a DOM document
	 * representing the list of // items. ObjectRepresentation<Serializable>
	 * representation = new ObjectRepresentation<Serializable>((Serializable)
	 * getItems(),MediaType.APPLICATION_JAVA_OBJECT);
	 * 
	 * // Returns the XML representation of this document. return
	 * representation; }
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

			ChannelBean[] channelBean = channelModel.viewChannelDetails();
			for (int i = 0; i < channelBean.length; i++) {

				// for (Item item : getItems().values()) {
				Element eltItem = d.createElement("channel");

				Element eltProductId = d.createElement("channelId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(channelBean[i].getChannelid())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("channelName");
				eltName.appendChild(d.createTextNode(channelBean[i]
						.getChannelname()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("channelDesc");
				eltDescription.appendChild(d.createTextNode(channelBean[i]
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
