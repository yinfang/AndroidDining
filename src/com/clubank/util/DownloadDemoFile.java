package com.clubank.util;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.clubank.device.BaseActivity;
import com.clubank.dining.R;
import com.clubank.domain.Result;

//usually, subclasses of AsyncTask are declared inside the activity class. 
// that way, you can easily modify the UI thread from here 
public class DownloadDemoFile extends AsyncTask<String, Integer, Result> {

	ProgressDialog pdialog;
	BaseActivity a;
	String filename;
	Class<?> op;

	public DownloadDemoFile(BaseActivity a, Class<?> op, String filename) {
		this.a = a;
		this.filename = filename;
		this.op = op;
	}

	@Override
	protected Result doInBackground(String... sUrl) {
		try {
			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a typical 0-100%
			// progress bar
			int fileLength = connection.getContentLength();
			// download the file
			InputStream input = new BufferedInputStream(url.openStream());

			File f = new File(a.getExternalFilesDir(null), filename);

			OutputStream output = new FileOutputStream(f);
			byte data[] = new byte[1024];
			long total = 0;
			int count;
			int last_percent = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				int percent = (int) (total * 100.00 / fileLength);
				if (percent > last_percent) {
					publishProgress(percent);
				}
				last_percent = percent;
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();

			Result result = new Result();

			result.obj = f.getAbsolutePath();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Result result) {
		a.onPostExecute(op, result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pdialog = new ProgressDialog(a);
		// dialog.setTitle("Indeterminate");
		pdialog.setMessage(a.getText(R.string.msg_please_wait));
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		pdialog.setIndeterminate(false);
		pdialog.setMax(100);
		pdialog.incrementProgressBy(1);
		pdialog.setCancelable(true);
		pdialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		if (progress[0] >= 100) {
			pdialog.dismiss();
		}
		pdialog.setProgress(progress[0]);
	}
}
