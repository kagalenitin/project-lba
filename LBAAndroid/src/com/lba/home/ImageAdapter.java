/**
 * 
 */
package com.lba.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lba.R;

/**
 * @author payal
 * 
 */
public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private static LayoutInflater inflater = null;

	public ImageAdapter(Context c, Activity activity) {
		mContext = c;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView;
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			vi = inflater.inflate(R.layout.welcome, null);
			holder = new ViewHolder();
			//
			// if(convertView==null){
			// LayoutInflater li = getLayoutInflater();
			// v = li.inflate(R.layout.icon, null);
			// TextView tv = (TextView)v.findViewById(R.id.icon_text);
			// tv.setText("Profile "+position);
			// ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
			// iv.setImageResource(R.drawable.icon);

			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);

			holder.text = (TextView) vi.findViewById(R.id.icon_text);
			holder.image = (ImageView) vi.findViewById(R.id.icon_image);
			// holder.image.setLayoutParams(new GridView.LayoutParams(80, 80));
			// holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// holder.image.setPadding(1, 1, 1, 1);

			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
			// imageView = (ImageView) convertView;
		}

		// imageView.setImageResource(mThumbIds[position]);

		holder.text.setText(mTextIds[position]);
		holder.image.setImageResource(mThumbIds[position]);
		return vi;
	}

	// create a new ImageView for each item referenced by the Adapter
	/*
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * ImageView imageView; if (convertView == null) { // if it's not recycled,
	 * initialize some // attributes imageView = new ImageView(mContext);
	 * imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
	 * imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	 * imageView.setPadding(8, 8, 8, 8); } else { imageView = (ImageView)
	 * convertView; }
	 * 
	 * imageView.setImageResource(mThumbIds[position]); return imageView; }
	 */
	// references to our images
	/*
	 * private Integer[] mThumbIds = {
	 * 
	 * R.drawable.largetiles48, R.drawable.bluelocationpin, R.drawable.user,
	 * R.drawable.topview, R.drawable.search,
	 * R.drawable.preferencesdesktopnotification, R.drawable.image,
	 * R.drawable.settingsicon, R.drawable.directions_icon, };
	 */

	private Integer[] mThumbIds = {

	R.drawable.largetiles48, R.drawable.bluelocationpin, R.drawable.user,
			R.drawable.topview, R.drawable.searchiconsmall,
			R.drawable.preferencesdesktopnotification, R.drawable.largetiles48,
			R.drawable.settingsicon, R.drawable.im_directions_icon_86x82 };

	private String[] mTextIds = {

	"Channel", "GMap", "Profile", "Products", "Search", "Alert", "Ads",
			"Settings", "Direction" };
}
