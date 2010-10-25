package com.lba.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.advertisement.Advertisement;
import com.lba.channel.Channelv2;
import com.lba.location.GPSMap;
import com.lba.mapService.LBAMap;
import com.lba.product.Product;
import com.lba.user.Profile;

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
		setContentView(R.layout.welcome);
		this.setTitle("Location Based Advertisement");

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
				Toast.makeText(WelcomeUser.this, "" + position,
						Toast.LENGTH_SHORT).show();
				Intent intent = null;
				Bundle b = new Bundle();
				if (position == 0) {
					intent = new Intent(WelcomeUser.this, Channelv2.class);
					b.putString("uname", uname);
				} else if (position == 2) {
					intent = new Intent(WelcomeUser.this, Profile.class);
					b.putString("uname", uname);
				} else if (position == 1) {
					intent = new Intent(WelcomeUser.this, LBAMap.class);
					b.putString("channelCode", uname);
				} else if (position == 3) {
					intent = new Intent(WelcomeUser.this, Product.class);
					b.putString("channelCode", uname);
				} else if (position == 6) {
					intent = new Intent(WelcomeUser.this, Advertisement.class);
					b.putString("adCode", uname);
				} else if (position == 8) {
					intent = new Intent(WelcomeUser.this, GPSMap.class);
					b.putString("adCode", uname);
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
			Toast.makeText(this, "Channels", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(WelcomeUser.this, Channelv2.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.text:
			Toast.makeText(this, "Products", Toast.LENGTH_LONG).show();
			intent = new Intent(WelcomeUser.this, Product.class);
			b = new Bundle();
			b.putString("channelId", "");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.icontext:
			Toast.makeText(this, "Advertisements", Toast.LENGTH_LONG).show();
			intent = new Intent(WelcomeUser.this, Advertisement.class);
			b = new Bundle();
			b.putString("productId", "default");
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}
