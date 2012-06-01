package st.rhapsody.youtube4kids;

import android.content.res.Configuration;
import android.os.Bundle;

import com.keyes.youtube.OpenYouTubePlayerActivity;

public class YoutubePlayerActivity extends OpenYouTubePlayerActivity {

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
