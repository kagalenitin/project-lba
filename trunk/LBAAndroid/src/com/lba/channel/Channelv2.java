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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lba.R;
import com.lba.beans.ChannelBean;
import com.lba.product.Product;
import com.lba.service.ChannelResourceClient;

/**
 * @author payal
 * 
 */
public class Channelv2 extends Activity { // implements OnClickListener{

	private ListView channelListView;
	private TextView lblChannelCode;

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
			String uname = b.getString("uname");
			// lblChannelCode.setText("Welcome, " + uname);
			if (!(uname.equalsIgnoreCase("default"))) {
				lblChannelCode.setText("Welcome, " + uname);
			}
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
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		// channelListView.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, channel_array));

	}
}