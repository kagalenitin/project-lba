package com.lba.user;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author payal
 * 
 */
public class CustomDialogLogin extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Display Custom Dialog */
		CustomizeDialog customizeDialog = new CustomizeDialog(this, 1);
		customizeDialog.show();
	}
}
