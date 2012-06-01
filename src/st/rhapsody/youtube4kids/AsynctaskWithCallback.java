package st.rhapsody.youtube4kids;

import android.os.AsyncTask;

public abstract class AsynctaskWithCallback<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private final AsyncCallback<Result> callback;

	public AsynctaskWithCallback(AsyncCallback<Result> result) {
		this.callback = result;
	}

	@Override
	protected void onPostExecute(Result result) {
		callback.call(result);
	}
}
