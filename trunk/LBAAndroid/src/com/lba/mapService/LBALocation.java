package com.lba.mapService;

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
import com.lba.advertisement.AdDetail;
import com.lba.beans.AdMerchantAdBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */

public class LBALocation extends Activity {

	private EditText elAdname;
	// private EditText etLongitude;
	private Button btnMap;
	String uname;
	AdAdapter adapter;
	private ListView adListView;
	private String adPath = "http://192.168.1.72:8080";
	String adLocation = "";

	ArrayList<AdMerchantAdBean> advertisements = new ArrayList<AdMerchantAdBean>();

	public ArrayList<AdMerchantAdBean> getAdsByMerchant(String adName) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisementsByMerchant(adName);
			if (representation != null) {
				advertisements = advertisementResource
						.getAdvertisementsByMerchantFromXml(representation);
			} else {
				advertisements = new ArrayList<AdMerchantAdBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.mapmain);
		this.setTitle("Location Based Advertisement - Map");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		// Get the EditText and Button References
		elAdname = (EditText) findViewById(R.id.adName);
		// etLongitude = (EditText) findViewById(R.id.longitude);
		btnMap = (Button) findViewById(R.id.map_button);
		adListView = (ListView) findViewById(R.id.ListView01);

		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();

		if (b != null) {
			uname = b.getString("uname");
		}
		// Set Click Listener
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LBALocation.this, LBAMap.class);
				Bundle b = new Bundle();
				b.putString("uname", uname);
				b.putString("adName", elAdname.getText().toString());
				// b.putString("longitude", etLongitude.getText().toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		elAdname.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

				String adName = elAdname.getText().toString();
				advertisements = getAdsByMerchant(adName);
				adapter = new AdAdapter(LBALocation.this, advertisements);
				adListView.setAdapter(adapter);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});

		// Set Click Listener
		adListView.setClickable(true);
		adListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(LBALocation.this, AdDetail.class);
				Bundle b = new Bundle();
				String adLocation = "";
				b.putString("uname", uname);
				b.putString("AdId", adLocation);
				b.putString("adId", advertisements.get(position).getAdID());
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
			Intent intent = new Intent(LBALocation.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
			intent = new Intent(LBALocation.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

}