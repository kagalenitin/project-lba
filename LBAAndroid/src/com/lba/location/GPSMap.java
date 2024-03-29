package com.lba.location;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.lba.R;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;

/**
 * This class manages the GPS Direction service for the application.
 * 
 * @author payal
 */
public class GPSMap extends MapActivity implements LocationListener {
	/** Called when the activity is first created. */

	// EditText txted = null;
	Button btnSimple = null;
	MapView gMapView = null;
	MapController mc = null;
	Drawable defaultMarker = null;
	GeoPoint p = null;
	String uname = "";
	double latitude = 37.3387589, longitude = -121.8850902;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.location);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.ic_menu_directions);

		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
		}

		// Creating and initializing Map
		gMapView = (MapView) findViewById(R.id.myGMap);
		p = new GeoPoint((int) (latitude * 1000000),
				(int) (longitude * 1000000));

		gMapView.setSatellite(false);
		mc = gMapView.getController();
		mc.setCenter(p);
		mc.setZoom(14);

		// Add a location mark
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
		List<Overlay> list = gMapView.getOverlays();
		list.add(myLocationOverlay);

		// Adding zoom controls to Map
		@SuppressWarnings("deprecation")
		ZoomControls zoomControls = (ZoomControls) gMapView.getZoomControls();
		zoomControls.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		gMapView.addView(zoomControls);
		gMapView.displayZoomControls(true);

		// Getting locationManager and reflecting changes over map if distance
		// travel by
		// user is greater than 500m from current location.
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,
				this);

		// this.startActivity(new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://maps.google.com/maps?f=d&saddr=37.4,-121.9&daddr=Bellevue, WA&hl=en")));
	}

	/* This method is called when use position will get changed */
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			String currentLocation = "Lat: " + lat + " Lng: " + lng;
			// txted.setText(currentLocation);
			p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mc.animateTo(p);
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://maps.google.com/maps?f=d&saddr=" + lat + ","
							+ lng + "&daddr=37.6,-121.11111")));
			// Uri.parse("http://maps.google.com/maps?f=d&saddr=37.4,-121.9&daddr=37.6,-121.11111")));
			// Uri.parse("http://maps.google.com/maps?f=d&saddr=37.4,-121.9&daddr=Bellevue, WA&hl=en")));
		}
	}

	public void onProviderDisabled(String provider) {
		// required for interface, not used
	}

	public void onProviderEnabled(String provider) {
		// required for interface, not used
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// required for interface, not used
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* User can zoom in/out using keys provided on keypad */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() + 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() - 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			gMapView.setSatellite(true);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			gMapView.setTraffic(true);
			return true;
		}
		return false;
	}

	/* Class overload draw method which actually plot a marker,text etc. on Map */
	protected class MyLocationOverlay extends com.google.android.maps.Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			Paint paint = new Paint();

			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(p, myScreenCoords);

			paint.setStrokeWidth(1);
			paint.setARGB(255, 139, 35, 35);
			paint.setStyle(Paint.Style.STROKE);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.blackblank);

			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("Current Location", myScreenCoords.x,
					myScreenCoords.y, paint);
			return true;
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
			Intent intent = new Intent(GPSMap.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(GPSMap.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}