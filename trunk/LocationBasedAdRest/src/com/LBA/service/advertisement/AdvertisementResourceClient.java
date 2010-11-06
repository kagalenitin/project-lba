package com.LBA.service.advertisement;

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

import com.LBA.Advertiser.bean.AdMerchantAdBean;
import com.LBA.Advertiser.bean.AdvertisementBean;

/**
 * @author payalpatel
 * 
 */
public class AdvertisementResourceClient {

	public ClientResource advertisementsResource;
	public ClientResource advertisementResource;
	public ClientResource advertisementsByProductResource;
	public ClientResource advertisementsByMerchantResource;
	public ClientResource advertisementsByMerchantDistanceResource;

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

	public DomRepresentation retrieveAdvertisementsByProduct(String productId) {
		try {
			advertisementsByMerchantResource = new ClientResource(
					serviceAddress + "/products/" + productId);
			return get(advertisementsByProductResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveAdvertisementsByMerchant(String adName) {
		try {
			advertisementsByMerchantResource = new ClientResource(
					serviceAddress + "/merchant/" + adName);
			return get(advertisementsByMerchantResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveAdvertisementsByMerchantDistance(
			String adName, String latitude, String longitude) {
		try {
			advertisementsByMerchantDistanceResource = new ClientResource(
					serviceAddress + "/merchant/" + adName + "/" + latitude
							+ "/" + longitude);
			return get(advertisementsByMerchantDistanceResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

				NodeList adLocationElmntLst = fstElmnt
						.getElementsByTagName("adPath");
				Element adLocationNmElmnt = (Element) adLocationElmntLst
						.item(0);
				NodeList adLocation = adLocationNmElmnt.getChildNodes();
				System.out.println("Location : "
						+ ((Node) adLocation.item(0)).getNodeValue());

				advertisement.setFileLocation(((Node) adLocation.item(0))
						.getNodeValue().toString());

			}

		}
		return advertisement;

	}

	public ArrayList<AdMerchantAdBean> getAdvertisementsByMerchantFromXml(
			DomRepresentation representation) throws IOException {

		ArrayList<AdMerchantAdBean> advertisements = new ArrayList<AdMerchantAdBean>();
		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("advertisement");
		System.out.println("Information of all AdvertisementsByMerchant");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);
			AdMerchantAdBean advertisement = new AdMerchantAdBean();

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList adIdElmntLst = fstElmnt.getElementsByTagName("adId");
				Element adIdElmnt = (Element) adIdElmntLst.item(0);
				NodeList adId = adIdElmnt.getChildNodes();
				System.out.println("Id : "
						+ ((Node) adId.item(0)).getNodeValue());

				advertisement.setAdID(((Node) adId.item(0)).getNodeValue()
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
				Element adDescElmnt = (Element) adDescElmntLst.item(0);
				NodeList adDesc = adDescElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) adDesc.item(0)).getNodeValue());

				advertisement.setAdDesc(((Node) adDesc.item(0)).getNodeValue()
						.toString());

				NodeList longitudeElmntLst = fstElmnt
						.getElementsByTagName("longitude");
				Element longitudeElmnt = (Element) longitudeElmntLst.item(0);
				NodeList longitude = longitudeElmnt.getChildNodes();
				System.out.println("longitude : "
						+ ((Node) longitude.item(0)).getNodeValue());

				advertisement.setLongitude(((Node) longitude.item(0))
						.getNodeValue().toString());

				NodeList latitudeElmntLst = fstElmnt
						.getElementsByTagName("latitude");
				Element latitudeElmnt = (Element) latitudeElmntLst.item(0);
				NodeList latitude = latitudeElmnt.getChildNodes();
				System.out.println("latitude : "
						+ ((Node) latitude.item(0)).getNodeValue());

				advertisement.setLatitude(((Node) latitude.item(0))
						.getNodeValue().toString());

				NodeList addressElmntLst = fstElmnt
						.getElementsByTagName("address");
				Element addressElmnt = (Element) addressElmntLst.item(0);
				NodeList address = addressElmnt.getChildNodes();
				System.out.println("address : "
						+ ((Node) address.item(0)).getNodeValue());

				advertisement.setAddress(((Node) address.item(0))
						.getNodeValue().toString());

				NodeList cityElmntLst = fstElmnt.getElementsByTagName("city");
				Element cityElmnt = (Element) cityElmntLst.item(0);
				NodeList city = cityElmnt.getChildNodes();
				System.out.println("city : "
						+ ((Node) city.item(0)).getNodeValue());

				advertisement.setCity(((Node) city.item(0)).getNodeValue()
						.toString());

				NodeList stateElmntLst = fstElmnt.getElementsByTagName("state");
				Element stateElmnt = (Element) stateElmntLst.item(0);
				NodeList state = stateElmnt.getChildNodes();
				System.out.println("state : "
						+ ((Node) state.item(0)).getNodeValue());

				advertisement.setState(((Node) state.item(0)).getNodeValue()
						.toString());

				NodeList zipElmntLst = fstElmnt.getElementsByTagName("zip");
				Element zipElmnt = (Element) zipElmntLst.item(0);
				NodeList zip = zipElmnt.getChildNodes();
				System.out.println("zip : "
						+ ((Node) zip.item(0)).getNodeValue());

				advertisement.setZip(((Node) zip.item(0)).getNodeValue()
						.toString());

				advertisements.add(advertisement);
			}

		}
		return advertisements;

	}

	public ArrayList<AdvertisementBean> getAdvertisementsByFromXml(
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
				Element adDescNmElmnt = (Element) adDescElmntLst.item(0);
				NodeList adDesc = adDescNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) adDesc.item(0)).getNodeValue());

				advertisement.setAdDesc(((Node) adDesc.item(0)).getNodeValue()
						.toString());

				NodeList adLocationElmntLst = fstElmnt
						.getElementsByTagName("adPath");
				Element adLocationNmElmnt = (Element) adLocationElmntLst
						.item(0);
				NodeList adLocation = adLocationNmElmnt.getChildNodes();
				System.out.println("Location : "
						+ ((Node) adLocation.item(0)).getNodeValue());

				advertisement.setFileLocation(((Node) adLocation.item(0))
						.getNodeValue().toString());

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
			System.out.println();

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