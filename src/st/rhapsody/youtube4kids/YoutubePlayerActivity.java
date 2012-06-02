package st.rhapsody.youtube4kids;

import android.content.res.Configuration;
import android.os.Bundle;

import com.keyes.youtube.OpenYouTubePlayerActivity;

public class YoutubePlayerActivity extends OpenYouTubePlayerActivity {

	private Youtube4KidsApplication app;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		app = (Youtube4KidsApplication) getApplication();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		app.setVideoPlaying(false);
		finish();
	}
}
