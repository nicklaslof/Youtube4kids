package st.rhapsody.youtube4kids;

public class PlaylistEntry {

	private final String title;
	private final String id;
	private final String thumbUrl;

	public PlaylistEntry(String title, String id, String thumbUrl) {
		this.title = title;
		this.id = id;
		this.thumbUrl = thumbUrl;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}
}
