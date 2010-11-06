package com.LBA.service.advertisement;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.AdMerchantAdBean;
import com.LBA.Advertiser.bean.AdvertisementBean;
import com.LBA.Advertiser.model.AdvertisementModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 */
public class AdvertisementsByMerchantIdResource extends BaseResource {

	AdvertisementBean advertisement;
	AdvertisementModel advertisementModel = new AdvertisementModel();
	String adID;

	@Override
	protected void doInit() throws ResourceException {

		this.adID = (String) getRequest().getAttributes().get("adId");
		// Get the item directly from the "persistence layer".
		try {
			setExisting(this.adID != null);
		} catch (NullPointerException e) {
			this.advertisement = null;
		}
	}

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

			ArrayList<AdMerchantAdBean> advertisements = advertisementModel
					.getAdsbyMerchantID(adID);

			for (int i = 0; i < advertisements.size(); i++) {
				Element eltItem = d.createElement("advertisement");

				Element eltId = d.createElement("adId");
				eltId.appendChild(d.createTextNode(String
						.valueOf(advertisements.get(i).getAdID())));
				eltItem.appendChild(eltId);

				Element eltName = d.createElement("adName");
				eltName.appendChild(d.createTextNode(advertisements.get(i)
						.getAdName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("adDesc");
				eltDescription.appendChild(d.createTextNode(advertisements.get(
						i).getAdDesc()));
				eltItem.appendChild(eltDescription);

				Element eltLatitude = d.createElement("latitude");
				eltLatitude.appendChild(d.createTextNode(advertisements.get(i)
						.getLatitude()));
				eltItem.appendChild(eltLatitude);

				Element eltLongitude = d.createElement("longitude");
				eltLongitude.appendChild(d.createTextNode(advertisements.get(i)
						.getLongitude()));
				eltItem.appendChild(eltLongitude);

				Element eltAddress = d.createElement("address");
				eltAddress.appendChild(d.createTextNode(advertisements.get(i)
						.getAddress()));
				eltItem.appendChild(eltAddress);

				Element eltCity = d.createElement("city");
				eltCity.appendChild(d.createTextNode(advertisements.get(i)
						.getCity()));
				eltItem.appendChild(eltCity);

				Element eltState = d.createElement("state");
				eltState.appendChild(d.createTextNode(advertisements.get(i)
						.getState()));
				eltItem.appendChild(eltState);

				Element eltZip = d.createElement("zip");
				eltZip.appendChild(d.createTextNode(advertisements.get(i)
						.getZip()));
				eltItem.appendChild(eltZip);

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
