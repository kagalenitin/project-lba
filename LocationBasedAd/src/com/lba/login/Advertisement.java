/**
 * 
 */
package com.lba.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author payal
 * 
 */
public class Advertisement extends Activity { // implements OnClickListener{

	private ListView lv1;
	private TextView lblChannelCode;
	private String lv_arr[] = { "Ad1", "Ad2", "Ad3", "Ad4", "Ad5", "Ad6",
			"Ad7", "Ad8" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Location Based Advertisement");
		setContentView(R.layout.advertisement);
		lblChannelCode = (TextView) findViewById(R.id.ChannelCode);
		lv1 = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			String channelCode = b.getString("channelCode");
			// lv_arr[0] = channelCode;
			lblChannelCode.setText("Ads for ChannelCode:" + channelCode);
		}
		// Set Click Listener
		lv1.setClickable(true);
		lv1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(Advertisement.this, Item.class);
				Bundle b = new Bundle();
				b.putString("AdCode", lv_arr[position]);
				System.out.println(Log.VERBOSE);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		lv1.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lv_arr));

	}
}