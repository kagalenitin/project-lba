package com.lba.mapService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import com.google.android.maps.Projection;
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

	public ArrayList<AdMerchantAdBean> getAdsByMerchantDistance(String adName,
			String latitude, String longitude) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisementsByMerchantDistance(adName, latitude,
							longitude);
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
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.map);
		this.setTitle("AdSpot - Map");
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.ic_menu_mylocation);

		advertisements = new ArrayList<AdMerchantAdBean>();
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			adName = b.getString("adName");
			this.setTitle("LBA: Search Ad - " + adName);
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
			String coordinates[] = { latitude, longitude };
			double lat = Double.parseDouble(coordinates[0]);
			double lng = Double.parseDouble(coordinates[1]);
			p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			points.add(p);
			mc.animateTo(p);
			// mc.setZoom(13);
		}
		if (advertisements.size() == 0) {
			Toast.makeText(getBaseContext(),
					"No ads Available for your search", Toast.LENGTH_SHORT)
					.show();

		}
		// ---Add a location marker---
		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		// listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		mapView.invalidate();
		mapView.setBuiltInZoomControls(true);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener myLocationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 500.0f, myLocationListener);
		Location currentLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		try {
			double Latitude = currentLocation.getLatitude();
			double Longitude = currentLocation.getLongitude();
			// adds the user location as a point on the map
			p = new GeoPoint((int) (Latitude * 1e6), (int) (Longitude * 1e6));
			mc.animateTo(p);
			for (int i = 0; i < advertisements.size(); i++) {
				latitude = advertisements.get(i).getLatitude();
				longitude = advertisements.get(i).getLongitude();
				String coordinates[] = { latitude, longitude };
				double lat = Double.parseDouble(coordinates[0]);
				double lng = Double.parseDouble(coordinates[1]);
				GeoPoint point = new GeoPoint((int) (lat * 1E6),
						(int) (lng * 1E6));
				DrawPath(p, point, Color.GREEN, mapView);

			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(),
					"No current locaiton information available!",
					Toast.LENGTH_SHORT).show();
		}
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

				canvas.drawBitmap(bmp, screenPts.x - 6, screenPts.y - 6, null);
			}

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			Paint paint = new Paint();
			paint.setStrokeWidth(1);
			paint.setARGB(255, 140, 23, 23);
			// paint.setStyle(Paint.Style.STROKE);
			paint.setTextSize(15);

			// ---add the marker---
			try {
				mapView.getProjection().toPixels(p, screenPts);
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.redblank);

				canvas.drawBitmap(bmp, screenPts.x - 6, screenPts.y - 6, null);
			} catch (Exception e) {
				// Toast.makeText(getBaseContext(),
				// "No location information available", Toast.LENGTH_SHORT)
				// .show();
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

		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();

		System.out.println(Log.VERBOSE + "LOCATION CHANGED");
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			p = new GeoPoint((int) (lat * 1000000), (int) (lng * 1000000));
			GeoPoint currentLocation = p;
			mc.animateTo(p);

			advertisements = getAdsByMerchantDistance(adName,
					String.valueOf(lat), String.valueOf(lng));
			points.removeAll(advertisements);
			points = new ArrayList<GeoPoint>();

			for (int i = 0; i < advertisements.size(); i++) {

				System.out.println("AD:" + advertisements.get(i).getAdID());
				latitude = advertisements.get(i).getLatitude();
				longitude = advertisements.get(i).getLongitude();
				// String coordinates[] = { latitude + 50, longitude + 30 };
				String coordinates[] = { latitude, longitude };

				lat = Double.parseDouble(coordinates[0]);
				lng = Double.parseDouble(coordinates[1]);
				GeoPoint point = new GeoPoint((int) (lat * 1E6),
						(int) (lng * 1E6));

				points.add(point);
				mc.animateTo(point);

				DrawPath(currentLocation, point, Color.GREEN, mapView);

				// mc.setZoom(13);
			}
			if (advertisements.size() == 0) {
				Toast.makeText(getBaseContext(),
						"No ads Available for your search distance",
						Toast.LENGTH_SHORT).show();

			}

			// ---Add a location marker---
			MapOverlay mapOverlay = new MapOverlay();
			// List<Overlay> listOfOverlays = mapView.getOverlays();
			// listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
			mapView.invalidate();
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
		if (status == LocationProvider.AVAILABLE) {

		}
		if (status == LocationProvider.OUT_OF_SERVICE) {
			// ViewAdapter.showShortToast(mContext,
			// "Location is temporarily out of service.");
		}
		if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {

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
			Intent intent = new Intent(LBAMap.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(LBAMap.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

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
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.blackblank);

			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("My Location", myScreenCoords.x, myScreenCoords.y,
					paint);
			return true;
		}
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			/*
			 * it will show a message on location change
			 * Toast.makeText(getBaseContext(), "New location latitude ["
			 * +argLocation.getLatitude() + "] longitude [" +
			 * argLocation.getLongitude()+"]", Toast.LENGTH_SHORT).show();
			 */

			mc.animateTo(myGeoPoint);

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	// ============================================================//

	public class MyOverLay extends Overlay {
		private GeoPoint gp1;
		private GeoPoint gp2;
		private int mRadius = 6;
		private int mode = 0;
		private int defaultColor;
		private String text = "";
		private Bitmap img = null;

		public MyOverLay(GeoPoint gp1, GeoPoint gp2, int mode) // GeoPoint is a
		// int. (6E)
		{
			this.gp1 = gp1;
			this.gp2 = gp2;
			this.mode = mode;
			defaultColor = 999; // no defaultColor

		}

		public MyOverLay(GeoPoint gp1, GeoPoint gp2, int mode, int defaultColor) {
			this.gp1 = gp1;
			this.gp2 = gp2;
			this.mode = mode;
			this.defaultColor = defaultColor;
		}

		public void setText(String t) {
			this.text = t;
		}

		public void setBitmap(Bitmap bitmap) {
			this.img = bitmap;
		}

		public int getMode() {
			return mode;
		}

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			Projection projection = mapView.getProjection();
			if (shadow == false) {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				Point point = new Point();
				projection.toPixels(gp1, point);
				// mode=1&#65306;start
				if (mode == 1) {
					if (defaultColor == 999)
						paint.setColor(Color.BLUE);
					else
						paint.setColor(defaultColor);
					RectF oval = new RectF(point.x - mRadius,
							point.y - mRadius, point.x + mRadius, point.y
									+ mRadius);
					// start point
					canvas.drawOval(oval, paint);
				}
				// mode=2&#65306;path
				else if (mode == 2) {
					if (defaultColor == 999)
						paint.setColor(Color.RED);
					else
						paint.setColor(defaultColor);
					Point point2 = new Point();
					projection.toPixels(gp2, point2);
					paint.setStrokeWidth(5);
					paint.setAlpha(120);
					canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
				}
				/* mode=3&#65306;end */
				else if (mode == 3) {
					/* the last path */

					if (defaultColor == 999)
						paint.setColor(Color.GREEN);
					else
						paint.setColor(defaultColor);
					Point point2 = new Point();
					projection.toPixels(gp2, point2);
					paint.setStrokeWidth(5);
					paint.setAlpha(120);
					canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
					RectF oval = new RectF(point2.x - mRadius, point2.y
							- mRadius, point2.x + mRadius, point2.y + mRadius);
					/* end point */
					paint.setAlpha(255);
					canvas.drawOval(oval, paint);
				}
			}
			return super.draw(canvas, mapView, shadow, when);
		}

	}

	private void DrawPath(GeoPoint src, GeoPoint dest, int color,
			MapView mMapView01) {
		// connect to map web service
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
		urlString.append("&saddr=");// from
		urlString.append(Double.toString((double) src.getLatitudeE6() / 1.0E6));
		urlString.append(",");
		urlString
				.append(Double.toString((double) src.getLongitudeE6() / 1.0E6));
		urlString.append("&daddr=");// to
		urlString
				.append(Double.toString((double) dest.getLatitudeE6() / 1.0E6));
		urlString.append(",");
		urlString
				.append(Double.toString((double) dest.getLongitudeE6() / 1.0E6));
		urlString.append("&ie=UTF8&0&om=0&output=kml");
		Log.d("xxx", "URL=" + urlString.toString());
		// get the kml (XML) doc. And parse it to get the coordinates(direction
		// route).
		Document doc = null;
		HttpURLConnection urlConnection = null;
		URL url = null;
		try {
			url = new URL(urlString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConnection.getInputStream());

			if (doc.getElementsByTagName("GeometryCollection").getLength() > 0) {
				// String path =
				// doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
				String path = doc.getElementsByTagName("GeometryCollection")
						.item(0).getFirstChild().getFirstChild()
						.getFirstChild().getNodeValue();
				Log.d("xxx", "path=" + path);
				String[] pairs = path.split(" ");
				String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude
				// lngLat[1]=latitude
				// lngLat[2]=height
				// src
				GeoPoint startGP = new GeoPoint(
						(int) (Double.parseDouble(lngLat[1]) * 1E6),
						(int) (Double.parseDouble(lngLat[0]) * 1E6));
				mMapView01.getOverlays()
						.add(new MyOverLay(startGP, startGP, 1));
				GeoPoint gp1;
				GeoPoint gp2 = startGP;
				for (int i = 1; i < pairs.length; i++) // the last one would be
				// crash
				{
					lngLat = pairs[i].split(",");
					gp1 = gp2;
					// watch out! For GeoPoint, first:latitude, second:longitude
					gp2 = new GeoPoint(
							(int) (Double.parseDouble(lngLat[1]) * 1E6),
							(int) (Double.parseDouble(lngLat[0]) * 1E6));
					mMapView01.getOverlays().add(
							new MyOverLay(gp1, gp2, 2, color));
					Log.d("xxx", "pair:" + pairs[i]);
				}
				mMapView01.getOverlays().add(new MyOverLay(dest, dest, 3)); // use
				// the
				// default
				// color
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}