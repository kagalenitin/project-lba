package com.lba.service;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lba.beans.MobileUserBean;

/**
 * @author payalpatel
 * 
 */
public class MobileUserResourceClient {

	public ClientResource mobleUsersResource;
	public ClientResource mobilrUserResource;
	String ipaddress = "192.168.1.72:8182";
	String serviceAddress = "http://" + ipaddress + "/LBAResource/mobileusers";

	public MobileUserResourceClient() {
		mobleUsersResource = new ClientResource(serviceAddress);
		mobilrUserResource = null;
	}

	public void createMobileUser(MobileUserBean mobileUser) {

		// Create a new user
		try {
			Representation r = mobleUsersResource
					.post(getRepresentation(mobileUser));
			mobilrUserResource = new ClientResource(r.getLocationRef());

		} catch (ResourceException e) {
			System.out.println("Error  status:: " + e.getStatus());
			System.out.println("Error message:: " + e.getMessage());
		}
		// Consume the response's entity which releases the connection
		try {
			mobleUsersResource.getResponseEntity().exhaust();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateMobileUser(String username, MobileUserBean newMobileUser)
			throws IOException {
		mobilrUserResource = new ClientResource(serviceAddress + "/" + username);
		MobileUserBean mobileuser = new MobileUserBean();
		DomRepresentation representation = retrieveMobileUser(username);
		mobileuser = getMobileUserFromXml(representation);
		mobileuser.setUsername(newMobileUser.getUsername());
		mobileuser.setPassword(newMobileUser.getPassword());
		mobileuser.setFirstName(newMobileUser.getFirstName());
		mobileuser.setLastName(newMobileUser.getLastName());
		mobileuser.setAddress(newMobileUser.getAddress());
		mobileuser.setPhone(newMobileUser.getPhone());
		mobileuser.setEmail(newMobileUser.getEmail());

		mobilrUserResource.put(getRepresentation(mobileuser));
	}

	public void deleteMobileUser(String username) {
		mobilrUserResource = new ClientResource(serviceAddress + "/" + username);
		mobilrUserResource.delete();

	}

	public DomRepresentation retrieveMobileUser(String username) {
		try {
			mobilrUserResource = new ClientResource(serviceAddress + "/"
					+ username);
			return get(mobilrUserResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {

		MobileUserResourceClient client = new MobileUserResourceClient();
		MobileUserBean mobileuser = new MobileUserBean();
		// for (int i = 1; i <= 10; i++) {

		mobileuser.setUsername("payal");
		mobileuser.setPassword("payal");
		mobileuser.setFirstName("fname");
		mobileuser.setLastName("lname");
		mobileuser.setAddress("address");
		mobileuser.setPhone("1234567890");
		mobileuser.setEmail("payal");
		client.createMobileUser(mobileuser);
		// }
		// client.deleteMobileUser("payal");
		mobileuser.setFirstName("Payal");

		try {
			client.updateMobileUser("payal", mobileuser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// client.retrieveMobileUser("payal");
	}

	public MobileUserBean getMobileUserFromXml(DomRepresentation representation)
			throws IOException {

		MobileUserBean mobileuser = new MobileUserBean();
		try {

			Document doc = representation.getDocument();
			doc.getDocumentElement().normalize();
			System.out.println("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("mobileuser");
			System.out.println("Information of User");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					NodeList usernameElmntLst = fstElmnt
							.getElementsByTagName("username");
					Element userNameElmnt = (Element) usernameElmntLst.item(0);
					NodeList username = userNameElmnt.getChildNodes();
					System.out.println("Username : "
							+ ((Node) username.item(0)).getNodeValue());

					mobileuser.setUsername(((Node) username.item(0))
							.getNodeValue().toString());

					NodeList passwordElmntLst = fstElmnt
							.getElementsByTagName("password");
					Element passwordElmnt = (Element) passwordElmntLst.item(0);
					NodeList password = passwordElmnt.getChildNodes();
					System.out.println("password : "
							+ ((Node) password.item(0)).getNodeValue());

					mobileuser.setLastName(((Node) password.item(0))
							.getNodeValue().toString());

					NodeList firstNameElmntLst = fstElmnt
							.getElementsByTagName("firstName");
					Element firstNameElmnt = (Element) firstNameElmntLst
							.item(0);
					NodeList firstName = firstNameElmnt.getChildNodes();
					System.out.println("f Name : "
							+ ((Node) firstName.item(0)).getNodeValue());

					mobileuser.setFirstName(((Node) firstName.item(0))
							.getNodeValue().toString());

					NodeList lastNameElmntLst = fstElmnt
							.getElementsByTagName("lastName");
					Element lastNameElmnt = (Element) lastNameElmntLst.item(0);
					NodeList lastName = lastNameElmnt.getChildNodes();
					System.out.println("l Name : "
							+ ((Node) lastName.item(0)).getNodeValue());

					mobileuser.setLastName(((Node) lastName.item(0))
							.getNodeValue().toString());

					NodeList addressElmntLst = fstElmnt
							.getElementsByTagName("address");
					Element addressElmnt = (Element) addressElmntLst.item(0);
					NodeList address = addressElmnt.getChildNodes();
					System.out.println("Address : "
							+ ((Node) address.item(0)).getNodeValue());

					mobileuser.setAddress(((Node) address.item(0))
							.getNodeValue().toString());

					NodeList phoneElmntLst = fstElmnt
							.getElementsByTagName("phone");
					Element phoneElmnt = (Element) phoneElmntLst.item(0);
					NodeList phone = phoneElmnt.getChildNodes();
					System.out.println("Phone : "
							+ ((Node) phone.item(0)).getNodeValue());

					mobileuser.setPhone(((Node) phone.item(0)).getNodeValue()
							.toString());

					NodeList emailElmntLst = fstElmnt
							.getElementsByTagName("email");
					Element emailElmnt = (Element) emailElmntLst.item(0);
					NodeList email = emailElmnt.getChildNodes();
					System.out.println("Email : "
							+ ((Node) email.item(0)).getNodeValue());

					mobileuser.setEmail(((Node) email.item(0)).getNodeValue()
							.toString());

				}
			}
		} catch (Exception e) {
			System.out.println("Service not available");
		}
		return mobileuser;

	}

	/**
	 * Prints the resource's representation.
	 * 
	 * @param clientResource
	 *            The Restlet client resource.
	 * @throws IOException
	 * @throws ResourceException
	 */
	public static DomRepresentation get(ClientResource clientResource)
			throws IOException, ResourceException {
		try {
			clientResource.get().write(System.out);

			DomRepresentation representation = new DomRepresentation(
					clientResource.get());
			Document doc = representation.getDocument();
			doc.getDocumentElement().normalize();
			System.out.println("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("mobileuser");
			System.out.println("Information of all Users");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList prodNmElmntLst = fstElmnt
							.getElementsByTagName("username");
					Element prodNmElmnt = (Element) prodNmElmntLst.item(0);
					NodeList prodNm = prodNmElmnt.getChildNodes();
					System.out.println("Name : "
							+ ((Node) prodNm.item(0)).getNodeValue());
					NodeList prodDescElmntLst = fstElmnt
							.getElementsByTagName("firstName");
					Element prodDescElmnt = (Element) prodDescElmntLst.item(0);
					NodeList prodDesc = prodDescElmnt.getChildNodes();
					System.out.println("first name : "
							+ ((Node) prodDesc.item(0)).getNodeValue());
				}

			}

			return representation;
		} catch (ResourceException e) {
			System.out.println("Error  status: " + e.getStatus());
			System.out.println("Error message: " + e.getMessage());
			// Consume the response's entity which releases the connection
			// clientResource.getResponseEntity().exhaust();
		}
		return null;
	}

	/**
	 * Returns the Representation of an item.
	 * 
	 * @param item
	 *            the item.
	 * 
	 * @return The Representation of the item.
	 */
	public static Representation getRepresentation(MobileUserBean mobileuser) {
		// Gathering informations into a Web form.
		Form form = new Form();
		form.add("username", mobileuser.getUsername());
		form.add("password", mobileuser.getPassword());
		form.add("firstname", mobileuser.getFirstName());
		form.add("lastname", mobileuser.getLastName());
		form.add("address", mobileuser.getAddress());
		form.add("phone", mobileuser.getPhone());
		form.add("email", mobileuser.getEmail());
		return form.getWebRepresentation();
	}

}