package st.rhapsody.youtube4kids;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class PlaylistTask extends AsynctaskWithCallback<String, Void, List<PlaylistEntry>> {
	private static final String HTTPS_GDATA_YOUTUBE_COM_FEEDS_API_PLAYLISTS = "https://gdata.youtube.com/feeds/api/playlists/";
	private static final String V_2_ALT_JSON_MAX_RESULTS = "?v=2&alt=json&max-results=";

	public PlaylistTask(AsyncCallback<List<PlaylistEntry>> callback) {
		super(callback);
	}

	@Override
	protected List<PlaylistEntry> doInBackground(String... params) {

		String playlistId = params[0];
		ArrayList<PlaylistEntry> playlists = new ArrayList<PlaylistEntry>();

		try {
			JSONArray entires = getYoutubeRootEntry(playlistId,50,0).getJSONObject("feed").getJSONArray("entry");

			for (int entryCounter = 0; entryCounter < entires.length(); entryCounter++) {
				String title = "";
				String videoId = "";
				String url = "";

				JSONObject entry = entires.getJSONObject(entryCounter);
				
				title = getTitle(entry);
				
				if (title.equals("") || !entry.getJSONObject("media$group").has("media$thumbnail")){
					continue;
				}
				
				url = getThumbUrl(entry.getJSONObject("media$group").getJSONArray("media$thumbnail"));
				videoId = getVideoId(videoId, entry.getJSONArray("link"));

				playlists.add(new PlaylistEntry(title, videoId, url));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.unmodifiableList(playlists);
	}

	protected String getTitle(JSONObject entry) throws JSONException {
		return entry.getJSONObject("title").getString("$t");
	}

	private String getVideoId(String videoId, JSONArray links) throws JSONException {
		for (int linkCounter = 0; linkCounter < links.length(); linkCounter++) {
			JSONObject link = links.getJSONObject(linkCounter);
			String rel = link.optString("rel", null);
			if (rel != null && rel.equals("alternate")) {
				videoId = Uri.parse(link.optString("href", null)).getQueryParameter("v");
				break;
			}
		}
		return videoId;
	}

	protected String getThumbUrl(JSONArray thumbs) throws JSONException {
		
		for (int thumbCounter = 0; thumbCounter < thumbs.length(); thumbCounter++) {
			JSONObject object = thumbs.getJSONObject(thumbCounter);
			String name = object.optString("yt$name", null);
			if (name != null && name.equals("mqdefault")) {
				return object.getString("url");
			}
		}
		return "";
	}

	protected JSONObject getYoutubeRootEntry(String playlistId, int maxResults, int page) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		String url = HTTPS_GDATA_YOUTUBE_COM_FEEDS_API_PLAYLISTS + playlistId + V_2_ALT_JSON_MAX_RESULTS+maxResults;
		System.out.println("opening URL "+url);
		HttpResponse youtubeResponse = defaultHttpClient.execute(new HttpGet(url));
		if (youtubeResponse.getStatusLine().getStatusCode() >= 400){
			throw new IOException("call to "+url+" resulted in status code "+youtubeResponse.getStatusLine().getStatusCode());
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		youtubeResponse.getEntity().writeTo(byteArrayOutputStream);
		String respsonseString = byteArrayOutputStream.toString("UTF-8");
		return new JSONObject(respsonseString);
	}
}
