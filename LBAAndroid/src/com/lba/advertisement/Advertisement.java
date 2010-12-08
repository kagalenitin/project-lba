/**
 * 
 */
package com.lba.advertisement;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.AdvertisementBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.AdvertisementResourceClient;

/**
 * This class mainly represents the advertisement of the AdSpot.
 * 
 * @author payal
 * 
 */
public class Advertisement extends Activity {

	private ListView itemListView;
	private TextView lblAdCode;
	LazyAdapter adapter;
	String uname;

	ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();
	private String adPath = "";

	String adLocation = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		System.out.println("tomcatServer "
				+ this.getResources().getString(R.string.tomcatServer));
		adPath = this.getResources().getString(R.string.tomcatServer);

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.advertisementmain);
		this.setTitle("AdSpot - Ads");
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.shopping_bag);

		lblAdCode = (TextView) findViewById(R.id.AdCode);
		itemListView = (ListView) findViewById(R.id.list);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		String productId = null;
		if (b != null) {
			productId = b.getString("productId");
			uname = b.getString("uname");
			if (productId != null) {
				if (!(productId.equalsIgnoreCase(""))) {
					String productName = b.getString("productName");
					lblAdCode.setText(productName);
					advertisements = getAdsByProduct(productId);
					adapter = new LazyAdapter(this, advertisements);
				} else {
					advertisements = getAdvertisements();
					adapter = new LazyAdapter(this, advertisements);
				}
			}
		}

		itemListView.setAdapter(adapter);

		itemListView.setClickable(true);
		itemListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(Advertisement.this, AdDetail.class);
				Bundle b = new Bundle();
				adLocation = adPath
						+ advertisements.get(position).getFileLocation();
				b.putString("uname", uname);
				b.putString("AdId", adLocation);
				b.putString("adId", advertisements.get(position).getAdId());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		itemListView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		adapter.imageLoader.stopThread();
		itemListView.setAdapter(null);
		super.onDestroy();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

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
			Intent intent = new Intent(Advertisement.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(Advertisement.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

	/**
	 * This method gets all the advertisement from the rest service.
	 * 
	 * @return List of advertisement in arrayList.
	 */
	public ArrayList<AdvertisementBean> getAdvertisements() {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();

		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisements();
			if (representation != null) {
				advertisements = advertisementResource
						.getAdvertisementsFromXml(representation);
			} else {
				advertisements = new ArrayList<AdvertisementBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	/**
	 * This method gets all the ads by products.
	 * 
	 * @param productId
	 * @return List of Advertisements
	 */
	public ArrayList<AdvertisementBean> getAdsByProduct(String productId) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();

		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisementsByProduct(productId);
			if (representation != null) {
				advertisements = advertisementResource
						.getAdvertisementsFromXml(representation);
			} else {
				advertisements = new ArrayList<AdvertisementBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

}