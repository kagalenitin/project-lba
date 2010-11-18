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

/**
 * @author payalpatel
 * 
 */
public class ChannelResourceClient {

	public ClientResource channelsResource;
	public ClientResource channelResource;
	public ClientResource channelsByNameResource;
	public ClientResource channelsByUserResource;
	public ClientResource channelsByNameByUserResource;
	public ClientResource channelsByCategoryResource;

	private String ipaddress = new ServiceUtil().getAddress();
	String serviceAddress = "http://" + ipaddress + "/channels";

	public ChannelResourceClient() {
		channelsResource = new ClientResource(serviceAddress);
		channelResource = null;

	}

	public void createChannel(ChannelBean channel) {

		// Create a new channel
		try {
			Representation r = channelsResource
					.post(getRepresentation(channel));
			channelResource = new ClientResource(r.getLocationRef());

		} catch (ResourceException e) {
			System.out.println("Error  status:: " + e.getStatus());
			System.out.println("Error message:: " + e.getMessage());
		}
		// Consume the response's entity which releases the connection
		try {
			channelsResource.getResponseEntity().exhaust();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateChannel(int channelId, ChannelBean newChannel)
			throws IOException {
		channelResource = new ClientResource(serviceAddress + channelId);

		ChannelBean channel = new ChannelBean();
		DomRepresentation representation = retrieveChannel(String
				.valueOf(channelId));
		channel = getChannelFromXml(representation);
		channel.setChannelname(newChannel.getChannelname());
		channel.setChanneldescription(newChannel.getChanneldescription());
		channelResource.put(getRepresentation(channel));
	}

	public void deleteChannel(int channelId) {
		channelResource = new ClientResource(serviceAddress + channelId);
		channelResource.delete();

	}

	public DomRepresentation retrieveChannel(String channelId) {
		try {
			channelResource = new ClientResource(serviceAddress + channelId);
			return get(channelResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveChannels() {
		try {
			return get(channelsResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveChannelsByUser(String user) {
		channelsByUserResource = new ClientResource(serviceAddress
				+ "/username/" + user);

		try {
			return get(channelsByUserResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveChannelsByNameByUser(String channelName,
			String user) {
		channelsByNameByUserResource = new ClientResource(serviceAddress
				+ "/channelname/" + channelName + "/username/" + user);

		try {
			return get(channelsByNameByUserResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveChannelsByCategory(String categoryName) {
		channelsByCategoryResource = new ClientResource(serviceAddress
				+ "/category/" + categoryName);
		try {
			return get(channelsByCategoryResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveChannelByName(String channelName) {
		channelsByNameResource = new ClientResource(serviceAddress
				+ "/channelname/" + channelName);
		try {
			return get(channelsByNameResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {

		ChannelResourceClient client = new ChannelResourceClient();

		for (int i = 0; i < 10; i++) {
			ChannelBean channel = new ChannelBean();
			channel.setChannelname("TestProd1" + i);
			channel.setChanneldescription("testDesc");
			// client.createChannel(channel);

		}

		// client.deleteProduct(68);

		// channel.setChannelname("NewTestedName2");

		/*
		 * try { client.updateChannel(1, channel); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		client.retrieveChannel("1");

		ArrayList<ChannelBean> channels = null;
		DomRepresentation representation = client.retrieveChannels();
		try {
			channels = client.getChannelsFromXml(representation);
			System.out.println(channels.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < channels.size(); i++) {
			System.out.println(channels.get(i).getChannelid());
		}

		// client.retrieveProducts();

	}

	public ChannelBean getChannelFromXml(DomRepresentation representation)
			throws IOException {

		ChannelBean channel = new ChannelBean();

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

				NodeList IdElmntLst = fstElmnt
						.getElementsByTagName("channelId");
				Element prodIdElmnt = (Element) IdElmntLst.item(0);
				NodeList Id = prodIdElmnt.getChildNodes();
				System.out
						.println("Id : " + ((Node) Id.item(0)).getNodeValue());

				channel.setChannelid(((Node) Id.item(0)).getNodeValue()
						.toString());

				NodeList NameElmntLst = fstElmnt
						.getElementsByTagName("channelName");
				Element channelNmElmnt = (Element) NameElmntLst.item(0);
				NodeList channelName = channelNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) channelName.item(0)).getNodeValue());

				channel.setChannelname(((Node) channelName.item(0))
						.getNodeValue().toString());

				NodeList descElmntLst = fstElmnt
						.getElementsByTagName("channelDesc");
				Element descNmElmnt = (Element) descElmntLst.item(0);
				NodeList desc = descNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) desc.item(0)).getNodeValue());

				channel.setChanneldescription(((Node) desc.item(0))
						.getNodeValue().toString());
			}
		}
		return channel;
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
			clientResource.get().write(System.out);

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
	public static Representation getRepresentation(ChannelBean channel) {
		// Gathering informations into a Web form.
		Form form = new Form();
		form.add("channelName", channel.getChannelname());
		form.add("channelDesc", channel.getChanneldescription());
		return form.getWebRepresentation();
	}

}