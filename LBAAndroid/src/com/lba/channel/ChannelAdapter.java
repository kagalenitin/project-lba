package com.lba.channel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lba.R;
import com.lba.beans.ChannelBean;

/**
 * @author payal
 * 
 */
public class ChannelAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ChannelBean> data;
	private static LayoutInflater inflater = null;

	public ChannelAdapter(Activity a, ArrayList<ChannelBean> channels) {
		activity = a;
		data = channels;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text;
		public TextView desc;
		public CheckBox check;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		int[] colors = new int[] { 0x30ffffff, 0x30808080 };

		if (convertView == null) {
			vi = inflater.inflate(R.layout.channel_detail, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.channelName);
			holder.desc = (TextView) vi.findViewById(R.id.channelDesc);
			// holder.check = (CheckBox) vi.findViewById(R.id.ChannelSub);

			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (getCount() == 0) {
			holder.text.setText("Channel " + position);
			holder.desc.setText("");
		} else {
			int colorPos = position % colors.length;
			vi.setBackgroundColor(colors[colorPos]);
			// vi.setBackgroundResource(R.layout.customshape);
			holder.text.setText(data.get(position).getChannelname());
			holder.desc.setText(data.get(position).getChanneldescription());
		}
		return vi;
	}
}