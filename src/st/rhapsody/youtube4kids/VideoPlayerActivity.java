package st.rhapsody.youtube4kids;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

public class VideoPlayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoselector);

		Gallery gallery = (Gallery) findViewById(R.id.gallery1);
		final ImageAdapter imageAdapter = new ImageAdapter(this);
		
		PlaylistTask playlistTask = new PlaylistTask(imageAdapter,gallery);
		playlistTask.execute("2E2D35D38C9750C1");

		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
				String youtubeid = (String) imageAdapter.getItem(position);
				 Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"+youtubeid), VideoPlayerActivity.this,YoutubePlayerActivity.class);
				startActivity(lVideoIntent);
			}
		});
	}
}
