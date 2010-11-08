package com.LBA.Advertiser.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

public class DBConnect {
	public static Connection con = null;

	public static void connectDB() {
		try {
			String username = "root";
			String password = "";
			String url = "jdbc:mysql://localhost/locateAd";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			try {
				throw new ServletException("JDBC Driver not found.", e);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void disconnectDB() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("DB not able to close");
			}
		}
	}

}
