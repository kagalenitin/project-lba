package com.lba.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		this.setTitle("Location Based Advertisement Registration");
		setContentView(R.layout.registration);
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

					MobileUserResourceClient mobileUserClient = new MobileUserResourceClient();
					MobileUserBean mobileUser = new MobileUserBean();
					mobileUser.setAddress(address);
					mobileUser.setUsername(username);
					mobileUser.setFirstName(firstName);
					mobileUser.setLastName(lastName);
					mobileUser.setPhone(phone);
					mobileUser.setEmail(email);
					mobileUser.setPassword(password);
					mobileUserClient.createMobileUser(mobileUser);

					Toast.makeText(Registration.this, R.string.regpositive,
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Registration.this,
							LBALogin.class);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(Registration.this, R.string.regnegative,
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
