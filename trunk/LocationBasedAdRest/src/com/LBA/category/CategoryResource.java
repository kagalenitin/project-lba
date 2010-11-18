package com.LBA.category;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.CategoryBean;
import com.LBA.Advertiser.model.CategoryModel;
import com.LBA.LBAResource.BaseResource;

/**
 * @author payalpatel
 * 
 */
public class CategoryResource extends BaseResource {

	/** The underlying Item object. */
	CategoryBean category;
	CategoryModel categoryModel = new CategoryModel();

	/** The sequence of characters that identifies the resource. */
	String categoryName;

	@Override
	protected void doInit() throws ResourceException {
		this.categoryName = (String) getRequest().getAttributes().get(
				"categoryName");
		// Get the item directly from the "persistence layer".
		try {
			this.category = categoryModel.getCategoryByName(categoryName);
			setExisting(this.category != null);
		} catch (NullPointerException e) {
			this.category = null;
		}
	}

	@Get("xml")
	public Representation toXml() {
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the item.
			Document d = representation.getDocument();

			Element eltItem = d.createElement("category");
			d.appendChild(eltItem);

			if (category != null) {

				Element eltCategoryID = d.createElement("categroyID");
				eltCategoryID.appendChild(d.createTextNode(String
						.valueOf(category.getCategoryID())));
				eltItem.appendChild(eltCategoryID);

				Element eltCategoryName = d.createElement("categoryName");
				eltCategoryName.appendChild(d.createTextNode(category
						.getCategoryName()));
				eltItem.appendChild(eltCategoryName);
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
