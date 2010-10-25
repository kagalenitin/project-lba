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

import com.lba.beans.AdvertisementBean;

/**
 * @author payalpatel
 * 
 */
public class AdvertisementResourceClient {

	public ClientResource advertisementsResource;
	public ClientResource advertisementResource;
	public ClientResource advertisementbyProductResource;

	// String ipaddress = "10.185.3.16:8182";
	String ipaddress = "192.168.1.72:8182";
	String serviceAddress = "http://" + ipaddress
			+ "/LBAResource/advertisements";

	public AdvertisementResourceClient() {
		advertisementsResource = new ClientResource(serviceAddress);
		advertisementResource = null;
	}

	public void createAdvertisement(AdvertisementBean advertisement) {

		// Create a new Ad
		try {
			Representation r = advertisementsResource
					.post(getRepresentation(advertisement));
			advertisementResource = new ClientResource(r.getLocationRef());

		} catch (ResourceException e) {
			System.out.println("Error  status:: " + e.getStatus());
			System.out.println("Error message:: " + e.getMessage());
		}
		// Consume the response's entity which releases the connection
		try {
			advertisementsResource.getResponseEntity().exhaust();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateAdvertisement(int adId, AdvertisementBean newAdvertisement)
			throws IOException {
		advertisementResource = new ClientResource(serviceAddress + adId);
		AdvertisementBean advertisement = new AdvertisementBean();
		DomRepresentation representation = retrieveAdvertisement(String
				.valueOf(adId));
		advertisement = getAdvertisementFromXml(representation);
		advertisement.setAdId(newAdvertisement.getAdName());
		advertisement.setAdDesc(newAdvertisement.getAdDesc());
		advertisementResource.put(getRepresentation(advertisement));
	}

	public void deleteProduct(int adId) {
		advertisementResource = new ClientResource(serviceAddress + adId);
		advertisementResource.delete();

	}

	public DomRepresentation retrieveAdvertisement(String adId) {
		try {
			advertisementResource = new ClientResource(serviceAddress + adId);
			return get(advertisementResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveAdvertisements() {
		try {
			return get(advertisementsResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DomRepresentation retrieveAdsbyProduct(String productId) {
		advertisementbyProductResource = new ClientResource(serviceAddress
				+ "/products/" + productId);
		try {
			return get(advertisementbyProductResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String args[]) {
		AdvertisementResourceClient client = new AdvertisementResourceClient();
		// yyyy-mm-dd
		AdvertisementBean advertisement = new AdvertisementBean();
		advertisement.setAdName("Jeccypenny");
		advertisement.setAdDesc("Men's Jeans");
		advertisement.setContractID("1");
		advertisement.setAdStartDate("2010-10-20");
		advertisement.setAdEndDate("2010-11-20");
		client.createAdvertisement(advertisement);

		// client.deleteProduct(68);

		/*
		 * advertisement.setAdName("NewTestedName2");
		 * 
		 * try { client.updateAdvertisement(1,advertisement); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * client.retrieveAdvertisement("1");
		 * 
		 * ArrayList<AdvertisementBean> products = null; DomRepresentation
		 * representation = client.retrieveAdvertisements(); try { products =
		 * client.getAdvertisementsFromXml(representation);
		 * System.out.println(products.size()); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * for (int i = 0; i < products.size(); i++) {
		 * System.out.println(products.get(i).getAdId()); }
		 * 
		 * // client.retrieveProducts();
		 */

	}

	public AdvertisementBean getAdvertisementFromXml(
			DomRepresentation representation) throws IOException {

		AdvertisementBean advertisement = new AdvertisementBean();

		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("advertisement");
		System.out.println("Information of all Advertisements");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList adIdElmntLst = fstElmnt.getElementsByTagName("adId");
				Element adIdElmnt = (Element) adIdElmntLst.item(0);
				NodeList adId = adIdElmnt.getChildNodes();
				System.out.println("Id : "
						+ ((Node) adId.item(0)).getNodeValue());

				advertisement.setAdId(((Node) adId.item(0)).getNodeValue()
						.toString());

				NodeList adNameElmntLst = fstElmnt
						.getElementsByTagName("adName");
				Element adNmElmnt = (Element) adNameElmntLst.item(0);
				NodeList adName = adNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) adName.item(0)).getNodeValue());

				advertisement.setAdName(((Node) adName.item(0)).getNodeValue()
						.toString());

				NodeList adDescElmntLst = fstElmnt
						.getElementsByTagName("adDesc");
				Element adDescNmElmnt = (Element) adDescElmntLst.item(0);
				NodeList adDesc = adDescNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) adDesc.item(0)).getNodeValue());

				advertisement.setAdDesc(((Node) adDesc.item(0)).getNodeValue()
						.toString());

			}

		}
		return advertisement;

	}

	public ArrayList<AdvertisementBean> getAdvertisementsFromXml(
			DomRepresentation representation) throws IOException {

		ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();
		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("advertisement");
		System.out.println("Information of all Advertisements");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);
			AdvertisementBean advertisement = new AdvertisementBean();

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList adIdElmntLst = fstElmnt.getElementsByTagName("adId");
				Element adIdElmnt = (Element) adIdElmntLst.item(0);
				NodeList adId = adIdElmnt.getChildNodes();
				System.out.println("Id : "
						+ ((Node) adId.item(0)).getNodeValue());

				advertisement.setAdId(((Node) adId.item(0)).getNodeValue()
						.toString());

				NodeList adNameElmntLst = fstElmnt
						.getElementsByTagName("adName");
				Element adNmElmnt = (Element) adNameElmntLst.item(0);
				NodeList adName = adNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) adName.item(0)).getNodeValue());

				advertisement.setAdName(((Node) adName.item(0)).getNodeValue()
						.toString());

				NodeList adDescElmntLst = fstElmnt
						.getElementsByTagName("adDesc");
				Element prodDescNmElmnt = (Element) adDescElmntLst.item(0);
				NodeList adDesc = prodDescNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) adDesc.item(0)).getNodeValue());

				advertisement.setAdDesc(((Node) adDesc.item(0)).getNodeValue()
						.toString());
				advertisements.add(advertisement);
			}

		}
		return advertisements;

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
			NodeList nodeLst = doc.getElementsByTagName("advertisement");
			System.out.println("Information of all Ads");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList adNmElmntLst = fstElmnt
							.getElementsByTagName("adName");
					Element adNmElmnt = (Element) adNmElmntLst.item(0);
					NodeList adNm = adNmElmnt.getChildNodes();
					System.out.println("Name : "
							+ ((Node) adNm.item(0)).getNodeValue());
					NodeList adDescElmntLst = fstElmnt
							.getElementsByTagName("adDesc");
					Element adDescElmnt = (Element) adDescElmntLst.item(0);
					NodeList adDesc = adDescElmnt.getChildNodes();
					System.out.println("Description : "
							+ ((Node) adDesc.item(0)).getNodeValue());
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
	public static Representation getRepresentation(
			AdvertisementBean advertisement) {
		// Gathering informations into a Web form.
		Form form = new Form();
		form.add("adName", advertisement.getAdName());
		form.add("adDesc", advertisement.getAdDesc());
		form.add("adStartDate", advertisement.getAdStartDate());
		form.add("adEndDate", advertisement.getAdEndDate());
		form.add("adContractId", advertisement.getContractID());

		return form.getWebRepresentation();
	}

}