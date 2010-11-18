/**
 * 
 */
package com.lba.search;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.ChannelBean;
import com.lba.home.WelcomeUser;
import com.lba.mapService.LBALocation;
import com.lba.product.Product;
import com.lba.service.ChannelResourceClient;

/**
 * @author payal
 * 
 */
public class SearchChannel extends Activity { // implements OnClickListener{

	private ListView channelListView;
	private Button btnSearch;
	private EditText elChannelName;
	String channelName;
	int textlength = 0;
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

	public static ArrayList<ChannelBean> getChannelsByName(String channelName) {

		ChannelResourceClient itemConnect = new ChannelResourceClient();
		try {
			DomRepresentation representation = itemConnect
					.retrieveChannelByName(channelName);
			if (representation != null) {
				channels = itemConnect.getChannelsFromXml(representation);
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
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.searchchannel);
		this.setTitle("AdSpot - Search Item");
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.ic_menu_search);
		elChannelName = (EditText) findViewById(R.id.channelName);
		channelListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			channelName = b.getString("channelName");
			if (channelName != null) {
				elChannelName.setText(channelName);
				if (!(channelName.equalsIgnoreCase(""))) {
					channels = getChannelsByName(channelName);
					// products = getProducts();
				} else {
					channels = getChannels();
				}
			}
		}
		adapter = new ChannelAdapter(this, channels);
		channelListView.setAdapter(adapter);
		btnSearch = (Button) findViewById(R.id.searchbutton);

		// Set Click Listener
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String prodName = elChannelName.getText().toString();
				channels = getChannelsByName(prodName);
				adapter = new ChannelAdapter(SearchChannel.this, channels);
				channelListView.setAdapter(adapter);
			}
		});

		elChannelName.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String prodName = elChannelName.getText().toString();
				channels = getChannelsByName(prodName);
				adapter = new ChannelAdapter(SearchChannel.this, channels);
				channelListView.setAdapter(adapter);
			}
		});

		// Set Click Listener
		channelListView.setClickable(true);
		channelListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load Ad
				Intent intent = new Intent(SearchChannel.this, Product.class);
				Bundle b = new Bundle();
				b.putString("uname", uname);
				b.putString("channelId", String.valueOf(((ChannelBean) channels
						.get(position)).getChannelid()));
				b.putString("channelName", ((ChannelBean) channels
						.get(position)).getChannelname().toString());
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
			Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SearchChannel.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(SearchChannel.this, LBALocation.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}