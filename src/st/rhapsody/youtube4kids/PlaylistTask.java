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
	private static final String V_2_ALT_JSON = "?v=2&alt=json";



	public PlaylistTask(AsyncCallback<List<PlaylistEntry>> callback) {
		super(callback);
	}
	
	@Override
	protected List<PlaylistEntry> doInBackground(String... params) {

		String playlistId = params[0];
		ArrayList<PlaylistEntry> playlists = new ArrayList<PlaylistEntry>();
		
		try {			
			JSONArray entires = getYoutubeRootEntry(playlistId).getJSONObject("feed").getJSONArray("entry");
			
			for (int i = 0; i < entires.length(); i++) {
				String title = "";
				String videoId = "";
				String url = "";
				
				JSONObject entry = entires.getJSONObject(i);
				
				title = entry.getJSONObject("title").getString("$t");		
				url = getThumbUrl(url, entry.getJSONObject("media$group").getJSONArray("media$thumbnail"));
				videoId = getVideoId(videoId, entry.getJSONArray("link"));
				
				playlists.add(new PlaylistEntry(title, videoId,url));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.unmodifiableList(playlists);
	}

	private String getVideoId(String videoId, JSONArray links) throws JSONException {
		for (int j = 0; j < links.length(); j++) {
			JSONObject link = links.getJSONObject(j);

			String rel = link.optString("rel", null);
			if (rel != null && rel.equals("alternate")) {
				String href = link.optString("href", null);
				Uri parsedUri = Uri.parse(href);
				videoId = parsedUri.getQueryParameter("v");
				break;
			}
		}
		return videoId;
	}

	private String getThumbUrl(String url, JSONArray thumbs) throws JSONException {
		for (int thumbCounter = 0; thumbCounter < thumbs.length(); thumbCounter++){
			JSONObject object = thumbs.getJSONObject(thumbCounter);
			String name = object.getString("yt$name");
			
			if (name != null && name.equals("mqdefault")){
				url = object.getString("url");
				break;
			}
		}
		return url;
	}

	private JSONObject getYoutubeRootEntry(String playlistId) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(HTTPS_GDATA_YOUTUBE_COM_FEEDS_API_PLAYLISTS + playlistId + V_2_ALT_JSON);
		HttpResponse youtubeResponse = defaultHttpClient.execute(get);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		youtubeResponse.getEntity().writeTo(byteArrayOutputStream);
		String respsonseString = byteArrayOutputStream.toString("UTF-8");
		System.out.println(respsonseString);
		JSONObject youtubeJson = new JSONObject(respsonseString);
		return youtubeJson;
	}
}
