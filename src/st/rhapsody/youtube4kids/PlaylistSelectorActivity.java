package st.rhapsody.youtube4kids;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;

public class PlaylistSelectorActivity extends Activity {
	private Gallery gallery;
	private ImageAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlistselector);

		gallery = (Gallery) findViewById(R.id.playlistselectgallery);
		imageAdapter = new ImageAdapter(this);

		PreviewPlaylistTask playlistTask = new PreviewPlaylistTask(new AsyncCallback<List<PlaylistEntry>>() {
			@Override
			public void call(List<PlaylistEntry> result) {
				
				imageAdapter.setPlaylistEntries(result);
				gallery.setAdapter(imageAdapter);
			}
		});
		
		playlistTask.execute("CE2937EB39FAD5C9","17C039397320E463","39DA49B3FC33FAA5","396B5D0A58C80879","93A073013645CB15","457DD199FC856DA8","4B3F3073B5E10F63"); // Needs to be replaced with configurable IDs
		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				String youtubeid = imageAdapter.getItem(position);
				Intent i = new Intent(PlaylistSelectorActivity.this,VideoPlayerActivity.class);
				i.putExtra(VideoPlayerActivity.ID, youtubeid);
				startActivity(i);
			}
		});
	}
}
