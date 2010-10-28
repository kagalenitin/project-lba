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
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);

			final int w = (int) (36 * vi.getResources().getDisplayMetrics().density + 0.5f);
			imageView.setLayoutParams(new GridView.LayoutParams(w, w));

			holder.text = (TextView) vi.findViewById(R.id.icon_text);
			holder.image = (ImageView) vi.findViewById(R.id.icon_image);

			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.text.setText(mTextIds[position]);
		holder.image.setImageResource(mThumbIds[position]);
		return vi;
	}

	// references to our images

	private Integer[] mThumbIds = {

			// R.drawable.largetiles48, R.drawable.bluelocationpin48,
			// R.drawable.user48,
			// R.drawable.topview48, R.drawable.searchiconsmall48,
			// R.drawable.preferencesdesktopnotification48,
			// R.drawable.largetiles48, R.drawable.settingsicon48,
			// R.drawable.im_directions_icon_48 };

			R.drawable.largetiles48, R.drawable.black_pin, R.drawable.user_48,
			R.drawable.topview48, R.drawable.searchiconsmall48,
			R.drawable.rss1, R.drawable.button_add_to_cart,
			R.drawable.settingsicon48, R.drawable.im_directions_icon_48 };
	private String[] mTextIds = {

	"Channel", "LocateAd", "Profile", "Products", "Search", "Subscription",
			"Ads", "Subscribe", "Direction" };
}
