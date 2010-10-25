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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class Product extends Activity { // implements OnClickListener{

	private ListView productListView;
	private TextView lblChannelCode;
	static ArrayList<ProductBean> products = new ArrayList<ProductBean>();

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
		this.setTitle("Location Based Advertisement");
		setContentView(R.layout.product);
		lblChannelCode = (TextView) findViewById(R.id.ChannelCode);
		productListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			String channelId = b.getString("channelId");
			if (channelId != null) {
				if (!(channelId.equalsIgnoreCase(""))) {
					products = getProductsByChannel(channelId);
				} else {
					lblChannelCode.setText("Product List:");
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
				b.putString("productId", String.valueOf(((ProductBean) products
						.get(position)).getCount()));
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}