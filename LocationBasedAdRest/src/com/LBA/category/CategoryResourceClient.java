package com.LBA.category;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.LBA.Advertiser.bean.CategoryBean;

/**
 * @author payalpatel
 * 
 */
public class CategoryResourceClient {

	public ClientResource categoriesResource;
	public ClientResource categoryResource;

	// String ipaddress = "10.185.3.16:8182";
	String ipaddress = "192.168.1.72:8182";
	String serviceAddress = "http://" + ipaddress + "/LBAResource/categories";

	public CategoryResourceClient() {
		categoriesResource = new ClientResource(serviceAddress);
		categoryResource = null;
	}

	public DomRepresentation retrieveMobileUser(String username) {
		try {
			categoryResource = new ClientResource(serviceAddress + "/"
					+ username);
			return get(categoryResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation getCategories() {
		try {
			categoriesResource = new ClientResource(serviceAddress);
			// return get(mobileUserVerificationResource);
			DomRepresentation rep = get(categoriesResource);
			return rep;
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation getCategory(String categoryName) {
		try {
			categoriesResource = new ClientResource(serviceAddress + "/"
					+ categoryName);
			DomRepresentation rep = get(categoriesResource);
			return rep;
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		CategoryResourceClient client = new CategoryResourceClient();
		DomRepresentation representation = client.getCategories();
		try {
			client.getCategoriesFromXml(representation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CategoryBean getCategoryFromXml(DomRepresentation representation)
			throws IOException {
		CategoryBean category = new CategoryBean();
		try {
			Document doc = representation.getDocument();
			if (doc != null) {
				doc.getDocumentElement().normalize();
				System.out.println("Root element "
						+ doc.getDocumentElement().getNodeName());
				NodeList nodeLst = doc.getElementsByTagName("categories");
				System.out.println("Verification Information of Categories");
				Node fstNode = nodeLst.item(0);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					NodeList categoryIdElmntLst = fstElmnt
							.getElementsByTagName("categoryID");
					Element categoryIdElmnt = (Element) categoryIdElmntLst
							.item(0);
					NodeList categoryId = categoryIdElmnt.getChildNodes();
					System.out.println("categoryID : "
							+ ((Node) categoryId.item(0)).getNodeValue());
					category.setCategoryID(((Node) categoryId.item(0))
							.getNodeValue().toString());

					NodeList categoryNameElmntLst = fstElmnt
							.getElementsByTagName("categoryName");
					Element categoryNameElmnt = (Element) categoryNameElmntLst
							.item(0);
					NodeList categoryName = categoryNameElmnt.getChildNodes();
					System.out.println("categoryName : "
							+ ((Node) categoryName.item(0)).getNodeValue());
					category.setCategoryName(((Node) categoryName.item(0))
							.getNodeValue().toString());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return category;
	}

	public ArrayList<CategoryBean> getCategoriesFromXml(
			DomRepresentation representation) throws IOException {

		ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();

		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("category");
		System.out.println("Information of category");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				CategoryBean category = new CategoryBean();

				Element fstElmnt = (Element) fstNode;

				NodeList categoryIdElmntLst = fstElmnt
						.getElementsByTagName("categoryID");
				Element categoryIdElmnt = (Element) categoryIdElmntLst.item(0);
				NodeList categoryId = categoryIdElmnt.getChildNodes();
				System.out.println("categoryID : "
						+ ((Node) categoryId.item(0)).getNodeValue());
				category.setCategoryID(((Node) categoryId.item(0))
						.getNodeValue().toString());

				NodeList categoryNameElmntLst = fstElmnt
						.getElementsByTagName("categoryName");
				Element categoryNameElmnt = (Element) categoryNameElmntLst
						.item(0);
				NodeList categoryName = categoryNameElmnt.getChildNodes();
				System.out.println("categoryName : "
						+ ((Node) categoryName.item(0)).getNodeValue());
				category.setCategoryName(((Node) categoryName.item(0))
						.getNodeValue().toString());

				categories.add(category);
			}

		}
		return categories;

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
			NodeList nodeLst = doc.getElementsByTagName("categories");
			System.out.println("Information of all categories");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					NodeList categoryIdElmntLst = fstElmnt
							.getElementsByTagName("categoryID");
					Element categoryIdElmnt = (Element) categoryIdElmntLst
							.item(0);
					NodeList categoryId = categoryIdElmnt.getChildNodes();
					System.out.println("categoryID : "
							+ ((Node) categoryId.item(0)).getNodeValue());

					NodeList categoryNameElmntLst = fstElmnt
							.getElementsByTagName("categoryName");
					Element categoryNameElmnt = (Element) categoryNameElmntLst
							.item(0);
					NodeList categoryName = categoryNameElmnt.getChildNodes();
					System.out.println("categoryName : "
							+ ((Node) categoryName.item(0)).getNodeValue());

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

}