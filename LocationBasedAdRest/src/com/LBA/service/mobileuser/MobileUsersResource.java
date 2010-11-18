package com.LBA.service.mobileuser;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.LBA.Advertiser.bean.MobileUserBean;
import com.LBA.Advertiser.model.MobileUserModel;
import com.LBA.LBAResource.BaseResource;

/**
 * Resource that manages a list of items.
 * 
 * @author payalpatel
 */
public class MobileUsersResource extends BaseResource {

	MobileUserBean mobileuser;

	MobileUserModel mobileUserModel = new MobileUserModel();

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
				mobileuser = new MobileUserBean();
				// Register the new item if one is not already registered.
				mobileuser.setUsername(form.getFirstValue("username"));
				mobileuser.setPassword(form.getFirstValue("password"));
				mobileuser.setFirstName(form.getFirstValue("firstname"));
				mobileuser.setLastName(form.getFirstValue("lastname"));
				mobileuser.setAddress(form.getFirstValue("address"));
				mobileuser.setPhone(form.getFirstValue("phone"));
				mobileuser.setEmail(form.getFirstValue("email"));
				mobileUserModel.createMobileUser(mobileuser);

				// Set the response's status and entity
				setStatus(Status.SUCCESS_CREATED);
				Representation rep = new StringRepresentation(
						"mobileuser created", MediaType.TEXT_PLAIN);
				// Indicates where is located the new resource.
				rep.setLocationRef(getRequest().getResourceRef()
						.getIdentifier() + "/" + form.getFirstValue("username"));
				result = rep;
			} else { // Item is already registered.
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				result = generateErrorRepresentation("Mobile user Item "
						+ " is NULL", "1");
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
}
