/**
 * 
 */
package com.lba.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author payal
 * 
 */
public class Item extends Activity { // implements OnClickListener{

	private ListView lv1;
	private TextView lblChannelCode;
	private String lv_arr[] = { "item1", "item2", "item3", "item4", "item5",
			"item6", "item7", "item8" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		this.setTitle("Location Based Advertisement");
		lblChannelCode = (TextView) findViewById(R.id.ChannelCode);
		lv1 = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			String AdCode = b.getString("AdCode");
			// lv_arr[0] = channelCode;
			lblChannelCode.setText("Items for AdCode:" + AdCode);
		}
		lv1.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lv_arr));
	}
}