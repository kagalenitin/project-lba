package com.LBA.Advertiser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.LBA.Advertiser.bean.MobileUserBean;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class MobileUserModel {
	static boolean valueInserted;
	static boolean valueDeleted;
	static Statement stmtInsert = null;
	static Statement stmtView = null;
	static ResultSet rsSet = null;
	static ResultSet rsRead = null;
	MobileUserBean mobileUserBean = new MobileUserBean();

	public Boolean createMobileUser(MobileUserBean beanObj) {
		DBConnect.connectDB();
		try {
			stmtInsert = DBConnect.con.createStatement();
			String qry = "INSERT INTO mobileuser (username, password, firstname, lastname, address, phone, email) values"
					+ "('"
					+ beanObj.getUsername()
					+ "', '"
					+ beanObj.getPassword()
					+ "', '"
					+ beanObj.getFirstName()
					+ "' , '"
					+ beanObj.getLastName()
					+ "' , '"
					+ beanObj.getAddress()
					+ "' , '"
					+ beanObj.getPhone()
					+ "' , '" + beanObj.getEmail() + "');";
			System.out.println(qry);
			int res = stmtInsert.executeUpdate(qry);

			if (res == 1) {
				valueInserted = true;
			} else {
				valueInserted = false;
			}

			stmtInsert.close();
			DBConnect.disconnectDB();
		} catch (MySQLIntegrityConstraintViolationException e) {
			System.out.println("Primay key contraint violation");
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int getMobileUserCount() {
		DBConnect.connectDB();
		int count = 0;
		try {
			stmtView = DBConnect.con.createStatement();
			String qryCount = "SELECT COUNT(*) as cnt FROM mobileuser;";
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

	public boolean updateMobileUser(MobileUserBean beanObject) {

		try {
			DBConnect.connectDB();
			stmtInsert = DBConnect.con.createStatement();
			String qry = "UPDATE mobileuser SET password='"
					+ beanObject.getPassword() + "', firstName='"
					+ beanObject.getFirstName() + "'" + ", lastname='"
					+ beanObject.getLastName() + "'" + ", address='"
					+ beanObject.getAddress() + "'" + ", phone='"
					+ beanObject.getPhone() + "'" + ", email='"
					+ beanObject.getEmail() + "'" + " WHERE username='"
					+ beanObject.getUsername() + "';";
			System.out.println(qry);
			int res = stmtInsert.executeUpdate(qry);
			if (res == 1) {
				valueInserted = true;
			} else {
				valueInserted = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valueInserted;
	}

	public boolean deleteMobileUser(MobileUserBean beanObject) {

		try {
			DBConnect.connectDB();

			stmtInsert = DBConnect.con.createStatement();
			String qry = "DELETE FROM mobileuser where username='"
					+ beanObject.getUsername() + "';";
			System.out.println(qry);
			int res = stmtInsert.executeUpdate(qry);
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

	public MobileUserBean getMobileUser(String username) {
		try {
			DBConnect.connectDB();
			stmtView = DBConnect.con.createStatement();
			String qry = "SELECT * FROM MobileUser where username='" + username
					+ "';";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			if(rsRead.next()){
				mobileUserBean.setUsername(rsRead.getString("username"));
				mobileUserBean.setPassword(rsRead.getString("password"));
				mobileUserBean.setFirstName(rsRead.getString("firstname"));
				mobileUserBean.setLastName(rsRead.getString("lastname"));
				mobileUserBean.setAddress(rsRead.getString("address"));
				mobileUserBean.setPhone(rsRead.getString("phone"));
				mobileUserBean.setEmail(rsRead.getString("email"));
			}
			
			stmtView.close();
			rsRead.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mobileUserBean;
	}
}
