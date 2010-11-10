package com.lba.search;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.searchchanneladapter, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.prodName);
			holder.desc = (TextView) vi.findViewById(R.id.prodDesc);
			vi.setTag(holder);
			int[] colors = new int[] { 0x30ffffff, 0x30808080 };
			int colorPos = position % colors.length;
			vi.setBackgroundColor(colors[colorPos]);
		} else
			holder = (ViewHolder) vi.getTag();

		if (getCount() == 0) {
			holder.text.setText("Search Channel " + position);
			holder.desc.setText("Service Not Available");
		} else {

			holder.text.setText(data.get(position).getChannelname());
			holder.desc.setText(data.get(position).getChanneldescription());
		}
		return vi;
	}
}