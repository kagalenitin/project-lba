package com.lba.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LBALogin extends Activity {

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnCancel;
	private TextView lblResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Location Based Advertisement");
		setContentView(R.layout.main);

		// Get the EditText and Button References
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.login_button);
		btnCancel = (Button) findViewById(R.id.cancel_button);
		lblResult = (TextView) findViewById(R.id.result);

		// Set Click Listener
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check Login
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();

				if (username.equals("payal") && password.equals("payal")) {
					lblResult.setText("Login successful.");
					System.out.println("Started.....");
					Intent intent = new Intent(LBALogin.this, WelcomeUser.class);
					Bundle b = new Bundle();
					b.putString("uname", etUsername.getText().toString());
					System.out.println(Log.VERBOSE);
					intent.putExtras(b);
					startActivity(intent);
				} else {
					lblResult
							.setText("Login failed. Username and/or password doesn't match.");
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				

				System.out.println("Started.....");
				Intent intent = new Intent(LBALogin.this, Item.class);
				Bundle b = new Bundle();
				b.putString("uname", etUsername.getText().toString());
				System.out.println(Log.VERBOSE);
				intent.putExtras(b);
				startActivity(intent);

				// Close the application
				// finish();
			}
		});

	}
}