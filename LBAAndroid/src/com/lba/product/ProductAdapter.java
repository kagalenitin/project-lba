package com.lba.product;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lba.R;
import com.lba.beans.ProductBean;

/**
 * @author payal
 * 
 */
public class ProductAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ProductBean> data;
	private static LayoutInflater inflater = null;

	public ProductAdapter(Activity a, ArrayList<ProductBean> products) {
		activity = a;
		data = products;
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
			vi = inflater.inflate(R.layout.productdetail, null);
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
			holder.text.setText("Channel " + position);
			holder.desc.setText("Service Not Available");
		} else {
			holder.text.setText(data.get(position).getProductName());
			holder.desc.setText(data.get(position).getProductdescription());
		}
		return vi;
	}
}