package st.rhapsody.youtube4kids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Context applicationContext;
	private Map<Integer, View> viewCache = new HashMap<Integer, View>();
	private List<PlaylistEntry> playlistEntries = new ArrayList<PlaylistEntry>();

	public ImageAdapter(Activity activity) {
		this.applicationContext = activity.getApplicationContext();
	}

	public void setPlaylistEntries(List<PlaylistEntry> playlistEntries) {
		this.playlistEntries = playlistEntries;
	}

	@Override
	public int getCount() {
		return playlistEntries.size();
	}

	@Override
	public String getItem(int position) {
		return playlistEntries.get(position).getId();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View cachedView = getCachedView(position, convertView);
		if (cachedView != null) {
			return cachedView;
		}

		final ImageView imageView = new ImageView(applicationContext);

		try {
			PlaylistThumbsTask playlistThumbsTask = new PlaylistThumbsTask(new AsyncCallback<Bitmap>() {

				@Override
				public void call(Bitmap result) {
					imageView.setImageBitmap(result);
				}
			});

			playlistThumbsTask.execute(playlistEntries.get(position).getThumbUrl());
 
		} catch (Exception e) {
			e.printStackTrace();
		}

		LinearLayout linearLayout = createLayout(playlistEntries.get(position).getTitle(), imageView);
		viewCache.put(position, linearLayout);

		return linearLayout;
	}

	private View getCachedView(int position, View convertView) {
		if (convertView != null) {
			return convertView;
		}
		if (viewCache.containsKey(position)) {
			return viewCache.get(position);
		}
		return null;
	}

	private LinearLayout createLayout(String title, ImageView imageView) {
		LinearLayout linearLayout = new LinearLayout(applicationContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		imageView.setLayoutParams(new Gallery.LayoutParams(320, 180));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

		TextView textView = new TextView(applicationContext);
		textView.setText(title);

		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		linearLayout.setPadding(20, 20, 20, 20);

		return linearLayout;
	}
}
