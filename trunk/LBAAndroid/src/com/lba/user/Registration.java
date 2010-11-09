package com.lba.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.MobileUserBean;
import com.lba.service.MobileUserResourceClient;

public class Registration extends Activity {

	private Button btnCancel;
	private Button btnSignup;
	private EditText txtusername;
	private EditText txtpassword;
	private EditText txtFirstName;
	private EditText txtLastName;
	private EditText txtAddress;
	private EditText txtPhone;
	private EditText txtEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.registration);
		this.setTitle("Location Based Advertisement - Registration");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Registration.this, LBALogin.class);
				startActivity(intent);
			}
		});

		btnSignup = (Button) findViewById(R.id.btnregister);
		btnSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				txtusername = (EditText) findViewById(R.id.txtusername);
				txtpassword = (EditText) findViewById(R.id.txtpassword);
				txtFirstName = (EditText) findViewById(R.id.txtfirstname);
				txtLastName = (EditText) findViewById(R.id.txtlastname);
				txtAddress = (EditText) findViewById(R.id.txtaddress);
				txtPhone = (EditText) findViewById(R.id.txtphone);
				txtEmail = (EditText) findViewById(R.id.txtemail);
				try {

					String username = txtusername.getText().toString();
					String firstName = txtFirstName.getText().toString();
					String lastName = txtLastName.getText().toString();
					String address = txtAddress.getText().toString();
					String phone = txtPhone.getText().toString();
					String email = txtEmail.getText().toString();
					String password = txtpassword.getText().toString();
					System.out.println(Log.VERBOSE + "Uanme" + username);
					if (!(username.equals("") || firstName.equals("")
							|| lastName.equals("") || address.equals("")
							|| phone.equals("") || email.equals("") || password
							.equals(""))) {
						if (!(ValidateUser.validateFirstName(firstName))) {
							Toast.makeText(Registration.this,
									R.string.regFirstNameNeg,
									Toast.LENGTH_SHORT).show();
						} else if (!(ValidateUser.validateLastName(lastName))) {
							Toast.makeText(Registration.this,
									R.string.regLastNameNeg, Toast.LENGTH_SHORT)
									.show();
						} else if (!(ValidateUser.validateUserName(username))) {
							Toast.makeText(Registration.this,
									R.string.regUserNameNeg, Toast.LENGTH_SHORT)
									.show();
						} else if (!(ValidateUser.validatePassword(password))) {
							Toast.makeText(Registration.this,
									R.string.regPasswordNeg, Toast.LENGTH_SHORT)
									.show();
						} else if (!(ValidateUser.validateEmail(email))) {
							Toast.makeText(Registration.this,
									R.string.regEmailNeg, Toast.LENGTH_SHORT)
									.show();
						} else if (!(ValidateUser.validaetPhoneNumber(phone))) {
							Toast.makeText(Registration.this,
									R.string.regPhoneNeg, Toast.LENGTH_SHORT)
									.show();
						} else if (!(ValidateUser.validateAddress(address))) {
							Toast.makeText(Registration.this,
									R.string.regAddressNeg, Toast.LENGTH_SHORT)
									.show();
						} else {
							MobileUserResourceClient mobileUserClient = new MobileUserResourceClient();
							MobileUserBean mobileUser = new MobileUserBean();
							mobileUser.setAddress(address);
							mobileUser.setUsername(username);
							mobileUser.setFirstName(firstName);
							mobileUser.setLastName(lastName);
							mobileUser.setPhone(phone);
							mobileUser.setEmail(email);
							mobileUser.setPassword(password);

							// create user
							mobileUserClient.createMobileUser(mobileUser);
							Toast.makeText(Registration.this,
									R.string.regpositive, Toast.LENGTH_SHORT)
									.show();
							Intent intent = new Intent(Registration.this,
									LBALogin.class);
							startActivity(intent);
						}
					} else {
						Toast.makeText(Registration.this, R.string.regnegative,
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(Registration.this, R.string.regnegative,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
