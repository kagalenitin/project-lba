package com.lba.mapService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lba.R;

/**
 * @author payalpatel
 * 
 */

public class LBALocation extends Activity {

	private EditText elAdname;
	// private EditText etLongitude;
	private Button btnMap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.mapmain);
		this.setTitle("Location Based Advertisement - Map");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);


		// Get the EditText and Button References
		elAdname = (EditText) findViewById(R.id.adName);
		// etLongitude = (EditText) findViewById(R.id.longitude);
		btnMap = (Button) findViewById(R.id.map_button);

		// Set Click Listener
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LBALocation.this, LBAMap.class);
				Bundle b = new Bundle();
				b.putString("adName", elAdname.getText().toString());
				// b.putString("longitude", etLongitude.getText().toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}