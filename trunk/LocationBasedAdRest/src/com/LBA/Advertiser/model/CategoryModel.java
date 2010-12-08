/**
 * 
 */
package com.LBA.Advertiser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.LBA.Advertiser.bean.CategoryBean;

/**
 * @author payalpatel
 * 
 */
public class CategoryModel {

	static Statement stmtView = null;
	static ResultSet rsRead = null;

	public ArrayList<CategoryBean> getCategories() {

		try {
			DBConnect.connectDB();
			ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
			stmtView = DBConnect.con.createStatement();
			String qry = "Select * FROM Category order by categoryname";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				CategoryBean categoryBean = new CategoryBean();
				categoryBean.setCategoryID(rsRead.getString("categoryID"));
				categoryBean.setCategoryName(rsRead.getString("categoryName"));
				categories.add(categoryBean);
			}
			stmtView.close();
			rsRead.close();
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CategoryBean getCategoryByName(String categoryName) {

		if(categoryName!=null){
			System.out.println("Replace");
			categoryName = categoryName.replace("+", " ");
		}
		try {
			DBConnect.connectDB();
			CategoryBean categoryBean = new CategoryBean();
			stmtView = DBConnect.con.createStatement();
			String qry = "Select * FROM Category where categoryname='"
					+ categoryName + "'";
			System.out.println(qry);
			rsRead = stmtView.executeQuery(qry);
			while (rsRead.next()) {
				categoryBean.setCategoryID(rsRead.getString("categoryID"));
				categoryBean.setCategoryName(rsRead.getString("categoryName"));
			}
			stmtView.close();
			rsRead.close();
			return categoryBean;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
