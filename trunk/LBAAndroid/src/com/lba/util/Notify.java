package com.lba.util;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.lba.R;
import com.lba.beans.AdMerchantAdBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */
public class Notify extends Activity {

	private NotificationManager mNotificationManager;
	private Location currentLocation;
	private double Latitude;
	private double Longitude;
	private ArrayList<AdMerchantAdBean> advertisements = new ArrayList<AdMerchantAdBean>();
	private ArrayList<Integer> notificationID = new ArrayList<Integer>();
	String uname;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.notify);
		this.setTitle("Location Based Advertisement - Notification");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
		}
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener myLocationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 500.0f, myLocationListener);
		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (currentLocation == null) {
			Latitude = 37.3348412;
			Longitude = -121.8849198;
		} else {
			Latitude = currentLocation.getLatitude();
			Longitude = currentLocation.getLongitude();
		}

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		final Notification notifyDetails = new Notification(R.drawable.arrow,
				"New Alert, LBA", System.currentTimeMillis());

		long[] vibrate = { 100, 100, 200, 300 };
		notifyDetails.vibrate = vibrate;
		notifyDetails.defaults = Notification.DEFAULT_ALL;

		Button start = (Button) findViewById(R.id.btn_showsample);
		Button cancel = (Button) findViewById(R.id.btn_clear);

		start.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// LocationListener myLocationListener = new
				// MyLocationListener();
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1000L, 500.0f,
						new MyLocationListener());
				currentLocation = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (currentLocation == null) {
					Latitude = 37.3348412;
					Longitude = -121.8849198;
				} else {
					Latitude = currentLocation.getLatitude();
					Longitude = currentLocation.getLongitude();
				}
				advertisements = getAdsBySubscriptionDistance(uname,
						String.valueOf(Latitude), String.valueOf(Longitude));
				notifyAd();

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				for (int i = 0; i < notificationID.size(); i++) {
					mNotificationManager.cancel(notificationID.get(i));
				}
			}
		});
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

			if (location != null) {
				double lat = location.getLatitude();
				String.valueOf(lat);
				double lng = location.getLongitude();
				String.valueOf(lng);
				new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

				advertisements = getAdsBySubscriptionDistance(uname,
						String.valueOf(lat), String.valueOf(lng));

				notifyAd();
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public ArrayList<AdMerchantAdBean> getAdsBySubscriptionDistance(
			String username, String latitude, String longitude) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisementsBySubscriptionDistance(username,
							latitude, longitude);
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

	private void notifyAd() {

		for (int i = 0; i < advertisements.size(); i++) {

			System.out.println("AD:" + advertisements.get(i).getAdID());

			final Notification notifyDetails = new Notification(
					R.drawable.arrow, "LBA!", System.currentTimeMillis());

			Context context = getApplicationContext();
			CharSequence contentTitle = "LBA Notification";
			CharSequence contentText = advertisements.get(i).getAdName();

			Intent notifyIntent = new Intent(context, Notify.class);

			PendingIntent intent = PendingIntent
					.getActivity(Notify.this, 0, notifyIntent,
							android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

			notifyDetails.setLatestEventInfo(context, contentTitle,
					contentText, intent);

			notificationID.add(Integer
					.parseInt(advertisements.get(i).getAdID()));

			mNotificationManager.notify(
					Integer.parseInt(advertisements.get(i).getAdID()),
					notifyDetails);
		}
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
			Intent intent = new Intent(Notify.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(Notify.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}