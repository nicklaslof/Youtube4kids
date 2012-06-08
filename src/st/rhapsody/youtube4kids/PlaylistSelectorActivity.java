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
		
		playlistTask.execute("2E2D35D38C9750C1","68282CFF66E9E68D"); // Needs to be replaced with configurable IDs
		
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
