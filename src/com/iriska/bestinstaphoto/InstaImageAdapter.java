package com.iriska.bestinstaphoto;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstaImageAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<String> imageSources = new ArrayList<String>();
	private ArrayList<Integer> imageLikes = new ArrayList<Integer>();
	
	public InstaImageAdapter(Context c,
			ArrayList<String> images, ArrayList<Integer> likes) {
		mContext = c;
		imageSources = images;
		imageLikes = likes;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageSources.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageSources.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		v = vi.inflate(R.layout.picture_item, null);
		ImageView imageInsta = (ImageView) v.findViewById(R.id.imgCollage);
		
		TextView tv = (TextView) v.findViewById(R.id.textviewLikes);
		tv.setText(imageLikes.get(position).toString() + " " + "\u2665");
		
		InstaApp.getImageLoader().displayImage(
				imageSources.get(position).toString(), imageInsta);
		return v;
	}

}
