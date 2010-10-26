package com.lba.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lba.R;
import com.lba.mapService.LBAMap;

/**
 * @author payalpatel
 * 
 */

public class LBALocation extends Activity {

	private EditText elProductName;
	private Button btnMap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Location Based Advertisement - Search");
		setContentView(R.layout.searchproduct);

		// Get the EditText and Button References
		elProductName = (EditText) findViewById(R.id.productName);
		btnMap = (Button) findViewById(R.id.searchbutton);

		// Set Click Listener
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LBALocation.this, LBAMap.class);
				Bundle b = new Bundle();
				b.putString("productName", elProductName.getText().toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}