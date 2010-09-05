package com.lba.mapService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


/**
 * @author payalpatel
 *
 */

public class LBALocation extends Activity {

	private EditText etLatitude;
	private EditText etLongitude;
	private Button btnMap;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Location Based Advertisement - Map");
		setContentView(R.layout.main);

		// Get the EditText and Button References
		etLatitude = (EditText) findViewById(R.id.latitude);
		etLongitude = (EditText) findViewById(R.id.longitude);
		btnMap = (Button) findViewById(R.id.map_button);
		
		// Set Click Listener
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LBALocation.this, LBAMap.class);
				Bundle b = new Bundle();
				b.putString("latitude", etLatitude.getText().toString());
				b.putString("longitude", etLongitude.getText().toString());
				System.out.println(Log.VERBOSE);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}