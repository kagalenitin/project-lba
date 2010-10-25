package com.lba.user;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lba.R;

/**
 * @author payal
 * 
 */

public class CustomizeDialog extends Dialog implements OnClickListener {

	Button okButton;

	public CustomizeDialog(Context context, int regpos) {

		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/** Design the dialog in main.xml file */
		setContentView(R.layout.customdialog);
		TextView message = (TextView) findViewById(R.id.dialogMessage);

		if (context.getClass().getName()
				.equalsIgnoreCase("com.lba.user.Registration")) {
			if (regpos == R.string.regneg)
				message.setText(R.string.regnegative);
			else
				message.setText(R.string.regpositive);
		} else if (context.getClass().getName()
				.equalsIgnoreCase("com.lba.user.LBALogin")) {
			if (regpos == R.string.logneg)
				message.setText(R.string.loginegative);
		}
		okButton = (Button) findViewById(R.id.OkButton);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == okButton) {
			dismiss();
		}
	}
}
