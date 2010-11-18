package com.lba.channel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.lba.R;
import com.lba.product.Product;

/**
 * @author payalpatel
 * 
 */
public class Channel extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel);
		this.setTitle("AdSpot");

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(Channel.this, "" + position, Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(Channel.this, Product.class);
				Bundle b = new Bundle();
				b.putString("channelCode", Integer.toString(position));
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}
}