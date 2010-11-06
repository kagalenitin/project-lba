package com.LBA.service.product;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.bean.ProductBean;
import com.LBA.Advertiser.model.ChannelModel;
import com.LBA.Advertiser.model.ProductModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 */
public class ProductsByChannelResource extends BaseResource {

	ProductBean product;
	ProductModel productModel = new ProductModel();
	ChannelModel channelModel = new ChannelModel();
	ChannelBean channel;

	/** The sequence of characters that identifies the resource. */
	String channelId;

	@Override
	protected void doInit() throws ResourceException {

		this.channelId = (String) getRequest().getAttributes().get("channelId");
		try {
			this.channel = channelModel.getChannelDetails(channelId);
			setExisting(this.channel != null);
		} catch (NullPointerException e) {
			this.channel = null;
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
			Element r = d.createElement("products");
			d.appendChild(r);

			ArrayList<ProductBean> products = productModel
					.getProductsByChannel(channel);

			for (int i = 0; i < products.size(); i++) {

				Element eltItem = d.createElement("product");

				Element eltProductId = d.createElement("productId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(products.get(i).getCount())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("productName");
				eltName.appendChild(d.createTextNode(products.get(i)
						.getProductName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("productDesc");
				eltDescription.appendChild(d.createTextNode(products.get(i)
						.getProductdescription()));
				eltItem.appendChild(eltDescription);

				Element eltPrice = d.createElement("proudctPrice");
				eltPrice.appendChild(d.createTextNode(String.valueOf(products
						.get(i).getPrice())));
				eltItem.appendChild(eltPrice);

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
