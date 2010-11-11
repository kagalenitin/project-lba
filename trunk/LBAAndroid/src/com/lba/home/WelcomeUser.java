package com.lba.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.advertisement.Advertisement;
import com.lba.channel.Channelv2;
import com.lba.location.GPSMap;
import com.lba.mapService.LocateAd;
import com.lba.product.Product;
import com.lba.search.SearchAd;
import com.lba.search.SearchChannel;
import com.lba.search.SearchProduct;
import com.lba.subscription.ChannelSubscription;
import com.lba.subscription.ViewChannelSubscription;
import com.lba.user.LBALogin;
import com.lba.user.Profile;
import com.lba.util.Notify;

/**
 * @author payal
 * 
 */
public class WelcomeUser extends Activity { // implements OnClickListener{

	private TextView lblUser;
	private String uname = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.welcome);
		this.setTitle("Location Based Advertisement - Home");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this, this));

		lblUser = (TextView) findViewById(R.id.user);

		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			lblUser.setText("Welcome, " + uname);
		}

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = null;
				Bundle b = new Bundle();
				if (position == 0) {
					Toast.makeText(WelcomeUser.this, "Channel",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, Channelv2.class);
					b.putString("uname", uname);
				} else if (position == 2) {
					Toast.makeText(WelcomeUser.this, "Profile",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, Profile.class);
					b.putString("uname", uname);
				} else if (position == 1) {
					Toast.makeText(WelcomeUser.this, "LocateAd",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, LocateAd.class);
					b.putString("uname", uname);
				} else if (position == 3) {
					Toast.makeText(WelcomeUser.this, "Product",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, Product.class);
					b.putString("channelId", "");
					b.putString("uname", uname);
				} else if (position == 4) {
					Toast.makeText(WelcomeUser.this, "Search",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, Notify.class);
					b.putString("uname", uname);
					b.putString("productId", "");
				} else if (position == 5) {
					Toast.makeText(WelcomeUser.this, "Direction",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, GPSMap.class);
					b.putString("uname", uname);
				} else if (position == 6) {
					Toast.makeText(WelcomeUser.this, "Advertisement",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this, Advertisement.class);
					b.putString("uname", uname);
					b.putString("productId", "");
				} else if (position == 7) {
					Toast.makeText(WelcomeUser.this, "Subscribe Channel",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this,
							ChannelSubscription.class);
					b.putString("uname", uname);
				} else if (position == 8) {
					Toast.makeText(WelcomeUser.this, "My Subscription",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(WelcomeUser.this,
							ViewChannelSubscription.class);
					b.putString("uname", uname);
				} else {
					intent = new Intent(WelcomeUser.this, Channelv2.class);
					b.putString("uname", uname);
				}
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.icon:
			Toast.makeText(this, "Search Channels", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(WelcomeUser.this, SearchChannel.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.text:
			Toast.makeText(this, "Search Products", Toast.LENGTH_SHORT).show();
			intent = new Intent(WelcomeUser.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			b.putString("channelId", "");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.icontext:
			Toast.makeText(this, "Search Advertisements", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(WelcomeUser.this, SearchAd.class);
			b = new Bundle();
			b.putString("uname", uname);
			b.putString("productId", "");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.singout:
			Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
			intent = new Intent(WelcomeUser.this, LBALogin.class);
			b = new Bundle();
			intent.putExtras(b);
			startActivity(intent);
			break;
		}

		return true;
	}
}
