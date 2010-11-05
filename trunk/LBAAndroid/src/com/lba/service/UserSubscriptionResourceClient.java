package com.lba.service;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.data.Form;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lba.beans.ChannelBean;
import com.lba.beans.ChannelSubscriptionBean;

/**
 * @author payalpatel
 * 
 */
public class UserSubscriptionResourceClient {

	public ClientResource UserSubscriptionsResource;
	public ClientResource UserSubscriptionResource;

	private String ipaddress = new ServiceUtil().getAddress();
	String serviceAddress = "http://" + ipaddress + "/LBAResource/subscription";

	ChannelSubscriptionBean userSubscription = new ChannelSubscriptionBean();

	public UserSubscriptionResourceClient(String username) {
		UserSubscriptionsResource = new ClientResource(serviceAddress + "/"
				+ username);
		UserSubscriptionResource = null;

	}

	public void createSubscription(
			ChannelSubscriptionBean channelSubscriptionBean) {

		// Create a new subscription
		try {
			UserSubscriptionResource = new ClientResource(serviceAddress + "/"
					+ channelSubscriptionBean.getUserId() + "/"
					+ channelSubscriptionBean.getChanneld());
			Representation r = UserSubscriptionResource
					.post(getRepresentation(channelSubscriptionBean));
			UserSubscriptionsResource = new ClientResource(r.getLocationRef());

		} catch (ResourceException e) {
			System.out.println("Error  status:: " + e.getStatus());
			System.out.println("Error message:: " + e.getMessage());
		}
		// Consume the response's entity which releases the connection

		// UserSubscriptionsResource.getResponseEntity().exhaust();
	}

	public void deleteSubscriptionByUser(
			ChannelSubscriptionBean channelSubscriptionBean) {
		UserSubscriptionResource = new ClientResource(serviceAddress + "/"
				+ channelSubscriptionBean.getUserId() + "/"
				+ channelSubscriptionBean.getChanneld());
		UserSubscriptionResource.delete();

	}

	public DomRepresentation retrieveSubscrptionByUser(String username) {
		try {
			UserSubscriptionsResource = new ClientResource(serviceAddress + "/"
					+ username);
			return get(UserSubscriptionsResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<ChannelBean> getChannelsFromXml(
			DomRepresentation representation) throws IOException {

		ArrayList<ChannelBean> channels = new ArrayList<ChannelBean>();

		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("channel");
		System.out.println("Information of all Channels");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);
			ChannelBean channel = new ChannelBean();

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList IdElmntLst = fstElmnt
						.getElementsByTagName("channelId");
				Element IdElmnt = (Element) IdElmntLst.item(0);
				NodeList Id = IdElmnt.getChildNodes();
				System.out
						.println("Id : " + ((Node) Id.item(0)).getNodeValue());

				channel.setChannelid(((Node) Id.item(0)).getNodeValue()
						.toString());

				NodeList NameElmntLst = fstElmnt
						.getElementsByTagName("channelName");
				Element fstNmElmnt = (Element) NameElmntLst.item(0);
				NodeList Name = fstNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) Name.item(0)).getNodeValue());

				channel.setChannelname(((Node) Name.item(0)).getNodeValue()
						.toString());

				NodeList descElmntLst = fstElmnt
						.getElementsByTagName("channelDesc");
				Element descNmElmnt = (Element) descElmntLst.item(0);
				NodeList desc = descNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) desc.item(0)).getNodeValue());

				channel.setChanneldescription(((Node) desc.item(0))
						.getNodeValue().toString());

				channels.add(channel);
			}

		}
		return channels;

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
			// clientResource.get().write(System.out);
			// System.out.println();

			DomRepresentation representation = new DomRepresentation(
					clientResource.get());
			Document doc = representation.getDocument();
			doc.getDocumentElement().normalize();
			System.out.println("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("channel");
			System.out.println("Information of all Channels");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList channelNameElmntLst = fstElmnt
							.getElementsByTagName("channelName");
					Element channelNmElmnt = (Element) channelNameElmntLst
							.item(0);
					NodeList channelNm = channelNmElmnt.getChildNodes();
					System.out.println("Name : "
							+ ((Node) channelNm.item(0)).getNodeValue());
					NodeList descElmntLst = fstElmnt
							.getElementsByTagName("channelDesc");
					Element descElmnt = (Element) descElmntLst.item(0);
					NodeList lstNm = descElmnt.getChildNodes();
					System.out.println("Description : "
							+ ((Node) lstNm.item(0)).getNodeValue());
				}

			}

			return representation;
			// Log.d("MyApplication==========",clientResource.get().write(System.out));
			// return clientResource.get();
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
	public static Representation getRepresentation(
			ChannelSubscriptionBean channelSubscriptionBean) {
		// Gathering informations into a Web form.
		Form form = new Form();
		form.add("username", channelSubscriptionBean.getUserId());
		form.add("channelId", channelSubscriptionBean.getChanneld());
		return form.getWebRepresentation();
	}

}