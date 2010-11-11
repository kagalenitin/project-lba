package com.LBA.Advertiser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import com.LBA.Advertiser.bean.AdMerchantAdBean;
import com.LBA.Advertiser.bean.AdvertisementBean;
import com.LBA.Advertiser.bean.GlobalBean;
import com.LBA.Advertiser.bean.ProductBean;

public class AdvertisementModel {

	static boolean valueInserted;
	static boolean valueDeleted;
	static Statement stmtInsert = null;
	static Statement stmtView = null;
	static ResultSet rsSet = null;
	static ResultSet rsRead = null;

	public int getAdvertisementIDCount() {
		/*
		 * Get the count of the Product for current user
		 */
		DBConnect.connectDB();

		int count = 0;

		try {
			stmtView = DBConnect.con.createStatement();
			String qryCount = "SELECT COUNT(*) as cnt FROM Advertisement";
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

	public int getProductCount() {
		/*
		 * Get the count of the Product for current user
		 */
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
			e.printStackTrace();
		}
		return count;
	}

	public Hashtable<Integer, String> onLoadAddProduct() {
		/*
		 * This function returns the productname for the product combobox while
		 * creating advertisement.
		 */

		Hashtable<Integer, String> hashProduct = new Hashtable<Integer, String>();
		try {
			DBConnect.connectDB();

			stmtView = DBConnect.con.createStatement();
			String qry = "select * from product where product.username='"
					+ GlobalBean.getUsersession()
					+ "' AND productID NOT IN (SELECT ad_product.productID from ad_product where product.productID = ad_product.productID);";

			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				hashProduct.put(
						rsRead.getInt("productID"),
						rsRead.getString("productname") + "\t"
								+ rsRead.getString("productdescription") + "\t"
								+ rsRead.getDouble("price") + "\t");
			}
			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashProduct;

	}

	public int getChannelCount() {
		/*
		 * Get Channel Count.
		 */
		int count = 0;
		DBConnect.connectDB();

		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT COUNT(*) as cnt from Channel";
			rsRead = stmtView.executeQuery(qry);
			rsRead.next();
			count = rsRead.getInt("cnt");

			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	public Hashtable<Integer, String> loadChannelName() {
		/*
		 * Load all the channels in the drop down of createAdvertisement form.
		 */
		Hashtable<Integer, String> hashChannel = new Hashtable<Integer, String>();

		try {
			DBConnect.connectDB();
			stmtInsert = DBConnect.con.createStatement();
			String qry = "SELECT channelID, channelname from Channel";
			rsRead = stmtInsert.executeQuery(qry);

			while (rsRead.next()) {
				hashChannel.put(rsRead.getInt("channelID"),
						rsRead.getString("channelname"));
			}

			stmtInsert.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hashChannel;
	}

	public int getContractCount() {
		/*
		 * Get the count of the Contract for current user
		 */
		DBConnect.connectDB();

		int count = 0;

		try {
			stmtView = DBConnect.con.createStatement();

			String qryCount = "SELECT COUNT(*) as cnt FROM Contract where username='"
					+ GlobalBean.getUsersession() + "';";
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

	public Hashtable<Integer, String> loadContractName() {
		Hashtable<Integer, String> hashContract = new Hashtable<Integer, String>();
		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT * from Contract where username='"
					+ GlobalBean.getUsersession() + "';";
			rsSet = stmtView.executeQuery(qry);
			while (rsSet.next()) {
				hashContract.put(
						rsSet.getInt("contractID"),
						rsSet.getString("contractname") + "\t"
								+ rsSet.getString("contractcreatedby") + "\t"
								+ rsSet.getDate("contractdate") + "\t"
								+ rsSet.getString("space") + "\t"
								+ rsSet.getDate("startdate") + "\t"
								+ rsSet.getDate("enddate") + "\t");
			}

			stmtView.close();
			rsSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hashContract;

	}

	public void addAdvertisement(AdvertisementBean sqlAdBean) {
		/*
		 * To add the Advertisement in the database. I have all the values in
		 * the bean now. I want to add the record in the database. But before I
		 * start adding it, I need to check the ad's date is not crossing
		 * contract's date. Also, I need to check the ad's file size doesn't
		 * cross the contract's file size limit.
		 */

		DBConnect.connectDB();

		try {
			stmtInsert = DBConnect.con.createStatement();

			// This logic is to convert the java date to sql date.

			java.sql.Date sqlStartDate = java.sql.Date.valueOf(sqlAdBean
					.getAdStartDate());
			java.sql.Date sqlEndDate = java.sql.Date.valueOf(sqlAdBean
					.getAdEndDate());
			String qty = "INSERT INTO Advertisement (adname, addesc, contractID, adstartdate, adenddate) values ('"
					+ sqlAdBean.getAdName()
					+ "','"
					+ sqlAdBean.getAdDesc()
					+ "', "
					+ sqlAdBean.getContractID()
					+ ", '"
					+ sqlStartDate
					+ "', '" + sqlEndDate + "');";
			System.out.println(qty);
			int rs = stmtInsert.executeUpdate(qty);
			if (rs == 1) {
				stmtView = DBConnect.con.createStatement();
				String qry = "SELECT MAX(adID) as adID from Advertisement";
				rsRead = stmtView.executeQuery(qry);
				rsRead.next();

				int adID = rsRead.getInt("adID");

				Statement stmt = DBConnect.con.createStatement();
				int res = stmt
						.executeUpdate("INSERT INTO AD_Product (adID, productID, adfilelocation, adSize) values("
								+ adID
								+ ", "
								+ sqlAdBean.getProductID()
								+ ", '"
								+ sqlAdBean.getFileLocation()
								+ "', '"
								+ sqlAdBean.getFileSize() + "')");
				if (res == 1) {
					valueInserted = true;
				} else {
					valueInserted = false;
				}

				stmt.close();
			}

			rsRead.close();
			stmtInsert.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getInsertedResult() {
		// Return the result of the value inserted.
		return valueInserted;
	}

	public long checkContractAdSize(int contractID) {
		long totalSize = 0;
		DBConnect.connectDB();

		try {
			stmtInsert = DBConnect.con.createStatement();

			String qry = "Select SUM(ad.adSize) as totalSize, c.contractID, a.adID from contract c, ad_product ad, advertisement a where a.adID = ad.adID and c.contractID="
					+ contractID
					+ " and c.contractID = a.contractID and c.username='"
					+ GlobalBean.getUsersession() + "';";
			// select SUM(ad.adSize) as totalSize, c.contractID, a.adID from
			// contract c, ad_product ad, advertisement a where a.adID = ad.adID
			// and c.contractID = a.contractID;
			// select SUM(ad.adSize) as totalSize, c.contractID, a.adID from
			// contract c, ad_product ad, advertisement a where a.adID = ad.adID
			// and c.contractID = a.contractID and c.contractID=2;
			System.out.println(qry);
			rsRead = stmtInsert.executeQuery(qry);
			rsRead.next();

			totalSize = rsRead.getLong("totalSize");

			stmtInsert.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalSize;
	}

	//
	// public AdvertisementBean[] viewAllAdvertisements() {
	//
	// DBConnect.connectDB();
	// int count = getAdvertisementIDCount();
	// AdvertisementBean[] objBean = new AdvertisementBean[count];
	// if (count == 0) {
	//
	// } else {
	//
	// try {
	// int i = 0;
	// // objBean = new ProductBean[count];
	// stmtView = DBConnect.con.createStatement();
	// String qry = "SELECT * FROM Advertisement;";
	// System.out.println(qry);
	// rsRead = stmtView.executeQuery(qry);
	// while (rsRead.next()) {
	// objBean[i] = new AdvertisementBean();
	// objBean[i].setAdId(rsRead.getString("adID"));
	// objBean[i].setAdName(rsRead.getString("adname"));
	// objBean[i].setAdDesc(rsRead.getString("addesc"));
	// i++;
	// }
	//
	// stmtView.close();
	// rsRead.close();
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// return objBean;
	//
	// }

	// public AdvertisementBean getAdvertisement(String adId) {
	// AdvertisementBean adBean = new AdvertisementBean();
	// try {
	// DBConnect.connectDB();
	// stmtView = DBConnect.con.createStatement();
	// String qry = "SELECT * FROM Advertisement where adID=" + adId + ";";
	// rsRead = stmtView.executeQuery(qry);
	// rsRead.next();
	// adBean.setAdId(rsRead.getString("adID"));
	// adBean.setAdName(rsRead.getString("adName"));
	// adBean.setAdDesc(rsRead.getString("adDesc"));
	// adBean.setAdDesc(rsRead.getString("adDesc"));
	//
	// stmtView.close();
	// rsRead.close();
	// return adBean;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return adBean;
	// }

	public ArrayList<AdvertisementBean> getAdsByProduct(ProductBean prodobj) {
		/*
		 * This function will retrieve Advertisement by product
		 */
		ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();
		int count = new ProductModel().getProductsCount();
		AdvertisementBean advertisment = new AdvertisementBean();
		if (count != 0) {
			try {
				DBConnect.connectDB();
				stmtView = DBConnect.con.createStatement();
				String qry = " SELECT a.adID,adname,addesc,adstartdate,adenddate from advertisement a,ad_product adp, product p "
						+ "where a.adid = adp.adid and adp.productid= p.productid "
						+ "and a.adID='" + prodobj.getCount() + "' order by adname;";
				System.out.println(qry);
				int i = 0;
				rsSet = stmtView.executeQuery(qry);
				while (rsSet.next()) {
					advertisment.setAdName(rsSet.getString("adname"));
					advertisment.setAdDesc(rsSet.getString("addesc"));
					// advertisment.setAdsize(rsSet.getString("adsize"));
					// advertisment
					// .setAdStartDate(rsRead.getString("adstartdate"));
					// advertisment.setAdEndDate(rsRead.getString("adenddate"));
					i++;
					advertisements.add(advertisment);
				}
				stmtView.close();
				rsSet.close();
				DBConnect.disconnectDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return advertisements;
	}

	public ArrayList<AdMerchantAdBean> getAdsbyMerchant(String adName) {

		DBConnect.connectDB();
		ArrayList<AdMerchantAdBean> adMerchantList = new ArrayList<AdMerchantAdBean>();
		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "select a.adid,adname,addesc,adstartdate,adenddate,longitude,latitude,address,city,state,zip "
					+ "from merchantlocation ml,ad_merchant aml,advertisement a where ml.merchantlocationID = aml.merchantlocationID "
					+ "and aml.adid = a.adid and adname like '%"
					+ adName
					+ "%' order by adname;";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				AdMerchantAdBean adMerchant = new AdMerchantAdBean();
				adMerchant.setAdID(rsRead.getString("adID"));
				adMerchant.setAdName(rsRead.getString("adname"));
				adMerchant.setAdDesc(rsRead.getString("addesc"));
				adMerchant.setAdStartDate(rsRead.getString("adstartdate"));
				adMerchant.setAdStartDate(rsRead.getString("adenddate"));
				adMerchant.setAddress(rsRead.getString("address"));
				adMerchant.setLatitude(rsRead.getString("latitude"));
				adMerchant.setLongitude(rsRead.getString("longitude"));
				adMerchant.setCity(rsRead.getString("city"));
				adMerchant.setState(rsRead.getString("state"));
				adMerchant.setZip(rsRead.getString("zip"));
				adMerchantList.add(adMerchant);
			}
			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adMerchantList;
	}

	public ArrayList<AdvertisementBean> getAdDetailsByProduct(
			ProductBean product) {

		ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();
		AdvertisementBean advertisement = new AdvertisementBean();
		try {
			DBConnect.connectDB();
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT ad.adID as adv, ad.adfilelocation as loc, ad.adSize as size,"
					+ " a.adstartdate as stdt, a.adenddate as eddt, a.adname as name, a.addesc as descrp "
					+ "from advertisement a, ad_product ad, product p where a.adID=ad.adID and ad.productID=p.productID and p.productID="
					+ product.getCount() + " order by name;";
			rsSet = stmtView.executeQuery(qry);

			while (rsSet.next()) {
				advertisement.setAdId(rsSet.getString("adv"));
				advertisement.setFileLocation(rsSet.getString("loc"));
				advertisement.setAdsize(rsSet.getString("size"));
				advertisement.setAdStartDate(rsSet.getString("stdt"));
				advertisement.setAdEndDate(rsSet.getString("eddt"));
				advertisement.setAdName(rsSet.getString("name"));
				advertisement.setAdDesc(rsSet.getString("descrp"));
				// j++;
				advertisements.add(advertisement);
			}
			stmtView.close();
			rsSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	public AdvertisementBean getAdvertisement(String adId) {
		AdvertisementBean adBean = new AdvertisementBean();
		try {
			DBConnect.connectDB();
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT ad.adID as adv, ad.adfilelocation as loc, ad.adSize as size,"
					+ " a.adstartdate as stdt, a.adenddate as eddt, a.adname as name, a.addesc as descrp "
					+ "from advertisement a, ad_product ad, product p where a.adID=ad.adID and ad.productID=p.productID"
					+ " and a.adID=" + adId + " order by name;";

			// String qry = "SELECT * FROM Advertisement where adID=" + adId +
			// ";";
			rsRead = stmtView.executeQuery(qry);
			rsRead.next();
			adBean.setAdId(rsRead.getString("adv"));
			adBean.setFileLocation(rsRead.getString("loc"));
			adBean.setAdsize(rsRead.getString("size"));
			adBean.setAdStartDate(rsRead.getString("stdt"));
			adBean.setAdEndDate(rsRead.getString("eddt"));
			adBean.setAdName(rsRead.getString("name"));
			adBean.setAdDesc(rsRead.getString("descrp"));
			stmtView.close();
			rsRead.close();
			return adBean;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adBean;
	}

	public ArrayList<AdvertisementBean> viewAllAdvertisements() {

		DBConnect.connectDB();
		int count = getAdvertisementIDCount();
		ArrayList<AdvertisementBean> advertisments = new ArrayList<AdvertisementBean>();
		AdvertisementBean[] objBean = new AdvertisementBean[count];
		if (count == 0) {

		} else {

			try {
				int i = 0;
				stmtView = DBConnect.con.createStatement();
				// String qry = "SELECT * FROM Advertisement;";
				String qry = "SELECT ad.adID as adv, ad.adfilelocation as loc, ad.adSize as size,"
						+ " a.adstartdate as stdt, a.adenddate as eddt, a.adname as name, a.addesc as descrp "
						+ "from advertisement a, ad_product ad, product p where a.adID=ad.adID and ad.productID=p.productID"
						+ " order by name;";
				System.out.println(qry);
				rsRead = stmtView.executeQuery(qry);
				while (rsRead.next()) {

					objBean[i] = new AdvertisementBean();
					objBean[i].setAdId(rsRead.getString("adv"));
					objBean[i].setFileLocation(rsRead.getString("loc"));
					objBean[i].setAdsize(rsRead.getString("size"));
					objBean[i].setAdStartDate(rsRead.getString("stdt"));
					objBean[i].setAdEndDate(rsRead.getString("eddt"));
					objBean[i].setAdName(rsRead.getString("name"));
					objBean[i].setAdDesc(rsRead.getString("descrp"));
					advertisments.add(objBean[i]);
					i++;
				}
				stmtView.close();
				rsRead.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return advertisments;
	}

	public ArrayList<AdMerchantAdBean> getAdsbyMerchantID(String adID) {

		DBConnect.connectDB();
		ArrayList<AdMerchantAdBean> adMerchantList = new ArrayList<AdMerchantAdBean>();
		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "select a.adid,adname,addesc,adstartdate,adenddate,longitude,latitude,address,city,state,zip "
					+ "from merchantlocation ml,ad_merchant aml,advertisement a where ml.merchantlocationID = aml.merchantlocationID "
					+ "and aml.adid = a.adid and a.adId='" + adID + "' order by adname;";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				AdMerchantAdBean adMerchant = new AdMerchantAdBean();
				adMerchant.setAdID(rsRead.getString("adID"));
				adMerchant.setAdName(rsRead.getString("adname"));
				adMerchant.setAdDesc(rsRead.getString("addesc"));
				adMerchant.setAdStartDate(rsRead.getString("adstartdate"));
				adMerchant.setAdStartDate(rsRead.getString("adenddate"));
				adMerchant.setAddress(rsRead.getString("address"));
				adMerchant.setLatitude(rsRead.getString("latitude"));
				adMerchant.setLongitude(rsRead.getString("longitude"));
				adMerchant.setCity(rsRead.getString("city"));
				adMerchant.setState(rsRead.getString("state"));
				adMerchant.setZip(rsRead.getString("zip"));
				adMerchantList.add(adMerchant);
			}
			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adMerchantList;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2,
			char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public ArrayList<AdMerchantAdBean> getAdsbyMerchantNearBy(String adName,
			String latitude, String longitude) {

		DBConnect.connectDB();
		ArrayList<AdMerchantAdBean> adMerchantList = new ArrayList<AdMerchantAdBean>();
		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "select a.adid,adname,addesc,adstartdate,adenddate,longitude,latitude,address,city,state,zip "
					+ "from merchantlocation ml,ad_merchant aml,advertisement a where ml.merchantlocationID = aml.merchantlocationID "
					+ "and aml.adid = a.adid and adname like '%"
					+ adName
					+ "%' order by adname;";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				AdMerchantAdBean adMerchant = new AdMerchantAdBean();
				adMerchant.setAdID(rsRead.getString("adID"));
				adMerchant.setAdName(rsRead.getString("adname"));
				adMerchant.setAdDesc(rsRead.getString("addesc"));
				adMerchant.setAdStartDate(rsRead.getString("adstartdate"));
				adMerchant.setAdStartDate(rsRead.getString("adenddate"));
				adMerchant.setAddress(rsRead.getString("address"));
				adMerchant.setLatitude(rsRead.getString("latitude"));
				adMerchant.setLongitude(rsRead.getString("longitude"));
				adMerchant.setCity(rsRead.getString("city"));
				adMerchant.setState(rsRead.getString("state"));
				adMerchant.setZip(rsRead.getString("zip"));

				double distance = distance(Double.parseDouble(latitude),
						Double.parseDouble(longitude),
						Double.parseDouble(adMerchant.getLatitude()),
						Double.parseDouble(adMerchant.getLongitude()), 'M');
				System.out.println("Distance: " + distance + " "
						+ adMerchant.adID);
				if (distance <= 10.0d) {
					System.out.println("IN: " + distance + " "
							+ adMerchant.adID);
					adMerchantList.add(adMerchant);
				}
			}
			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adMerchantList;
	}
	
	public ArrayList<AdMerchantAdBean> getAdsbyMerchantNearByUserSubscription(String username,
			String latitude, String longitude) {

		DBConnect.connectDB();
		ArrayList<AdMerchantAdBean> adMerchantList = new ArrayList<AdMerchantAdBean>();
		try {
			stmtView = DBConnect.con.createStatement();
			String qry = "select a.adid,adname,addesc,adstartdate,adenddate,longitude,latitude,address,city,state,zip "
					+ "from merchantlocation ml,ad_merchant aml,advertisement a , channel_ad ca, usersubscription u" +
					   " where ml.merchantlocationID = aml.merchantlocationID "
					+ "and aml.adid = a.adid and ca.channelid=u.channelid and ca.adid=a.adid and u.username='" + username + "'";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				AdMerchantAdBean adMerchant = new AdMerchantAdBean();
				adMerchant.setAdID(rsRead.getString("adID"));
				adMerchant.setAdName(rsRead.getString("adname"));
				adMerchant.setAdDesc(rsRead.getString("addesc"));
				adMerchant.setAdStartDate(rsRead.getString("adstartdate"));
				adMerchant.setAdStartDate(rsRead.getString("adenddate"));
				adMerchant.setAddress(rsRead.getString("address"));
				adMerchant.setLatitude(rsRead.getString("latitude"));
				adMerchant.setLongitude(rsRead.getString("longitude"));
				adMerchant.setCity(rsRead.getString("city"));
				adMerchant.setState(rsRead.getString("state"));
				adMerchant.setZip(rsRead.getString("zip"));

				double distance = distance(Double.parseDouble(latitude),
						Double.parseDouble(longitude),
						Double.parseDouble(adMerchant.getLatitude()),
						Double.parseDouble(adMerchant.getLongitude()), 'M');
				System.out.println("Distance: " + distance + " "
						+ adMerchant.adID);
				if (distance <= 10.0d) {
					System.out.println("IN: " + distance + " "
							+ adMerchant.adID);
					adMerchantList.add(adMerchant);
				}
			}
			stmtView.close();
			rsRead.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adMerchantList;
	}
}
