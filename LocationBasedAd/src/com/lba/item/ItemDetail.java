/**
 * 
 */
package com.lba.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lba.R;

/**
 * @author payalpatel
 *
 */
public class ItemDetail extends Activity{
	
	private TextView lblItemCode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_description);
		
		this.setTitle("Location Based Advertisement");
		lblItemCode = (TextView) findViewById(R.id.ItemCode);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		
		if (b != null) {
			String ItemCode = b.getString("ItemCode");
			lblItemCode.setText("Description for ItemCode:" + ItemCode);
		}
		
	}
}
