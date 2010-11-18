package com.LBA.service.mobileuser;

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

import com.LBA.Advertiser.bean.MobileUserBean;
import com.LBA.Advertiser.model.MobileUserModel;
import com.LBA.LBAResource.BaseResource;

/**
 * @author payalpatel
 * 
 */
public class MobileUserResource extends BaseResource {

	/** The underlying Item object. */
	MobileUserBean mobileuser;
	MobileUserModel mobileUserModel = new MobileUserModel();

	/** The sequence of characters that identifies the resource. */
	String username;

	@Override
	protected void doInit() throws ResourceException {
		this.username = (String) getRequest().getAttributes().get("username");
		// Get the item directly from the "persistence layer".
		try {
			this.mobileuser = mobileUserModel.getMobileUser(username);
			setExisting(this.mobileuser != null);
		} catch (NullPointerException e) {
			this.mobileuser = null;
		}
	}

	/**
	 * Handle DELETE requests.
	 */
	@Delete
	public void removeItem() {
		if (mobileuser != null) {
			// Remove the item from the list.
			mobileUserModel.deleteMobileUser(mobileuser);
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
		if (mobileuser == null) {
			mobileuser = new MobileUserBean();
			Form form = new Form(entity);
			mobileuser.setUsername(form.getFirstValue("username"));
			mobileuser.setPassword(form.getFirstValue("password"));
			mobileuser.setFirstName(form.getFirstValue("firstname"));
			mobileuser.setLastName(form.getFirstValue("lastname"));
			mobileuser.setAddress(form.getFirstValue("address"));
			mobileuser.setPhone(form.getFirstValue("phone"));
			mobileuser.setEmail(form.getFirstValue("email"));
			Boolean result = mobileUserModel.createMobileUser(mobileuser);
			if (result == true) {
				setStatus(Status.SUCCESS_CREATED);
				setStatus(Status.SUCCESS_OK);
			} else {
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		} else {
			// Update the description.
			System.out.println("Update");
			mobileuser = new MobileUserBean();
			Form form = new Form(entity);
			mobileuser.setUsername(form.getFirstValue("username"));
			mobileuser.setPassword(form.getFirstValue("password"));
			mobileuser.setFirstName(form.getFirstValue("firstname"));
			mobileuser.setLastName(form.getFirstValue("lastname"));
			mobileuser.setAddress(form.getFirstValue("address"));
			mobileuser.setPhone(form.getFirstValue("phone"));
			mobileuser.setEmail(form.getFirstValue("email"));
			boolean result = mobileUserModel.updateMobileUser(mobileuser);
			if (result == true) {
				setStatus(Status.SUCCESS_CREATED);
				setStatus(Status.SUCCESS_OK);
			} else {
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		}
	}

	@Get("xml")
	public Representation toXml() {
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the item.
			Document d = representation.getDocument();

			if (mobileuser != null) {

				Element eltItem = d.createElement("mobileuser");
				d.appendChild(eltItem);

				Element eltUserName = d.createElement("username");
				eltUserName.appendChild(d.createTextNode(String
						.valueOf(mobileuser.getUsername())));
				eltItem.appendChild(eltUserName);

				Element eltPassword = d.createElement("password");
				eltPassword.appendChild(d.createTextNode(mobileuser
						.getPassword()));
				eltItem.appendChild(eltPassword);

				Element eltFirstName = d.createElement("firstName");
				eltFirstName.appendChild(d.createTextNode(mobileuser
						.getFirstName()));
				eltItem.appendChild(eltFirstName);

				Element eltLastName = d.createElement("lastName");
				eltLastName.appendChild(d.createTextNode(mobileuser
						.getLastName()));
				eltItem.appendChild(eltLastName);

				Element eltAddress = d.createElement("address");
				eltAddress.appendChild(d.createTextNode(String
						.valueOf(mobileuser.getAddress())));
				eltItem.appendChild(eltAddress);

				Element eltPhone = d.createElement("phone");
				eltPhone.appendChild(d.createTextNode(String.valueOf(mobileuser
						.getPhone())));
				eltItem.appendChild(eltPhone);

				Element eltEmail = d.createElement("email");
				eltEmail.appendChild(d.createTextNode(String.valueOf(mobileuser
						.getEmail())));
				eltItem.appendChild(eltEmail);

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
