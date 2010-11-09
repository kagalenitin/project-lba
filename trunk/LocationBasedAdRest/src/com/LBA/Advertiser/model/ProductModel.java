package com.LBA.Advertiser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.bean.GlobalBean;
import com.LBA.Advertiser.bean.ProductBean;

public class ProductModel {
	static boolean valueInserted;
	static boolean valueDeleted;
	static Statement stmtInsert = null;
	static Statement stmtView = null;
	static ResultSet rsSet = null;
	static ResultSet rsRead = null;
	ProductBean productBean = new ProductBean();

	public void setProductDetails(ProductBean beanObj) {
		DBConnect.connectDB();
		try {
			stmtInsert = DBConnect.con.createStatement();
			String qry = "INSERT INTO Product (productname, productdescription, price, username) values "
				+ "('"
				+ beanObj.getProductName()
				+ "', '"
				+ beanObj.getProductdescription()
				+ "', "
				+ beanObj.getPrice()
				+ ", '"
				+ GlobalBean.getUsersession()
				+ "')";
			System.out.println(qry);
			int res = stmtInsert.executeUpdate(qry);

			if (res == 1) {
				valueInserted = true;
			} else {
				valueInserted = false;
			}

			stmtInsert.close();
			DBConnect.disconnectDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean getProductDetails() {
		return valueInserted;
	}

	public int getProductCount() {
		DBConnect.connectDB();
		int count = 0;
		try {
			stmtView = DBConnect.con.createStatement();

			String qryCount = "SELECT COUNT(*) as cnt FROM Product where username='"
				+ GlobalBean.getUsersession() + "';";
			rsSet = stmtView.executeQuery(qryCount);
			rsSet.next();
			count = rsSet.getInt("cnt");

			stmtView.close();
			rsSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	public ProductBean[] viewAllProducts() {

		DBConnect.connectDB();
		int count = getProductCount();
		ProductBean[] objBean = new ProductBean[count];
		if (count == 0) {

		} else {

			try {
				int i = 0;
				// objBean = new ProductBean[count];
				stmtView = DBConnect.con.createStatement();
				String qry = "SELECT * FROM Product where username='"
					+ GlobalBean.getUsersession() + "';";
				rsRead = stmtView.executeQuery(qry);
				while (rsRead.next()) {
					objBean[i] = new ProductBean();
					objBean[i].setCount(rsRead.getInt("productid"));
					objBean[i].setProductName(rsRead.getString("productname"));
					objBean[i].setProductdescription(rsRead
							.getString("productdescription"));
					objBean[i].setPrice(rsRead.getDouble("price"));
					i++;
				}

				stmtView.close();
				rsRead.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return objBean;

	}

	public void editProductDetail(int productID) {
		try {
			DBConnect.connectDB();
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT * FROM Product where productID=" + productID
			+ ";";
			rsRead = stmtView.executeQuery(qry);
			rsRead.next();

			productBean.setCount(rsRead.getInt("productID"));
			productBean.setAdvertiserName(rsRead.getString("username"));
			productBean.setProductName(rsRead.getString("productname"));
			productBean.setProductdescription(rsRead
					.getString("productdescription"));
			productBean.setPrice(rsRead.getDouble("price"));

			stmtView.close();
			rsRead.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ProductBean getEditProductDetail() {

		return productBean;
	}

	public boolean updateProduct(ProductBean beanObject) {

		try {
			DBConnect.connectDB();
			stmtInsert = DBConnect.con.createStatement();
			String qry = "UPDATE Product SET productname='"
				+ beanObject.getProductName() + "', productdescription='"
				+ beanObject.getProductdescription() + "'" + ", price="
				+ beanObject.getPrice() + " WHERE productID="
				+ beanObject.getCount() + ";";
			int res = stmtInsert.executeUpdate(qry);
			if (res == 1) {
				valueInserted = true;
			} else {
				valueInserted = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valueInserted;

	}

	public boolean deleteProduct(ProductBean beanObject) {

		try {
			DBConnect.connectDB();

			stmtInsert = DBConnect.con.createStatement();
			String qry = "DELETE FROM Product where productID="
				+ beanObject.getCount() + ";";
			int res = stmtInsert.executeUpdate(qry);
			if (res == 1) {
				valueDeleted = true;
			} else {
				valueDeleted = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return valueInserted;
	}

	public ProductBean[] getProducts() {

		DBConnect.connectDB();
		int count = getProductsCount();
		ProductBean[] objBean = new ProductBean[count];
		try {
			int i = 0;
			// objBean = new ProductBean[count];
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT * FROM Product order by productname;";
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				objBean[i] = new ProductBean();
				objBean[i].setCount(rsRead.getInt("productid"));
				objBean[i].setProductName(rsRead.getString("productname"));
				objBean[i].setProductdescription(rsRead
						.getString("productdescription"));
				objBean[i].setPrice(rsRead.getDouble("price"));
				i++;
			}

			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objBean;

	}

	public ProductBean getProduct(int productID) {
		try {
			DBConnect.connectDB();
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT * FROM Product where productID=" + productID
			+ " order by productname;";
			rsRead = stmtView.executeQuery(qry);
			rsRead.next();
			productBean.setCount(rsRead.getInt("productID"));
			productBean.setAdvertiserName(rsRead.getString("username"));
			productBean.setProductName(rsRead.getString("productname"));
			productBean.setProductdescription(rsRead
					.getString("productdescription"));
			productBean.setPrice(rsRead.getDouble("price"));

			stmtView.close();
			rsRead.close();
			return productBean;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productBean;
	}

	public int getProductsCount() {
		DBConnect.connectDB();
		int count = 0;

		try {
			stmtView = DBConnect.con.createStatement();

			String qryCount = "SELECT COUNT(*) as cnt FROM Product";
			rsSet = stmtView.executeQuery(qryCount);
			rsSet.next();
			count = rsSet.getInt("cnt");

			stmtView.close();
			rsSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<ProductBean> getProductsByChannel(ChannelBean chobj) {

		ChannelModel channelModel = new ChannelModel();
		int count = channelModel.getChannelCount();
		ArrayList<ProductBean> products = new ArrayList<ProductBean>();
		ProductBean product = new ProductBean();
		if (count != 0) {
			try {
				DBConnect.connectDB();
				stmtView = DBConnect.con.createStatement();
				String qry = " SELECT p.productID,productname,productdescription,price FROM channel_ad cad,ad_product adp, product p where cad.adid = adp.adid and adp.productid= p.productid and cad.channelid="
					+ chobj.getChannelid() + " order by productname;";
				System.out.println(qry);
				rsSet = stmtView.executeQuery(qry);
				while (rsSet.next()) {
					product = new ProductBean();
					product.setCount(rsSet.getInt("productid"));
					product.setProductName(rsSet.getString("productName"));
					product.setProductdescription(rsSet
							.getString("productdescription"));
					product.setPrice(rsSet.getDouble("price"));
					products.add(product);
				}
				stmtView.close();
				rsSet.close();
				DBConnect.disconnectDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return products;
	}

	public ArrayList<ProductBean> SearchProducts(String productName) {

		DBConnect.connectDB();
		ProductModel productmodel = new ProductModel();
		int count = productmodel.getProductsCount();
		ArrayList<ProductBean> products = new ArrayList<ProductBean>();
		ProductBean product = new ProductBean();
		if (count != 0) {
			try {
				stmtView = DBConnect.con.createStatement();
				String qry = "SELECT * FROM Product where productname like "
					+ "'%" + productName + "%' " + "or '%" + productName
					+ "' or '" + productName + "%' order by productname;";
				rsRead = stmtView.executeQuery(qry);
				while (rsRead.next()) {
					product = new ProductBean();
					product.setCount(rsRead.getInt("productid"));
					product.setProductName(rsRead.getString("productName"));
					product.setProductdescription(rsRead
							.getString("productdescription"));
					product.setPrice(rsRead.getDouble("price"));
					products.add(product);
				}
				stmtView.close();
				rsRead.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return products;
	}

}
