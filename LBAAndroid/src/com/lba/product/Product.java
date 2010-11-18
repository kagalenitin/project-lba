/**
 * 
 */
package com.lba.product;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.advertisement.Advertisement;
import com.lba.beans.ProductBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.ProductResourceClient;

/**
 * @author payal
 * 
 */
public class Product extends Activity { // implements OnClickListener{

	private ListView productListView;
	private TextView lblChannelCode;
	static ArrayList<ProductBean> products = new ArrayList<ProductBean>();
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

	public static ArrayList<ProductBean> getProductsByChannel(String channelId) {

		ProductResourceClient itemConnect = new ProductResourceClient();
		try {
			DomRepresentation representation = itemConnect
					.retrieveProductbyChannel(channelId);
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
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.product);
		this.setTitle("AdSpot - Product");
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.topview48);

		lblChannelCode = (TextView) findViewById(R.id.ChannelCode);
		productListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			String channelId = b.getString("channelId");
			if (channelId != null) {
				if (!(channelId.equalsIgnoreCase(""))) {
					String channelName = b.getString("channelName");
					lblChannelCode.setText(channelName);
					products = getProductsByChannel(channelId);
				} else {
					products = getProducts();
				}
			}
		}
		adapter = new ProductAdapter(this, products);
		productListView.setAdapter(adapter);

		// Set Click Listener
		productListView.setClickable(true);
		productListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(Product.this, Advertisement.class);
				Bundle b = new Bundle();
				b.putString("uname", uname);
				b.putString("productId", String.valueOf(((ProductBean) products
						.get(position)).getCount()));
				b.putString("productName", String
						.valueOf(((ProductBean) products.get(position))
								.getProductName()));
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
			Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Product.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(Product.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}