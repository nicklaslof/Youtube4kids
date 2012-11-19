package st.rhapsody.youtube4kids;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

public class VideoPlayerActivity extends Activity {

	private ImageAdapter imageAdapter;
	private Gallery gallery;
	private Youtube4KidsApplication app;
	public static final String ID = "id";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Youtube4KidsApplication) getApplication();
		setContentView(R.layout.videoselector);

		gallery = (Gallery) findViewById(R.id.gallery1);
		imageAdapter = new ImageAdapter(this);
		
		PlaylistTask playlistTask = new PlaylistTask(new AsyncCallback<List<PlaylistEntry>>() {

			@Override
			public void call(List<PlaylistEntry> result) {
				imageAdapter.setPlaylistEntries(result);
				gallery.setAdapter(imageAdapter);
			}
		});
		
		String id = getIntent().getExtras().getString(ID);
		
		playlistTask.execute(id);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				String youtubeid = imageAdapter.getItem(position);
				Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + youtubeid), VideoPlayerActivity.this, YoutubePlayerActivity.class);
				app.setVideoPlaying(true);
				startActivity(lVideoIntent);
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!app.isVideoPlaying()) {
			finish();
		}
	}
}
