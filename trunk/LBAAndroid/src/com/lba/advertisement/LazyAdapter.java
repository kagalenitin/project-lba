package com.lba.advertisement;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lba.R;
import com.lba.beans.AdvertisementBean;

/**
 * @author payal
 * 
 */
public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();
	private String adPath = "";

	String adLocation = "";

	public LazyAdapter(Activity a, ArrayList<AdvertisementBean> advertisements) {
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		this.advertisements = advertisements;
		System.out.println("tomcatServer "
				+ a.getResources().getString(R.string.tomcatServer));
		this.adPath = a.getResources().getString(R.string.tomcatServer);
	}

	public int getCount() {
		return advertisements.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text;
		public ImageView image;
		public TextView desc;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		int[] colors = new int[] { 0x30ffffff, 0x30808080 };

		if (convertView == null) {
			vi = inflater.inflate(R.layout.advertisement, null);
			holder = new ViewHolder();
			holder.desc = (TextView) vi.findViewById(R.id.text01);
			holder.text = (TextView) vi.findViewById(R.id.text);
			holder.image = (ImageView) vi.findViewById(R.id.image);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		int colorPos = position % colors.length;
		vi.setBackgroundColor(colors[colorPos]);
		if (advertisements.size() != 0) {
			String res = ((AdvertisementBean) advertisements.get(position))
					.getAdDesc();
			holder.text.setText(((AdvertisementBean) advertisements
					.get(position)).getAdName());
			System.out.println("Path");
			// holder.image.setTag(data[position]);
			adLocation = adPath
					+ advertisements.get(position).getFileLocation();
			holder.image.setTag(adPath
					+ advertisements.get(position).getFileLocation());
			holder.desc.setText(res);
		} else {
			String res = "Service not Available";
			holder.text.setText("Service not Available");
			adLocation = adPath + "/AdvertiserLBA/nopicture.gif";
			holder.image.setTag(R.drawable.nopicture);
			holder.desc.setText(res);
		}

		imageLoader.DisplayImage(adLocation, activity, holder.image);
		return vi;
	}
}