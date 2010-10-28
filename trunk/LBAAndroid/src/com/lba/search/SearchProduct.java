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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lba.R;
import com.lba.advertisement.Advertisement;
import com.lba.beans.ProductBean;
import com.lba.home.WelcomeUser;
import com.lba.mapService.LBALocation;
import com.lba.service.ProductResourceClient;

/**
 * @author payal
 * 
 */
public class SearchProduct extends Activity { // implements OnClickListener{

	private ListView productListView;
	private Button btnSearch;
	private EditText elProductName;
	static ArrayList<ProductBean> products = new ArrayList<ProductBean>();
	String productName;
	int textlength = 0;
	String uname;
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
		elProductName = (EditText) findViewById(R.id.productName);
		productListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			productName = b.getString("productName");
			if (productName != null) {
				elProductName.setText(productName);
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
		btnSearch = (Button) findViewById(R.id.searchbutton);

		// Set Click Listener
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String prodName = elProductName.getText().toString();
				products = getProductsByName(prodName);
				adapter = new ProductAdapter(SearchProduct.this, products);
				productListView.setAdapter(adapter);
			}
		});

		elProductName.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String prodName = elProductName.getText().toString();
				products = getProductsByName(prodName);
				adapter = new ProductAdapter(SearchProduct.this, products);
				productListView.setAdapter(adapter);
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
				b.putString("uname", uname);
				b.putString("productId", String.valueOf(((ProductBean) products
						.get(position)).getCount()));
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.commonmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(SearchProduct.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
			intent = new Intent(SearchProduct.this, LBALocation.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}