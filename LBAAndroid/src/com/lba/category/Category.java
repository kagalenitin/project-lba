/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lba.category;

import java.io.IOException;
import java.util.ArrayList;

import org.restlet.ext.xml.DomRepresentation;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.lba.R;
import com.lba.beans.CategoryBean;
import com.lba.beans.ChannelBean;
import com.lba.home.WelcomeUser;
import com.lba.product.Product;
import com.lba.search.SearchProduct;
import com.lba.service.CategoryResourceClient;
import com.lba.service.ChannelResourceClient;

/**
 * @author payalpatel
 * 
 */
public class Category extends ExpandableListActivity {

	ExpandableListAdapter mAdapter;
	static ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
	static ArrayList<ChannelBean> channels = new ArrayList<ChannelBean>();

	public static ArrayList<CategoryBean> getCategories() {

		CategoryResourceClient categoryResource = new CategoryResourceClient();
		try {
			DomRepresentation representation = categoryResource.getCategories();
			if (representation != null) {
				categories = categoryResource
				.getCategoriesFromXml(representation);
			} else {
				categories = new ArrayList<CategoryBean>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return categories;
	}

	public static ArrayList<ChannelBean> getChannelsByUser(String categoryName) {

		ChannelResourceClient channelResource = new ChannelResourceClient();
		try {
			DomRepresentation representation = channelResource
			.retrieveChannelsByCategory(categoryName);
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

	String uname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up our adapter
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		this.setTitle("Location Based Advertisement - Category");
		mAdapter = new MyExpandableListAdapter();
		setListAdapter(mAdapter);
		registerForContextMenu(getExpandableListView());
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.logo);
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b = intent.getExtras();
		if (b != null) {
			uname = b.getString("uname");
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Category");
		menu.add(0, 0, 0, R.string.expandable_list_sample_action);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
		.getMenuInfo();

		String title = ((TextView) info.targetView).getText().toString();

		int type = ExpandableListView
		.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int groupPos = ExpandableListView
			.getPackedPositionGroup(info.packedPosition);
			int childPos = ExpandableListView
			.getPackedPositionChild(info.packedPosition);
			Toast.makeText(
					this,
					title + ": Child " + childPos + " clicked in group "
					+ groupPos, Toast.LENGTH_SHORT).show();
			return true;
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			int groupPos = ExpandableListView
			.getPackedPositionGroup(info.packedPosition);
			Toast.makeText(this, title + ": Group " + groupPos + " clicked",
					Toast.LENGTH_SHORT).show();
			return true;
		}

		return false;
	}

	/**
	 * A simple adapter which maintains an ArrayList of category resource. Each
	 * category is displayed.
	 * 
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private ArrayList<String> group = loadCategory();
		private ArrayList<String> child[] = loadChannelByCategory();
		private ArrayList<String> childId[];

		public ArrayList<String> loadCategory() {
			categories = getCategories();
			ArrayList<String> g = new ArrayList<String>();
			for (int i = 0; i < categories.size(); i++) {
				System.out.println(Log.VERBOSE
						+ "categories.get(i).getCategoryName();"
						+ categories.get(i).getCategoryName());
				g.add(categories.get(i).getCategoryName());
			}
			return g;
		}

		public ArrayList<String>[] loadChannelByCategory() {
			ArrayList<String>[] lists = new ArrayList[categories.size()];
			ArrayList<String> c[] = lists;
			ArrayList<String>[] lists2 = new ArrayList[categories.size()];
			ArrayList<String> cId[] = lists2;

			for (int i = 0; i < categories.size(); i++) {
				String categoryName = group.get(i);
				c[i] = new ArrayList<String>();
				cId[i] = new ArrayList<String>();
				channels = getChannelsByUser(categoryName);
				for (int j = 0; j < channels.size(); j++) {
					System.out.println(Log.VERBOSE
							+ "channels.get(i).getChannelname().toString()"
							+ channels.get(j).getChannelname().toString());
					c[i].add(channels.get(j).getChannelname().toString());
					cId[i].add(channels.get(j).getChannelid().toString());
				}
			}
			childId = cId;
			return c;
		}

		public Object getChildChannelId(int groupPosition, int childPosition) {
			return childId[groupPosition].get(childPosition);
		}

		public Object getChild(int groupPosition, int childPosition) {
			// return children[groupPosition][childPosition];
			return child[groupPosition].get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// return children[groupPosition].length;
			return child[groupPosition].size();
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(Category.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final int position = childPosition;
			final int gposition = groupPosition;
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			textView.setFocusable(true);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Category.this, Product.class);
					Bundle b = new Bundle();
					b.putString("channelId",
							getChildChannelId(gposition, position).toString());
					b.putString("channelName", getChild(gposition, position)
							.toString());
					b.putString("uname", uname);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
			return textView;
		}

		public Object getGroup(int groupPosition) {
			// return groups[groupPosition];
			return group.get(groupPosition);
		}

		public int getGroupCount() {
			// return groups.length;
			return group.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}

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
			Intent intent = new Intent(Category.this, WelcomeUser.class);
			Bundle b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			intent = new Intent(Category.this, SearchProduct.class);
			b = new Bundle();
			b.putString("uname", uname);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}
}
