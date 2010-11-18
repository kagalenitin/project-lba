package com.LBA.service.product;

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

import com.LBA.Advertiser.bean.ProductBean;
import com.LBA.Advertiser.model.ProductModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 * @author payalpatel
 */
public class ProductsResource extends BaseResource {

	ProductBean product;
	ProductModel productModel = new ProductModel();

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
				product = new ProductBean();

				// Register the new item if one is not already registered.
				product.setProductName(form.getFirstValue("productName"));
				product.setProductdescription(form.getFirstValue("productDesc"));
				String price1 = form.getFirstValue("productPrice");
				double price = Double.parseDouble(price1);
				product.setPrice(price);
				productModel.setProductDetails(product);

				// Set the response's status and entity
				setStatus(Status.SUCCESS_CREATED);
				Representation rep = new StringRepresentation(
						"Product Item created", MediaType.TEXT_PLAIN);
				// Indicates where is located the new resource.
				rep.setLocationRef(getRequest().getResourceRef()
						.getIdentifier()
						+ "/"
						+ form.getFirstValue("productName"));
				result = rep;
			} else { // Item is already registered.
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				result = generateErrorRepresentation("Item " + " is NULL", "1");
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

			ProductBean[] productBean = productModel.getProducts();
			for (int i = 0; i < productBean.length; i++) {

				// for (Item item : getItems().values()) {
				Element eltItem = d.createElement("product");

				Element eltProductId = d.createElement("productId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(productBean[i].getCount())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("productName");
				eltName.appendChild(d.createTextNode(productBean[i]
						.getProductName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("productDesc");
				eltDescription.appendChild(d.createTextNode(productBean[i]
						.getProductdescription()));
				eltItem.appendChild(eltDescription);

				Element eltPrice = d.createElement("proudctPrice");
				eltPrice.appendChild(d.createTextNode(String
						.valueOf(productBean[i].getPrice())));
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
