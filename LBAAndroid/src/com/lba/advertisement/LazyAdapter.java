package com.lba.advertisement;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

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
import com.lba.service.AdvertisementResourceClient;

/**
 * @author payal
 * 
 */
public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private String[] data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	static ArrayList<AdvertisementBean> advertisements = new ArrayList<AdvertisementBean>();

	public LazyAdapter(Activity a, String[] d, String productId) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		advertisements = getAdsByProduct(productId);
	}

	public LazyAdapter(Activity a, String[] d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		advertisements = getAdvertisements();
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static ArrayList<AdvertisementBean> getAdvertisements() {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisements();
			if (representation != null) {
				advertisements = advertisementResource
						.getAdvertisementsFromXml(representation);
			} else {
				advertisements = new ArrayList<AdvertisementBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	public static ArrayList<AdvertisementBean> getAdsByProduct(String productId) {

		AdvertisementResourceClient advertisementResource = new AdvertisementResourceClient();
		try {
			DomRepresentation representation = advertisementResource
					.retrieveAdvertisementsByProduct(productId);
			if (representation != null) {
				advertisements = advertisementResource
						.getAdvertisementsFromXml(representation);
			} else {
				advertisements = new ArrayList<AdvertisementBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	public static class ViewHolder {
		public TextView text;
		public ImageView image;
		public TextView desc;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.advertisement, null);
			holder = new ViewHolder();
			holder.desc = (TextView) vi.findViewById(R.id.text01);
			holder.text = (TextView) vi.findViewById(R.id.text);
			holder.image = (ImageView) vi.findViewById(R.id.image);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		if (advertisements.size() != 0) {
			String res = ((AdvertisementBean) advertisements.get(position))
					.getAdDesc();
			holder.text.setText(((AdvertisementBean) advertisements
					.get(position)).getAdName());
			holder.image.setTag(data[position]);
			holder.desc.setText(res);
		} else {
			String res = "Service not Available";
			holder.text.setText("Service not Available");
			holder.image.setTag(data[position]);
			holder.desc.setText(res);
		}

		imageLoader.DisplayImage(data[position], activity, holder.image);
		return vi;
	}
}