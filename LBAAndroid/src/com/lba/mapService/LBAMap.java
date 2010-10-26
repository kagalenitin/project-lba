package com.lba.mapService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.xml.DomRepresentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;
import com.lba.R;
import com.lba.beans.AdMerchantAdBean;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */

public class LBAMap extends MapActivity implements LocationListener {

	MapView mapView;
	MapController mc;
	GeoPoint p;

	private String adName = "";
	private String longitude = "";
	private String latitude = "";
	// ArrayList latitudes = new ArrayList();
	// ArrayList longitudes = new ArrayList();

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
		setContentView(R.layout.map);
		this.setTitle("Location Based Advertisement - Map");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		advertisements = new ArrayList<AdMerchantAdBean>();
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			adName = b.getString("adName");
			if (adName != null || adName != "") {
				advertisements = getAdsByMerchant(adName);
			} else {
				Toast.makeText(LBAMap.this, "" + "Service not available",
						Toast.LENGTH_SHORT).show();
			}
			latitude = "37.422006";
			longitude = "-122.084095";
			// longitude = b.getString("longitude");
		} else {
			latitude = "37.422006";
			longitude = "-122.084095";

		}

		mapView = (MapView) findViewById(R.id.mapView);
		LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom);
		@SuppressWarnings("deprecation")
		View zoomView = mapView.getZoomControls();

		zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mapView.setStreetView(true);
		// mapView.setSatellite(true);
		mapView.displayZoomControls(true);

		mc = mapView.getController();
		// String coordinates[] = {"1.352566007", "103.78921587"};
		// String coordinates[] = {"35.37789235", "-122.465325"};

		for (int i = 0; i < 3; i++) {

			String coordinates[] = { latitude + 50, longitude + 30 };
			double lat = Double.parseDouble(coordinates[0]);
			double lng = Double.parseDouble(coordinates[1]);

			p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

			mc.animateTo(p);
			mc.setZoom(15);

		}
		// ---Add a location marker---
		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		// listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		mapView.invalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	class MapOverlay extends com.google.android.maps.Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.map_marker);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);

			/*
			 * Bitmap bmp1 = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.jcpenney); canvas.drawBitmap(bmp1, screenPts.x + 100,
			 * screenPts.y - 50 + 100, null);
			 * 
			 * Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.starbuckslogo_small); canvas.drawBitmap(bmp2,
			 * screenPts.x + 50, screenPts.y - 50 + 50, null);
			 * 
			 * Bitmap bmp3 = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.food_l2_43); canvas.drawBitmap(bmp3, screenPts.x + 20,
			 * screenPts.y - 50 + 20, null);
			 */
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// ---when user lifts his finger---
			if (event.getAction() == 1) {
				GeoPoint p = mapView.getProjection().fromPixels(
						(int) event.getX(), (int) event.getY());
				Toast.makeText(
						getBaseContext(),
						p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6()
								/ 1E6, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			// String currentLocation = "Lat: " + lat + " Lng: " + lng;
			// txted.setText(currentLocation);
			GeoPoint p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mc.animateTo(p);
			// this.startActivity(new Intent(Intent.ACTION_VIEW,
			// Uri.parse("http://maps.google.com/maps?f=d&saddr="+lat+","+lng+"&daddr=37.6,-121.11111")));
			// Uri.parse("http://maps.google.com/maps?f=d&saddr=37.4,-121.9&daddr=37.6,-121.11111")));
			// Uri.parse("http://maps.google.com/maps?f=d&saddr=37.4,-121.9&daddr=Bellevue, WA&hl=en")));
		}
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}