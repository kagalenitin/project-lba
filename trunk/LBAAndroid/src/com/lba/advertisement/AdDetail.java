/**
 * 
 */
package com.lba.advertisement;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lba.R;

/**
 * @author payalpatel
 * 
 */
public class AdDetail extends Activity {

	private TextView lblAdId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.adimage);

		this.setTitle("Location Based Advertisement");
		lblAdId = (TextView) findViewById(R.id.ItemCode);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();

		if (b != null) {
			String adImagePath = b.getString("AdId");
			lblAdId.setText("Description for Ad:" + adImagePath);
			ImageView imgView = (ImageView) findViewById(R.id.AdImage);
			Drawable drawable = LoadImageFromWebOperations(adImagePath);
			if (imgView != null)
				imgView.setImageDrawable(drawable);
		}

	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

}
