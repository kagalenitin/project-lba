package com.LBA.Advertiser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.LBA.Advertiser.bean.ChannelBean;
import com.LBA.Advertiser.bean.ChannelSubscriptionBean;

public class ChannelSubscriptionModel {
	static boolean valueInserted;
	static boolean valueDeleted;
	static boolean valueSelected;
	static Statement stmtInsert = null;
	static Statement stmtDelete = null;
	static Statement stmtView = null;
	static ResultSet rsSet = null;
	static ResultSet rsRead = null;
	ChannelSubscriptionBean channelSubscriptionBean = new ChannelSubscriptionBean();

	public void insertUserSubscription(ChannelSubscriptionBean beanObj) {
		DBConnect.connectDB();
		try {
			stmtInsert = DBConnect.con.createStatement();
			String qry = "INSERT INTO usersubscription (username, channelId) values "
					+ "('"
					+ beanObj.getUserId()
					+ "', '"
					+ beanObj.getChanneld() + "')";
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
			e.printStackTrace();
		}
	}

	public int getSubscriptionByUserByChannel(ChannelSubscriptionBean beanObject) {
		DBConnect.connectDB();
		int count = 0;
		try {
			stmtView = DBConnect.con.createStatement();

			String qryCount = "SELECT count(*) as cnt FROM usersubscription where username='"
					+ beanObject.getUserId()
					+ "' and channelId='"
					+ beanObject.getChanneld() + "';";
			System.out.println(qryCount);
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

	public int getSubscriptionExistance(String username) {
		DBConnect.connectDB();
		int count = 0;
		try {
			stmtView = DBConnect.con.createStatement();

			String qryCount = "SELECT * FROM usersubscription where username='"
					+ username + "';";
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

	public boolean deleteUserSubscription(ChannelSubscriptionBean beanObject) {

		try {
			DBConnect.connectDB();

			stmtDelete = DBConnect.con.createStatement();
			String qry = "DELETE FROM usersubscription where username='"
					+ beanObject.getUserId() + "' and channelId='"
					+ beanObject.getChanneld() + "';";
			System.out.println(qry);
			int res = stmtDelete.executeUpdate(qry);
			if (res == 1) {
				valueDeleted = true;
			} else {
				valueDeleted = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valueDeleted;
	}

	public ArrayList<ChannelBean> getChannelBySubscription(String username) {

		try {
			DBConnect.connectDB();
			ArrayList<ChannelBean> channels = new ArrayList<ChannelBean>();
			stmtView = DBConnect.con.createStatement();
			String qry = "Select * FROM Channel where channelId in ("
					+ "select channelId from usersubscription where username='"
					+ username + "');";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				ChannelBean channelBean = new ChannelBean();
				System.out.println("select==="
						+ rsRead.getString("channelname"));
				channelBean.setChannelid(rsRead.getString("channelID"));
				channelBean.setChannelname(rsRead.getString("channelname"));
				channelBean.setChanneldescription(rsRead
						.getString("channeldescription"));
				channels.add(channelBean);
			}
			stmtView.close();
			rsRead.close();
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
