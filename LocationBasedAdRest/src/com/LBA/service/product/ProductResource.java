package com.LBA.service.product;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.ProductBean;
import com.LBA.Advertiser.model.ProductModel;
import com.LBA.LBAResource.BaseResource;

/**
 * @author payalpatel
 * 
 */
public class ProductResource extends BaseResource {

	/** The underlying Item object. */
	ProductBean product;
	ProductModel productModel = new ProductModel();

	/** The sequence of characters that identifies the resource. */
	String productId;

	@Override
	protected void doInit() throws ResourceException {

		this.productId = (String) getRequest().getAttributes().get("productId");
		int prodId = Integer.valueOf(productId);

		// Get the item directly from the "persistence layer".
		try {
			this.product = productModel.getProduct(prodId);
			setExisting(this.product != null);
		} catch (NullPointerException e) {
			this.product = null;
		}
	}

	/**
	 * Handle DELETE requests.
	 */
	@Delete
	public void removeItem() {
		if (product != null) {
			// Remove the item from the list.
			productModel.deleteProduct(product);
		} else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		}

		// Tells the client that the request has been successfully fulfilled.
		setStatus(Status.SUCCESS_NO_CONTENT);
	}

	/**
	 * Handle PUT requests.
	 * 
	 * @throws IOException
	 */
	@Put
	public void storeItem(Representation entity) throws IOException {
		// The PUT request updates or creates the resource.
		if (product == null) {
			product = new ProductBean();
			Form form = new Form(entity);
			product.setProductName(form.getFirstValue("productName"));
			product.setProductdescription(form.getFirstValue("productDesc"));
			String price1 = form.getFirstValue("productPrice");
			double price = Double.parseDouble(price1);
			product.setPrice(price);
			productModel.setProductDetails(product);
		} else {
			// Update the description.
			Form form = new Form(entity);
			product.setProductName(form.getFirstValue("productName"));
			product.setProductdescription(form.getFirstValue("productDesc"));
			String price1 = form.getFirstValue("productPrice");
			double price = Double.parseDouble(price1);
			product.setPrice(price);
			productModel.updateProduct(product);
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

			if (product != null) {

				Element eltItem = d.createElement("product");
				d.appendChild(eltItem);

				Element eltProductId = d.createElement("productId");
				eltProductId.appendChild(d.createTextNode(String
						.valueOf(product.getCount())));
				eltItem.appendChild(eltProductId);

				Element eltName = d.createElement("productName");
				eltName.appendChild(d.createTextNode(product.getProductName()));
				eltItem.appendChild(eltName);

				Element eltDescription = d.createElement("productDesc");
				eltDescription.appendChild(d.createTextNode(product
						.getProductdescription()));
				eltItem.appendChild(eltDescription);

				Element eltPrice = d.createElement("proudctPrice");
				eltPrice.appendChild(d.createTextNode(String.valueOf(product
						.getPrice())));
				eltItem.appendChild(eltPrice);

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
