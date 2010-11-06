package com.LBA.service.advertisement;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.AdvertisementBean;
import com.LBA.Advertiser.bean.ProductBean;
import com.LBA.Advertiser.model.AdvertisementModel;
import com.LBA.Advertiser.model.ProductModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 */
public class AdvertisementsByProductResource extends BaseResource {

	AdvertisementBean advertisement;
	AdvertisementModel advertisementModel = new AdvertisementModel();
	ProductModel productModel = new ProductModel();
	String productID;
	ProductBean product = new ProductBean();

	@Override
	protected void doInit() throws ResourceException {

		this.productID = (String) getRequest().getAttributes().get("productId");
		int prodID = Integer.valueOf(productID);
		// Get the item directly from the "persistence layer".
		try {
			this.product = productModel.getProduct(prodID);
			setExisting(this.product != null);
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

			// ArrayList<AdvertisementBean> advertisements = advertisementModel
			// .getAdsByProduct(product);
			//

			ArrayList<AdvertisementBean> advertisements = advertisementModel
					.getAdDetailsByProduct(product);

			// AdvertisementBean[] advertisements =
			// advertisementModel.viewAdDetails(productID);

			for (int i = 0; i < advertisements.size(); i++) {
				Element eltItem = d.createElement("advertisement");

				Element eltId = d.createElement("adId");
				eltId.appendChild(d.createTextNode(String
						.valueOf(advertisements.get(i).getAdId())));
				eltItem.appendChild(eltId);

				Element eltName = d.createElement("adName");
				eltName.appendChild(d.createTextNode(advertisements.get(i)
						.getAdName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("adDesc");
				eltDescription.appendChild(d.createTextNode(advertisements.get(
						i).getAdDesc()));
				eltItem.appendChild(eltDescription);

				Element eltAdPath = d.createElement("adPath");
				eltAdPath.appendChild(d.createTextNode(advertisements.get(i)
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
