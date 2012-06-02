package st.rhapsody.youtube4kids;

import android.app.Application;

public class Youtube4KidsApplication extends Application{
	
	private boolean videoPlaying = false;
	
	public boolean isVideoPlaying() {
		return videoPlaying;
	}
	
	public void setVideoPlaying(boolean videoPlaying) {
		this.videoPlaying = videoPlaying;
	}
}
