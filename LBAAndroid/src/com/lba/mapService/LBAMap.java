package com.lba.mapService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.xml.DomRepresentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */

public class LBAMap extends MapActivity implements LocationListener {

	MapView mapView;
	MapController mc;
	GeoPoint p;
	ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
	ArrayList<Integer> draw = new ArrayList<Integer>();

	String uname;
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
			uname = b.getString("uname");
			if (adName != null || adName != "") {
				advertisements = getAdsByMerchant(adName);
			} else {
				Toast.makeText(LBAMap.this, "" + "Service not available",
						Toast.LENGTH_SHORT).show();
			}

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

		mapView.setSatellite(false);
		mapView.displayZoomControls(true);
		mc = mapView.getController();

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,
				this);

		for (int i = 0; i < advertisements.size(); i++) {
			latitude = advertisements.get(i).getLatitude();
			longitude = advertisements.get(i).getLongitude();
			String coordinates[] = { latitude + 50, longitude + 30 };
			double lat = Double.parseDouble(coordinates[0]);
			double lng = Double.parseDouble(coordinates[1]);
			p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			points.add(p);
			mc.animateTo(p);
			mc.setZoom(10);
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

			for (int i = 0; i < advertisements.size(); i++) {
				// ---translate the GeoPoint to screen pixels---
				Point screenPts = new Point();
				Paint paint = new Paint();
				paint.setStrokeWidth(1);
				paint.setARGB(255, 140, 23, 23);
				// paint.setStyle(Paint.Style.STROKE);
				paint.setTextSize(15);

				mapView.getProjection().toPixels(points.get(i), screenPts);

				// ---add the marker---
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.blackblank);
				String num;
				if (i < 10) {
					num = "0" + i;
				} else {
					num = i + "";
				}
				String filename = "/res/drawable/black" + num + ".png";
				System.out.println(Log.VERBOSE + "Path " + filename);
				// Bitmap bMap = BitmapFactory.decodeFile(filename);

				canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
				canvas.drawText(advertisements.get(i).getAdName(), screenPts.x,
						screenPts.y - 50, paint);
			}

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

			if (advertisements.size() == 0) {
				Toast.makeText(getBaseContext(),
						"No ads Available for your search", Toast.LENGTH_SHORT)
						.show();

			}
			return false;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mc.animateTo(p);
			// ---Add a location marker---
			MapOverlay mapOverlay = new MapOverlay();
			List<Overlay> listOfOverlays = mapView.getOverlays();
			// listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
			mapView.invalidate();
		}
	}

	/* User can zoom in/out using keys provided on keypad */
	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { if (keyCode ==
	 * KeyEvent.KEYCODE_I) {
	 * mapView.getController().setZoom(mapView.getZoomLevel() + 1); return true;
	 * } else if (keyCode == KeyEvent.KEYCODE_O) {
	 * mapView.getController().setZoom(mapView.getZoomLevel() - 1); return true;
	 * } else if (keyCode == KeyEvent.KEYCODE_S) { mapView.setSatellite(true);
	 * return true; } else if (keyCode == KeyEvent.KEYCODE_T) {
	 * mapView.setTraffic(true); return true; } return false; }
	 */

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

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
			Intent intent = new Intent(LBAMap.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
			intent = new Intent(LBAMap.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}