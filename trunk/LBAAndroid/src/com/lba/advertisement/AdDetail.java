/**
 * 
 */
package com.lba.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.AdvertisementBean;
import com.lba.home.WelcomeUser;
import com.lba.mapService.LBAMapAdId;
import com.lba.search.SearchProduct;
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payalpatel
 * 
 */
public class AdDetail extends Activity {

	private TextView lblAdId;
	String uname;
	String adId;
	private String adPath = "http://192.168.1.72:8080";
	static AdvertisementBean advertisement = null;

	public static AdvertisementBean getAdsByID(String adId) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisement(adId);
			if (representation != null) {
				advertisement = advertisementResource
						.getAdvertisementFromXml(representation);
			} else {
				advertisement = new AdvertisementBean();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisement;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.adimage);
		this.setTitle("Location Based Advertisement - Ad Detail");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		lblAdId = (TextView) findViewById(R.id.ItemCode);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();

		if (b != null) {
			uname = b.getString("uname");
			String adImagePath = b.getString("AdId");
			adId = b.getString("adId");
			lblAdId.setText("Advertisement Details");
			ImageView imgView = (ImageView) findViewById(R.id.AdImage);
			if (adImagePath.equals("") || adImagePath.equals(null)) {
				advertisement = getAdsByID(adId);
				adImagePath = adPath + advertisement.getFileLocation();
				if (advertisement != null) {
				//	drawable = LoadImageFromWebOperations(adImagePath);
				}
			} else {
				Toast.makeText(this, "Tap on the ad to get location info!",
						Toast.LENGTH_LONG).show();
				//drawable = LoadImageFromWebOperations(adImagePath);
			}
			Drawable drawable = LoadImageFromWebOperations(adImagePath);
			if (imgView != null) {
				imgView.setImageDrawable(drawable);
			}
			imgView.setClickable(true);
			imgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AdDetail.this, LBAMapAdId.class);
					Bundle b = new Bundle();
					b.putString("uname", uname);
					b.putString("adId", adId);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
		}
	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
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
			Intent intent = new Intent(AdDetail.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(AdDetail.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

}
