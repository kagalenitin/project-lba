/**
 * 
 */
package com.lba.advertisement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lba.R;

/**
 * @author payal
 * 
 */
public class Advertisement extends Activity {

	private ListView itemListView;
	private TextView lblAdCode;
	LazyAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisementmain);

		this.setTitle("Location Based Advertisement");
		lblAdCode = (TextView) findViewById(R.id.AdCode);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();

		itemListView = (ListView) findViewById(R.id.list);
		adapter = new LazyAdapter(this, mStrings);
		itemListView.setAdapter(adapter);

		if (b != null) {
			String productId = b.getString("productId");
			if (productId != null) {
				if (!(productId.equalsIgnoreCase("default"))) {
					lblAdCode.setText("List of Advertisements: " + productId);
				} else {
					// / load ad with given product ID
				}
			}
		}

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
			// "http://s2.hubimg.com/u/2179145_f260.jpg"
			"http://www.bordercity.com/menu/kfc_coupons.gif" };
}