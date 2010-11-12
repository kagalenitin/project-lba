package com.LBA.category;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.CategoryBean;
import com.LBA.Advertiser.model.CategoryModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 * @author payalpatel
 */
public class CategoriesResource extends BaseResource {

	CategoryBean category;

	CategoryModel categoryModel = new CategoryModel();

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

	@Get("xml")
	public Representation toXml() {
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the item.
			Document d = representation.getDocument();
			Element r = d.createElement("categories");
			d.appendChild(r);

			ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
			categories = categoryModel.getCategories();
			if (categories != null) {

				for (int i = 0; i < categories.size(); i++) {

					Element eltItem = d.createElement("category");

					Element eltCategoryID = d.createElement("categoryID");
					eltCategoryID.appendChild(d.createTextNode(String
							.valueOf(categories.get(i).getCategoryID())));
					eltItem.appendChild(eltCategoryID);

					Element eltCategoryName = d.createElement("categoryName");
					eltCategoryName.appendChild(d.createTextNode(categories
							.get(i).getCategoryName()));
					eltItem.appendChild(eltCategoryName);

					r.appendChild(eltItem);

				}
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
