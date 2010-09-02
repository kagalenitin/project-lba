package com.lba.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Registration extends Activity{
	private Button btnLogin;
	private Button btnCancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Location Based Advertisement Registration");
		setContentView(R.layout.registration);
		btnLogin = (Button) findViewById(R.id.btnregister);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Registration.this, LBALogin.class);
				startActivity(intent);
			}
		});
	}
}
