/**
 * 
 */
package com.lba.channel;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.ChannelBean;
import com.lba.home.WelcomeUser;
import com.lba.product.Product;
import com.lba.search.SearchProduct;
import com.lba.service.ChannelResourceClient;

/**
 * @author payal
 * 
 */
public class Channelv2 extends Activity { // implements OnClickListener{

	private ListView channelListView;
	private TextView lblChannelCode;
	String uname;
	ChannelAdapter adapter;
	static ArrayList<ChannelBean> channels = new ArrayList<ChannelBean>();

	public static ArrayList<ChannelBean> getChannels() {

		ChannelResourceClient channelResource = new ChannelResourceClient();
		try {
			DomRepresentation representation = channelResource
					.retrieveChannels();
			if (representation != null) {
				channels = channelResource.getChannelsFromXml(representation);
			} else {
				channels = new ArrayList<ChannelBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return channels;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.channelv2);
		this.setTitle("Location Based Advertisement - Channel");
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);

		lblChannelCode = (TextView) findViewById(R.id.ChannelCode);
		channelListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			// lblChannelCode.setText("Welcome, " + uname);
			if (!(uname.equalsIgnoreCase("default"))) {
				// lblChannelCode.setText("List of Channnel");
			}
			// lblChannelCode.setText("List of Channnel");
		}
		channels = getChannels();
		adapter = new ChannelAdapter(this, channels);
		channelListView.setAdapter(adapter);

		// Set Click Listener
		channelListView.setClickable(true);
		channelListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(Channelv2.this, Product.class);
				Bundle b = new Bundle();
				b.putString("channelId", ((ChannelBean) channels.get(position))
						.getChannelid().toString());
				b.putString("uname", uname);
				intent.putExtras(b);
				startActivity(intent);
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
			Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Channelv2.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
			intent = new Intent(Channelv2.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}