/**
 * 
 */
package com.lba.search;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lba.R;
import com.lba.advertisement.Advertisement;
import com.lba.beans.ProductBean;
import com.lba.service.ProductResourceClient;

/**
 * @author payal
 * 
 */
public class SearchProduct extends Activity { // implements OnClickListener{

	private ListView productListView;
	private TextView lblChannelCode;
	private Button btnSearch;
	private EditText elProductName;
	static ArrayList<ProductBean> products = new ArrayList<ProductBean>();
	String productName;

	ProductAdapter adapter;

	public static ArrayList<ProductBean> getProducts() {

		ProductResourceClient itemConnect = new ProductResourceClient();
		try {
			DomRepresentation representation = itemConnect.retrieveProducts();
			if (representation != null) {
				products = itemConnect.getProductsFromXml(representation);
			} else {
				products = new ArrayList<ProductBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return products;
	}

	public static ArrayList<ProductBean> getProductsByName(String productId) {

		ProductResourceClient itemConnect = new ProductResourceClient();
		try {
			DomRepresentation representation = itemConnect
					.retrieveProductbyName(productId);
			if (representation != null) {
				products = itemConnect.getProductsFromXml(representation);
			} else {
				products = new ArrayList<ProductBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return products;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.searchproduct);
		this.setTitle("Location Based Advertisement - Search");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);


		productListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			productName = b.getString("productName");
			if (productName != null) {
				if (!(productName.equalsIgnoreCase(""))) {
					products = getProductsByName(productName);
					// products = getProducts();
				} else {
					products = getProducts();
				}
			}
		}
		adapter = new ProductAdapter(this, products);
		productListView.setAdapter(adapter);
		elProductName = (EditText) findViewById(R.id.productName);
		btnSearch = (Button) findViewById(R.id.searchbutton);

		// Set Click Listener
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchProduct.this,
						SearchProduct.class);
				Bundle b = new Bundle();
				String prodName = elProductName.getText().toString();
				if (prodName != null)
					b.putString("productName", elProductName.getText()
							.toString());
				else
					b.putString("productName", "");
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		// Set Click Listener
		productListView.setClickable(true);
		productListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(SearchProduct.this,
						Advertisement.class);
				Bundle b = new Bundle();
				b.putString("productId", String.valueOf(((ProductBean) products
						.get(position)).getCount()));
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}