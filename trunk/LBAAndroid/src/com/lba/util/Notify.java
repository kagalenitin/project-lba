package com.lba.util;

import java.util.Random;

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
import android.widget.Button;

import com.lba.R;

public class Notify extends Activity {

	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID;
	private Location currentLocation;
	private double Latitude;
	private double Longitude;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify);

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
		Context context = getApplicationContext();

		Button start = (Button) findViewById(R.id.btn_showsample);
		Button cancel = (Button) findViewById(R.id.btn_clear);

		start.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Context context = getApplicationContext();
				CharSequence contentTitle = "LBA Notification";
				CharSequence contentText = "Ad Name";

				Intent notifyIntent = new Intent(context, Notify.class);

				PendingIntent intent = PendingIntent.getActivity(Notify.this,
						0, notifyIntent,
						android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

				notifyDetails.setLatestEventInfo(context, contentTitle,
						contentText, intent);

				mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
						notifyDetails);

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);
			}
		});
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {

			final Notification notifyDetails = new Notification(
					R.drawable.arrow, "LBA!", System.currentTimeMillis());

			Context context = getApplicationContext();
			CharSequence contentTitle = "LBA Notification";
			CharSequence contentText = "LBA";

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

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}