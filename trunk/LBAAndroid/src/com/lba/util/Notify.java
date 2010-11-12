package com.lba.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.lba.R;
import com.lba.beans.AdMerchantAdBean;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */
public class Notify extends Activity {

	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID;
	private Location currentLocation;
	private double Latitude;
	private double Longitude;
	private ArrayList<AdMerchantAdBean> advertisements = new ArrayList<AdMerchantAdBean>();
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

				mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);
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

			mNotificationManager.notify(
					SIMPLE_NOTFICATION_ID + new Random().nextInt(),
					notifyDetails);
		}
	}
}