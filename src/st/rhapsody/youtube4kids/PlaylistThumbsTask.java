package st.rhapsody.youtube4kids;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class PlaylistThumbsTask extends AsynctaskWithCallback<String, Void, Bitmap> {

	static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();
	static HashMap<String, ImageView> viewCache = new HashMap<String, ImageView>();
	private final Context applicationContext;

	public PlaylistThumbsTask(AsyncCallback<Bitmap> callback, Context applicationContext) {
		super(callback);
		this.applicationContext = applicationContext;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String thumbUrl = params[0];
		String id = getIdFromUrl(thumbUrl);
		File[] listFiles = applicationContext.getFilesDir().listFiles();
		Bitmap bitmap = null;
		boolean inFileCache = false;

		if (imageCache.containsKey(thumbUrl)) {
			System.out.println("memory cached");
			return imageCache.get(thumbUrl);
		}

		for (int i = 0; i < listFiles.length; i++) {
			File file = listFiles[i];
			if (file.getName().equals(id)) {
				try {
					FileInputStream openFileInput = applicationContext.openFileInput(file.getName());
					BufferedInputStream bis = new BufferedInputStream(openFileInput);
					bitmap = BitmapFactory.decodeStream(bis);
					System.out.println("file cached");
					inFileCache = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		if (bitmap == null) {

			URL url;

			try {
				url = new URL(thumbUrl);
				InputStream is = url.openConnection().getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);

				bitmap = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
				System.out.println("loaded from web");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		imageCache.put(thumbUrl, bitmap);

		if (!inFileCache) {
			try {
				FileOutputStream openFileOutput = applicationContext.openFileOutput(id, Context.MODE_PRIVATE);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, openFileOutput);
				openFileOutput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private String getIdFromUrl(String thumbUrl) {
		String[] split = thumbUrl.split("\\/");
		return split[4];
	}
}
