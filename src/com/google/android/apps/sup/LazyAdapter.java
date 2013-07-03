package com.google.android.apps.sup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements Filterable {

	private Activity activity;
	NewsActivity newsactivity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView artist = (TextView) vi.findViewById(R.id.artist); // artist
																	// name
		TextView duration = (TextView) vi.findViewById(R.id.duration); // duration
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image
		HashMap<String, String> song = new HashMap<String, String>();
		song = data.get(position);

		// Setting all values in listview
		title.setText(song.get(GlobalInfo.KEY_TITLE));
		artist.setText(song.get(GlobalInfo.KEY_ARTIST));
		duration.setText(song.get(GlobalInfo.KEY_DURATION));
		imageLoader.DisplayImage(song.get(GlobalInfo.KEY_THUMB_URL),
				thumb_image);
		return vi;
	}

	public android.widget.Filter getFilter() {

		return new android.widget.Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				LazyAdapter.this.notifyDataSetChanged();

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults Result = new FilterResults();
				// if constraint is empty return the original names
				if (constraint.length() == 0) {
					Result.values = data;
					Result.count = data.size();
					return Result;
				}

				ArrayList<HashMap<String, String>> Filtered_Names = new ArrayList<HashMap<String, String>>();
				String filterString = constraint.toString().toLowerCase();
				String filterableString;

				for (int i = 0; i < data.size(); i++) {
					filterableString = data.get(i).get(GlobalInfo.KEY_ARTIST);
					if (filterableString.toLowerCase().contains(filterString)) {
						Filtered_Names.add(data.get(i));
						Log.i("LazyAdapter match", filterableString);
						Log.i("LazyAdapter query", filterString);
					}
				}
				Result.values = Filtered_Names;
				Result.count = Filtered_Names.size();

				return Result;
			}

		};
	}

}
