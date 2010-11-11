package com.LBA.service.advertisement;

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

import com.LBA.Advertiser.bean.AdvertisementBean;
import com.LBA.Advertiser.model.AdvertisementModel;
import com.LBA.LBAResource.BaseResource;
/**
 * @author payalpatel
 * 
 */
public class AdvertisementResource extends BaseResource {

	/** The underlying Item object. */
	AdvertisementBean advertisement;
	AdvertisementModel advertisementModel = new AdvertisementModel();

	/** The sequence of characters that identifies the resource. */
	String adId;

	@Override
	protected void doInit() throws ResourceException {

		this.adId = (String) getRequest().getAttributes().get("adId");
		// Get the item directly from the "persistence layer".
		try {
			this.advertisement = advertisementModel.getAdvertisement(adId);
			System.out.println("Ad:::" + advertisement.getAdName());
			setExisting(this.advertisement != null);
		} catch (NullPointerException e) {
			this.advertisement = null;
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
		if (advertisement == null) {
			advertisement = new AdvertisementBean();
			Form form = new Form(entity);
			advertisement.setAdName(form.getFirstValue("adName"));
			advertisement.setAdDesc(form.getFirstValue("adDesc"));
			advertisementModel.addAdvertisement(advertisement);
		} else {
			// Update the description.
			Form form = new Form(entity);
			advertisement.setAdName(form.getFirstValue("adName"));
			advertisement.setAdDesc(form.getFirstValue("adDesc"));
			// advertisementModel.updateProduct(advertisement);
		}
		setStatus(Status.SUCCESS_CREATED);
		setStatus(Status.SUCCESS_OK);
	}

	@Get("xml")
	public Representation toXml() {
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the item.
			Document d = representation.getDocument();

			if (advertisement != null) {

				Element eltItem = d.createElement("advertisement");
				d.appendChild(eltItem);

				Element eltId = d.createElement("adId");
				eltId.appendChild(d.createTextNode(String.valueOf(advertisement
						.getAdId())));
				eltItem.appendChild(eltId);

				Element eltName = d.createElement("adName");
				eltName.appendChild(d.createTextNode(advertisement.getAdName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("adDesc");
				eltDescription.appendChild(d.createTextNode(advertisement
						.getAdDesc()));
				eltItem.appendChild(eltDescription);

				Element eltAdPath = d.createElement("adPath");
				eltAdPath.appendChild(d.createTextNode(advertisement
						.getFileLocation()));
				eltItem.appendChild(eltAdPath);

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
