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

public class MobileUserVerificationResource extends BaseResource {

	/** The underlying Item object. */
	MobileUserBean mobileuser;
	MobileUserModel mobileUserModel = new MobileUserModel();

	/** The sequence of characters that identifies the resource. */
	String username;
	String password;


	@Override
	protected void doInit() throws ResourceException {
		this.username = (String) getRequest().getAttributes().get("username");
		this.password = (String) getRequest().getAttributes().get("password");

		// Get the item directly from the "persistence layer".
		try {
			this.mobileuser = mobileUserModel.getMobileUser(username);
			setExisting(this.mobileuser != null);
		} catch (NullPointerException e) {
			this.mobileuser = null;
		}
	}

	public boolean authenticateMobileUser() throws IOException {
		// The PUT request updates or creates the resource.
		if (mobileuser != null) {
			Boolean result = verifyMobileUser();
			if (result == true) {
				setStatus(Status.SUCCESS_CREATED);
				setStatus(Status.SUCCESS_OK);
				return true;
			} else {
			//	setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return false;
			}
		} else {
		//	setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return false;
		}
	}
	
	private boolean verifyMobileUser(){
		if(username.equalsIgnoreCase(mobileuser.getUsername()) && password.equals(mobileuser.getPassword())) {
			return true;
		} else {
			return false;
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

				Element eltPassword = d.createElement("authenticate");
				eltPassword.appendChild(d.createTextNode(String.valueOf(authenticateMobileUser())));
				eltItem.appendChild(eltPassword);

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
