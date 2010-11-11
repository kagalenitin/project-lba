package com.LBA.service.advertisement;

import java.io.IOException;
import java.util.ArrayList;

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

import com.LBA.Advertiser.bean.AdvertisementBean;
import com.LBA.Advertiser.model.AdvertisementModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * @author payalpatel
 */
public class AdvertisementsResource extends BaseResource {

	AdvertisementBean advertisement;
	AdvertisementModel advertisementModel = new AdvertisementModel();

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
				advertisement = new AdvertisementBean();
				// Register the new item if one is not already registered.
				advertisement.setAdName(form.getFirstValue("adName"));
				advertisement.setAdDesc(form.getFirstValue("adDesc"));
				advertisement.setAdStartDate(form.getFirstValue("adStartDate"));
				advertisement.setAdEndDate(form.getFirstValue("adEndDate"));
				advertisement.setContractID(form.getFirstValue("adContractId"));

				advertisementModel.addAdvertisement(advertisement);

				// Set the response's status and entity
				setStatus(Status.SUCCESS_CREATED);
				Representation rep = new StringRepresentation(
						"Ad-Item created", MediaType.TEXT_PLAIN);
				// Indicates where is located the new resource.
				rep.setLocationRef(getRequest().getResourceRef()
						.getIdentifier() + "/" + form.getFirstValue("adName"));
				result = rep;
			} else { // Item is already registered.
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				result = generateErrorRepresentation("Ad-Item " + " is NULL",
						"1");
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
			Element r = d.createElement("advertisements");
			d.appendChild(r);

			ArrayList<AdvertisementBean> advertisementBean = advertisementModel
					.viewAllAdvertisements();
			for (int i = 0; i < advertisementBean.size(); i++) {

				// for (Item item : getItems().values()) {
				Element eltItem = d.createElement("advertisement");

				Element eltId = d.createElement("adId");
				eltId.appendChild(d.createTextNode(String
						.valueOf(advertisementBean.get(i).getAdId())));
				eltItem.appendChild(eltId);

				Element eltName = d.createElement("adName");
				eltName.appendChild(d.createTextNode(advertisementBean.get(i)
						.getAdName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("adDesc");
				eltDescription.appendChild(d.createTextNode(advertisementBean
						.get(i).getAdDesc()));
				eltItem.appendChild(eltDescription);

				Element eltAdPath = d.createElement("adPath");
				eltAdPath.appendChild(d.createTextNode(advertisementBean.get(i)
						.getFileLocation()));
				eltItem.appendChild(eltAdPath);

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
