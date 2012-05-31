package st.rhapsody.youtube4kids;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class PlaylistThumbsTask extends AsyncTask<String, Void, Bitmap> {

	static HashMap<String,Bitmap> imageCache = new HashMap<String,Bitmap>();
	static HashMap<String,ImageView> viewCache = new HashMap<String,ImageView>();
	private final ImageView imageView;
	public PlaylistThumbsTask(ImageView iv) {
		this.imageView = iv;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String thumbUrl = params[0];
		if (imageCache.containsKey(thumbUrl)){
			System.out.println("cached");
			return imageCache.get(thumbUrl);
		}
		
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(thumbUrl);
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("loaded");
		imageCache.put(thumbUrl, bitmap);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {

		imageView.setImageBitmap(result);
		
	}

}
