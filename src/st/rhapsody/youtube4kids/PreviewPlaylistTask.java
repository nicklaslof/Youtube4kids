package st.rhapsody.youtube4kids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PreviewPlaylistTask extends PlaylistTask {

	public PreviewPlaylistTask(AsyncCallback<List<PlaylistEntry>> callback) {
		super(callback);
	}

	@Override
	protected List<PlaylistEntry> doInBackground(String... params) {
		List<PlaylistEntry> list = new ArrayList<PlaylistEntry>();
		for (int i = 0; i < params.length; i++) {
			try {
				String playlistId = params[i];

				JSONObject rootEntry = getYoutubeRootEntry(playlistId,0,0).getJSONObject("feed");

				String title = getTitle(rootEntry);
				//JSONArray entries = rootEntry.getJSONArray("entry");
				//JSONObject firstEntry = entries.getJSONObject(0);
				String thumbUrl = getThumbUrl(rootEntry.getJSONObject("media$group").getJSONArray("media$thumbnail"));
				list.add(new PlaylistEntry(title, playlistId, thumbUrl));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Collections.unmodifiableList(list);
	}
}
