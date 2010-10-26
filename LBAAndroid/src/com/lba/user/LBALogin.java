package com.lba.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lba.R;
import com.lba.home.WelcomeUser;

/**
 * @author payal
 * 
 */
public class LBALogin extends Activity {

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnSignUp;
	private ImageView imageView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.main);
		this.setTitle("Location Based Advertisement - Login");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		// Get the EditText and Button References
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.signin_button);
		btnSignUp = (Button) findViewById(R.id.signup_button);
		imageView = (ImageView) findViewById(R.id.ImageView01);
		imageView.setImageResource(R.drawable.adspotlogo);

		// Set Click Listener
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check Login
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();

				if (username.equals("payal") && password.equals("payal")) {

					Intent intent = new Intent(LBALogin.this, WelcomeUser.class);
					Bundle b = new Bundle();
					b.putString("uname", etUsername.getText().toString());
					intent.putExtras(b);
					startActivity(intent);
				} else {
					CustomizeDialog customizeDialog = new CustomizeDialog(
							LBALogin.this, R.string.logneg);
					customizeDialog.show();
				}
			}
		});
		btnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LBALogin.this, Registration.class);
				Bundle b = new Bundle();
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}
}