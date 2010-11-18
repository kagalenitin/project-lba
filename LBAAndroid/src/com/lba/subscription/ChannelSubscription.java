/**
 * 
 */
package com.lba.subscription;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.ChannelBean;
import com.lba.beans.ChannelSubscriptionBean;
import com.lba.home.WelcomeUser;
import com.lba.search.SearchProduct;
import com.lba.service.ChannelResourceClient;
import com.lba.service.UserSubscriptionResourceClient;

/**
 * @author payal
 * 
 */
public class ChannelSubscription extends Activity { // implements
	// OnClickListener{

	private ListView channelListView;
	String uname;
	static ArrayList<ChannelBean> channels = new ArrayList<ChannelBean>();
	private Button btnSearch;
	private EditText elChannelName;

	public static ArrayList<ChannelBean> getChannelsByUser(String username) {

		ChannelResourceClient channelResource = new ChannelResourceClient();
		try {
			DomRepresentation representation = channelResource
					.retrieveChannelsByUser(username);
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

	public static ArrayList<ChannelBean> getChannelsByNameByUser(
			String channelName, String username) {

		ChannelResourceClient channelResource = new ChannelResourceClient();
		try {
			DomRepresentation representation = channelResource
					.retrieveChannelsByNameByUser(channelName, username);
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

	public void subscribeChannel(String channelId, String username) {
		UserSubscriptionResourceClient userSubscriptionResourceClient = new UserSubscriptionResourceClient(
				username);
		ChannelSubscriptionBean userSubscription = new ChannelSubscriptionBean();
		userSubscription.setChanneld(channelId);
		userSubscription.setUserId(username);
		userSubscriptionResourceClient.createSubscription(userSubscription);
	}

	public void unsubscribeChannel(String channelId, String username) {
		UserSubscriptionResourceClient userSubscriptionResourceClient = new UserSubscriptionResourceClient(
				username);
		ChannelSubscriptionBean userSubscription = new ChannelSubscriptionBean();
		userSubscription.setChanneld(channelId);
		userSubscription.setUserId(username);
		userSubscriptionResourceClient
				.deleteSubscriptionByUser(userSubscription);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setContentView(R.layout.channelsubscription);
		this.setTitle("AdSpot - MyChannel");
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,
				R.drawable.feed_icon_grey_48px);

		channelListView = (ListView) findViewById(R.id.ListView01);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
			if (!(uname.equalsIgnoreCase("default"))) {
			}
		}
		channels = getChannelsByUser(uname);
		String lv_items[] = new String[channels.size()];
		for (int i = 0; i < channels.size(); i++) {
			lv_items[i] = new String(channels.get(i).getChannelname());
		}
		channelListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, lv_items));
		btnSearch = (Button) findViewById(R.id.searchbutton);
		// Set Click Listener
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String channelName = elChannelName.getText().toString();
				if (channelName.equals("")) {
					channels = getChannelsByUser(uname);
				} else {
					channels = getChannelsByNameByUser(channelName, uname);
				}
				String lv_items[] = new String[channels.size()];
				for (int i = 0; i < channels.size(); i++) {
					lv_items[i] = new String(channels.get(i).getChannelname());
				}
				channelListView.setAdapter(new ArrayAdapter<String>(
						ChannelSubscription.this,
						android.R.layout.simple_list_item_multiple_choice,
						lv_items));
				channelListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				channelListView.setClickable(true);
			}
		});

		elChannelName = (EditText) findViewById(R.id.channelName);
		elChannelName.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String channelName = elChannelName.getText().toString();
				if (channelName.equals("")) {
					channels = getChannelsByUser(uname);
				} else {
					channels = getChannelsByNameByUser(channelName, uname);
				}
				String lv_items[] = new String[channels.size()];
				for (int i = 0; i < channels.size(); i++) {
					lv_items[i] = new String(channels.get(i).getChannelname());
				}
				channelListView.setAdapter(new ArrayAdapter<String>(
						ChannelSubscription.this,
						android.R.layout.simple_list_item_multiple_choice,
						lv_items));
				channelListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				channelListView.setClickable(true);
			}
		});

		// Set Click Listener
		channelListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		channelListView.setClickable(true);
		channelListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				boolean pos = channelListView.isItemChecked(position);
				System.out.println(Log.VERBOSE + "output " + pos);
				if (pos == true) {
					String channelId = ((ChannelBean) channels.get(position))
							.getChannelid().toString();
					subscribeChannel(channelId, uname);
					Toast.makeText(
							ChannelSubscription.this,
							"You're subscribed to "
									+ channels.get(position).getChannelname()
									+ "", Toast.LENGTH_SHORT).show();
				} else {
					String channelId = ((ChannelBean) channels.get(position))
							.getChannelid().toString();
					unsubscribeChannel(channelId, uname);
					Toast.makeText(ChannelSubscription.this,
							"Channel Unsubscribed ", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}// end create

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
			Intent intent = new Intent(ChannelSubscription.this,
					WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(ChannelSubscription.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}