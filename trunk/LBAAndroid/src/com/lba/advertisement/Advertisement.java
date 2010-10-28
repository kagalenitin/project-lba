/**
 * 
 */
package com.lba.advertisement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;

/**
 * @author payal
 * 
 */
public class Advertisement extends Activity {

	private ListView itemListView;
	private TextView lblAdCode;
	LazyAdapter adapter;
	String uname;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.advertisementmain);
		this.setTitle("Location Based Advertisement - Ads");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);
		lblAdCode = (TextView) findViewById(R.id.AdCode);
		itemListView = (ListView) findViewById(R.id.list);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		String productId = null;
		if (b != null) {
			productId = b.getString("productId");
			uname = b.getString("uname");
			if (productId != null) {
				if (!(productId.equalsIgnoreCase(""))) {
					lblAdCode.setText("List of Advertisements: " + productId);
					adapter = new LazyAdapter(this, mStrings, productId);
				} else {
					lblAdCode.setText("List of Advertisements " + productId);
					adapter = new LazyAdapter(this, mStrings);
				}
			}
		}

		itemListView.setAdapter(adapter);

		itemListView.setClickable(true);
		itemListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(Advertisement.this, AdDetail.class);
				Bundle b = new Bundle();
				b.putString("AdId", mStrings[position]);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		itemListView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		adapter.imageLoader.stopThread();
		itemListView.setAdapter(null);
		super.onDestroy();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	private String[] mStrings = {
			"http://www.whofish.org/DevImages/imgActive/original_47944.jpg",
			"http://1.bp.blogspot.com/_ur37apRbjR4/TBfIT6WIjfI/AAAAAAAAACo/nDa9ziaTh0M/S272/s4.jpg",
			"http://s2.hubimg.com/u/2179145_f260.jpg",
			"http://www.bordercity.com/menu/kfc_coupons.gif" };

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
			Intent intent = new Intent(Advertisement.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
			intent = new Intent(Advertisement.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

}