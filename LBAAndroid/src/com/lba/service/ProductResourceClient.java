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

import com.lba.beans.ProductBean;

/**
 * @author payalpatel
 * 
 */
public class ProductResourceClient {

	public ClientResource productsResource;
	public ClientResource productResource;
	public ClientResource productByChannelResource;
	public ClientResource productByNameResource;

	// String ipaddress = "10.185.3.16:8182";
	// String ipaddress = "192.168.1.72:8182";
	String ipaddress = "10.185.3.171:8182";

	String serviceAddress = "http://" + ipaddress + "/LBAResource/products";

	public ProductResourceClient() {
		productsResource = new ClientResource(serviceAddress);
		productResource = null;
	}

	public void createProduct(ProductBean product) {

		// Create a new product
		try {
			Representation r = productsResource
					.post(getRepresentation(product));
			productResource = new ClientResource(r.getLocationRef());

		} catch (ResourceException e) {
			System.out.println("Error  status:: " + e.getStatus());
			System.out.println("Error message:: " + e.getMessage());
		}
		// Consume the response's entity which releases the connection
		try {
			productsResource.getResponseEntity().exhaust();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateProduct(int productId, ProductBean newProduct)
			throws IOException {
		productResource = new ClientResource(serviceAddress + productId);
		ProductBean product = new ProductBean();
		DomRepresentation representation = retrieveProduct(String
				.valueOf(productId));
		product = getProductFromXml(representation);
		product.setProductName(newProduct.getProductName());
		product.setProductdescription(newProduct.getProductdescription());
		product.setPrice(newProduct.getPrice());
		product.setAdvertiserName(newProduct.getAdvertiserName());
		productResource.put(getRepresentation(product));
	}

	public void deleteProduct(int productId) {
		productResource = new ClientResource(serviceAddress + productId);
		productResource.delete();

	}

	public DomRepresentation retrieveProduct(String productId) {
		try {
			productResource = new ClientResource(serviceAddress + productId);
			return get(productResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveProducts() {
		try {
			return get(productsResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveProductbyChannel(String channelId) {
		productByChannelResource = new ClientResource(serviceAddress
				+ "/channels/" + channelId);
		try {
			return get(productByChannelResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DomRepresentation retrieveProductbyName(String productName) {
		productByNameResource = new ClientResource(serviceAddress
				+ "/productname/" + productName);
		try {
			return get(productByNameResource);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		ProductResourceClient client = new ProductResourceClient();

		ProductBean product = new ProductBean();
		for (int i = 1; i <= 10; i++) {
			product.setProductName("TestProd1" + i);
			product.setProductdescription("testDesc" + i);
			product.setPrice(12.56);
			product.setAdvertiserName("testUser" + i);
			client.createProduct(product);
		}
		product.setProductName("NewTestedName2");

		try {
			client.updateProduct(70, product);
		} catch (IOException e) {
			e.printStackTrace();
		}

		client.retrieveProduct("5");

		ArrayList<ProductBean> products = null;
		DomRepresentation representation = client.retrieveProducts();
		try {
			products = client.getProductsFromXml(representation);
			System.out.println(products.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < products.size(); i++) {
			System.out.println(products.get(i).getCount());
		}

		// client.retrieveProducts();

	}

	public ProductBean getProductFromXml(DomRepresentation representation)
			throws IOException {

		ProductBean product = new ProductBean();

		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("product");
		System.out.println("Information of all Products");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList prodIdElmntLst = fstElmnt
						.getElementsByTagName("productId");
				Element prodIdElmnt = (Element) prodIdElmntLst.item(0);
				NodeList prodId = prodIdElmnt.getChildNodes();
				System.out.println("Id : "
						+ ((Node) prodId.item(0)).getNodeValue());

				product.setCount(Integer.valueOf(((Node) prodId.item(0))
						.getNodeValue().toString()));

				NodeList prodNameElmntLst = fstElmnt
						.getElementsByTagName("productName");
				Element prodNmElmnt = (Element) prodNameElmntLst.item(0);
				NodeList prodName = prodNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) prodName.item(0)).getNodeValue());

				product.setProductName(((Node) prodName.item(0)).getNodeValue()
						.toString());

				NodeList prodDescElmntLst = fstElmnt
						.getElementsByTagName("productDesc");
				Element prodDescNmElmnt = (Element) prodDescElmntLst.item(0);
				NodeList prodDesc = prodDescNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) prodDesc.item(0)).getNodeValue());

				product.setProductdescription(((Node) prodDesc.item(0))
						.getNodeValue().toString());

				NodeList prodPriceElmntLst = fstElmnt
						.getElementsByTagName("proudctPrice");
				Element prodPriceElmnt = (Element) prodPriceElmntLst.item(0);
				NodeList prodPrice = prodPriceElmnt.getChildNodes();
				System.out.println("Price : "
						+ ((Node) prodPrice.item(0)).getNodeValue());

				product.setPrice(Double.valueOf(((Node) prodPrice.item(0))
						.getNodeValue().toString()));

			}

		}
		return product;

	}

	public ArrayList<ProductBean> getProductsFromXml(
			DomRepresentation representation) throws IOException {

		ArrayList<ProductBean> products = new ArrayList<ProductBean>();
		// ProductBean product = new ProductBean();

		Document doc = representation.getDocument();
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("product");
		System.out.println("Information of all Products");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);
			ProductBean product = new ProductBean();

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList prodIdElmntLst = fstElmnt
						.getElementsByTagName("productId");
				Element prodIdElmnt = (Element) prodIdElmntLst.item(0);
				NodeList prodId = prodIdElmnt.getChildNodes();
				System.out.println("Id : "
						+ ((Node) prodId.item(0)).getNodeValue());

				product.setCount(Integer.valueOf(((Node) prodId.item(0))
						.getNodeValue().toString()));

				NodeList prodNameElmntLst = fstElmnt
						.getElementsByTagName("productName");
				Element prodNmElmnt = (Element) prodNameElmntLst.item(0);
				NodeList prodName = prodNmElmnt.getChildNodes();
				System.out.println("Name : "
						+ ((Node) prodName.item(0)).getNodeValue());

				product.setProductName(((Node) prodName.item(0)).getNodeValue()
						.toString());

				NodeList prodDescElmntLst = fstElmnt
						.getElementsByTagName("productDesc");
				Element prodDescNmElmnt = (Element) prodDescElmntLst.item(0);
				NodeList prodDesc = prodDescNmElmnt.getChildNodes();
				System.out.println("Description : "
						+ ((Node) prodDesc.item(0)).getNodeValue());

				product.setProductdescription(((Node) prodDesc.item(0))
						.getNodeValue().toString());

				NodeList prodPriceElmntLst = fstElmnt
						.getElementsByTagName("proudctPrice");
				Element prodPriceElmnt = (Element) prodPriceElmntLst.item(0);
				NodeList prodPrice = prodPriceElmnt.getChildNodes();
				System.out.println("Price : "
						+ ((Node) prodPrice.item(0)).getNodeValue());

				product.setPrice(Double.valueOf(((Node) prodPrice.item(0))
						.getNodeValue().toString()));

				products.add(product);
			}

		}
		return products;

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
			NodeList nodeLst = doc.getElementsByTagName("product");
			System.out.println("Information of all Products");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList prodNmElmntLst = fstElmnt
							.getElementsByTagName("productName");
					Element prodNmElmnt = (Element) prodNmElmntLst.item(0);
					NodeList prodNm = prodNmElmnt.getChildNodes();
					System.out.println("Name : "
							+ ((Node) prodNm.item(0)).getNodeValue());
					NodeList prodDescElmntLst = fstElmnt
							.getElementsByTagName("productDesc");
					Element prodDescElmnt = (Element) prodDescElmntLst.item(0);
					NodeList prodDesc = prodDescElmnt.getChildNodes();
					System.out.println("Description : "
							+ ((Node) prodDesc.item(0)).getNodeValue());
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
	public static Representation getRepresentation(ProductBean product) {
		// Gathering informations into a Web form.
		Form form = new Form();
		form.add("productName", product.getProductName());
		form.add("productDesc", product.getProductdescription());
		form.add("productPrice", String.valueOf(product.getPrice()));
		return form.getWebRepresentation();
	}

}