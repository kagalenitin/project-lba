package com.lba.user;

import java.io.IOException;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lba.R;
import com.lba.home.WelcomeUser;
import com.lba.service.MobileUserResourceClient;

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

	private static final int DIALOG_TEXT_ENTRY = 7;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_TEXT_ENTRY:
			Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
			// This example shows how to add a custom layout to an AlertDialog
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.login_dialog,
					null);
			return new AlertDialog.Builder(LBALogin.this)
					.setIcon(R.drawable.logo)
					.setTitle(R.string.alert_dialog_text_entry)
					.setView(textEntryView)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
									etUsername = (EditText) textEntryView
											.findViewById(R.id.username_edit);
									etPassword = (EditText) textEntryView
											.findViewById(R.id.password_edit);
									if (etUsername != null
											&& etPassword != null) {
										String username = etUsername.getText()
												.toString();
										String password = etPassword.getText()
												.toString();

										if (!((username.equals("") && password
												.equals("")))) {

											if (verifyMobileUser(username,
													password)) {

												Intent intent = new Intent(
														LBALogin.this,
														WelcomeUser.class);
												Bundle b = new Bundle();
												b.putString("uname", etUsername
														.getText().toString());
												intent.putExtras(b);
												startActivity(intent);
											} else {
												Toast.makeText(
														LBALogin.this,
														"Invalid Username or password!",
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											Toast.makeText(
													LBALogin.this,
													"Username or Password can not be empty!",
													Toast.LENGTH_SHORT).show();

										}
									}

								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();
		}
		return null;
	}

	private boolean verifyMobileUser(String username, String password) {
		boolean result = false;
		MobileUserResourceClient mobileUserClient = new MobileUserResourceClient();
		DomRepresentation representation = mobileUserClient.verifyMobileUser(
				username, password);
		try {
			result = mobileUserClient
					.getMobileUserVerificationFromXml(representation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.main);
		this.setTitle("Location Based Advertisement - Login");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		// Get the EditText and Button References
		btnLogin = (Button) findViewById(R.id.signin_button);
		btnSignUp = (Button) findViewById(R.id.signup_button);
		imageView = (ImageView) findViewById(R.id.ImageView01);
		imageView.setImageResource(R.drawable.adspotlogo);

		// Set Click Listener
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_TEXT_ENTRY);
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