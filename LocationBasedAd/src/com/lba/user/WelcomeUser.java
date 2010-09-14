package com.lba.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lba.R;
import com.lba.channel.Channel;

/**
 * @author payal
 * 
 */
public class WelcomeUser extends Activity { // implements OnClickListener{

	private TextView lblUser;
	private Button btnShowChannel;
	private String uname = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		this.setTitle("Location Based Advertisement");
		btnShowChannel = (Button) findViewById(R.id.channel);
		lblUser = (TextView) findViewById(R.id.user);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			System.out.println(uname);
			lblUser.setText("Welcome, " + uname);

		}

		// Set Click Listener
		btnShowChannel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Load Channel
				Intent intent = new Intent(WelcomeUser.this, Channel.class);
				Bundle b = new Bundle();
				b.putString("uname", uname);
				System.out.println(Log.VERBOSE);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}
}