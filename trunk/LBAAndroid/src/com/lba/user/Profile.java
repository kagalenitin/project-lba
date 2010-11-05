package com.lba.user;

import java.io.IOException;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.MobileUserBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.MobileUserResourceClient;

public class Profile extends Activity {

	private Button btnCancel;
	private Button btnUpdate;
	private EditText txtusername;
	private EditText txtpassword;
	private EditText txtFirstName;
	private EditText txtLastName;
	private EditText txtAddress;
	private EditText txtPhone;
	private EditText txtEmail;
	MobileUserResourceClient mobileUserClient = new MobileUserResourceClient();
	MobileUserBean mobileUser = new MobileUserBean();
	String uname = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.profile);
		this.setTitle("Location Based Advertisement - Profile");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
		}

		// load user profile
		txtusername = (EditText) findViewById(R.id.txtusername);
		txtpassword = (EditText) findViewById(R.id.txtpassword);
		txtFirstName = (EditText) findViewById(R.id.txtfirstname);
		txtLastName = (EditText) findViewById(R.id.txtlastname);
		txtAddress = (EditText) findViewById(R.id.txtaddress);
		txtPhone = (EditText) findViewById(R.id.txtphone);
		txtEmail = (EditText) findViewById(R.id.txtemail);

		try {
			DomRepresentation representation = mobileUserClient
					.retrieveMobileUser(uname);
			if (representation == null) {
				Toast.makeText(Profile.this,
						"" + "Service temporary not available",
						Toast.LENGTH_SHORT).show();
			}
			mobileUser = mobileUserClient.getMobileUserFromXml(representation);
		} catch (IOException e1) {
			Toast.makeText(Profile.this,
					"" + "Service temporary not available", Toast.LENGTH_SHORT)
					.show();
			e1.printStackTrace();
		}

		txtusername.setText(mobileUser.getUsername());
		txtpassword.setText(mobileUser.getPassword());
		txtFirstName.setText(mobileUser.getFirstName());
		txtLastName.setText(mobileUser.getLastName());
		txtAddress.setText(mobileUser.getAddress());
		txtPhone.setText(mobileUser.getPhone());
		txtEmail.setText(mobileUser.getEmail());

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Profile.this, WelcomeUser.class);
				Bundle b = new Bundle();
				b.putString("uname", uname);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					String username = txtusername.getText().toString();
					String firstName = txtFirstName.getText().toString();
					String lastName = txtLastName.getText().toString();
					String address = txtAddress.getText().toString();
					String phone = txtPhone.getText().toString();
					String email = txtEmail.getText().toString();
					String password = txtpassword.getText().toString();

					mobileUser.setAddress(address);
					mobileUser.setUsername(username);
					mobileUser.setFirstName(firstName);
					mobileUser.setLastName(lastName);
					mobileUser.setPhone(phone);
					mobileUser.setEmail(email);
					mobileUser.setPassword(password);

					mobileUserClient.updateMobileUser(uname, mobileUser);

					Toast.makeText(Profile.this, "Profile update Successfully",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Profile.this, WelcomeUser.class);
					Bundle b = new Bundle();
					b.putString("uname", uname);
					intent.putExtras(b);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(Profile.this, "Error updating profile!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.commonmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Profile.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(Profile.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}
