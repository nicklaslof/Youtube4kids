package st.rhapsody.youtube4kids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Context applicationContext;
	private Map<Integer,View> viewCache = new HashMap<Integer,View>();
	private List<PlaylistEntry> playlistEntries = new ArrayList<PlaylistEntry>();

	public ImageAdapter(VideoPlayerActivity videoPlayerActivity) {
		this.applicationContext = videoPlayerActivity.getApplicationContext();
		
	}
	
	public void setPlaylistEntries(List<PlaylistEntry> playlistEntries) {
		this.playlistEntries = playlistEntries;
	}

	@Override
	public int getCount() {
		return playlistEntries.size();
	}

	@Override
	public Object getItem(int position) {
		return playlistEntries.get(position).getId();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView != null){
			return convertView;
		}
		
		if (viewCache.containsKey(position)){
			return viewCache.get(position);
		}
		
		LinearLayout linearLayout = new LinearLayout(applicationContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		ImageView imageView = new ImageView(applicationContext);

		try {
			
			imageView.setLayoutParams(new Gallery.LayoutParams(320, 180));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			
			PlaylistThumbsTask playlistThumbsTask = new PlaylistThumbsTask(imageView);
			
			playlistThumbsTask.execute(playlistEntries.get(position).getThumbUrl());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextView textView = new TextView(applicationContext);
		textView.setText(playlistEntries.get(position).getTitle());

		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		linearLayout.setPadding(20, 20, 20, 20);
		
		viewCache.put(position, linearLayout);
		
		return linearLayout;
	}
}
