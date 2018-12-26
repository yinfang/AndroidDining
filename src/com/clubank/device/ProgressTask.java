package com.clubank.device;

import com.clubank.dining.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ProgressTask extends AsyncTask<String, Integer, String> {
private 	ProgressDialog pdialog;
 Context context;
	int title;

	public ProgressTask(Context context) {
		this.context = context;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		preWork();
		pdialog = new ProgressDialog(context);
		// dialog.setTitle("Indeterminate");
		if (title == 0) {
			title = R.string.msg_please_wait;
		}
		pdialog.setMessage(context.getText(title));
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		pdialog.setIndeterminate(false);
		pdialog.setMax(size());
		pdialog.incrementProgressBy(1);
		pdialog.setCancelable(false);
		pdialog.show();
	}

	protected int size() {
		return 0;
	}

	protected void preWork() {
	}

	protected void doWork() {

	}

	protected void afterWork() {
		pdialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		afterWork();
	}

	@Override
	protected String doInBackground(String... args) {
		doWork();
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		pdialog.setProgress(progress[0]);
	}

}
