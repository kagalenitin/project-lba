package com.lba.channel;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lba.R;
import com.lba.beans.ChannelBean;

/**
 * @author payal
 * 
 */
public class ChannelAdapter extends BaseAdapter implements Filterable {

	private Activity activity;
	private ArrayList<ChannelBean> data;
	private static LayoutInflater inflater = null;
    private ArrayFilter mFilter;

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

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
            mFilter = new ArrayFilter();
            mFilter.mOriginalValues = new ArrayList();
            for(int i=0;i<data.size();i++){
                mFilter.mOriginalValues.add(data.get(i).getChannelname());
            }
        }
        return mFilter;
	}
	
    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
    	
        private List mObjects;
        private ArrayList mOriginalValues;
        private final Object mLock = new Object();
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList list = new ArrayList(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList values = mOriginalValues;
                final int count = values.size();

                final ArrayList newValues = new ArrayList(count);

                for (int i = 0; i < count; i++) {
                    final Object value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


}