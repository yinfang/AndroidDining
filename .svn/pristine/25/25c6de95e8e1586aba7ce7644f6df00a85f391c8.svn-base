package com.clubank.device;

import java.io.File;


import java.io.FilenameFilter;
import java.util.Date;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.clubank.device.BaseActivity;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.util.MyWaitingDialog;

public class AboutActivity extends BaseActivity {
	private String fname = "Dining";
	private DownloadManager downloadManager;
	private long downloadId;
	private MyReceiver receiver;
	private IntentFilter filter;
	private MyWaitingDialog wd;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		String version = "1.0";
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = info.versionName;
			setEText(R.id.version, version);
		} catch (NameNotFoundException e) {

		}

		String c = getEText(R.id.copyright_en);
		setEText(R.id.copyright_en, String.format(c, new Date().getYear() + 1900));
		filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		receiver = new MyReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, filter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@SuppressWarnings("deprecation")
	public void downloadLatest(View view) {
		wd = new MyWaitingDialog(this);
		wd.show();
		File file = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		file.mkdir();
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		String url = BC.BASE_URL_INTERNET + fname + ".apk";
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		request.setShowRunningNotification(true);
		request.setVisibleInDownloadsUi(true);
		String actualName = getFileName(fname, ".apk");
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, actualName);
		downloadId = downloadManager.enqueue(request);
	}

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			long downId = b.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (downId != downloadId) {
				return;
			}
			// String action = intent.getAction();
			DownloadManager.Query q = new DownloadManager.Query();
			q.setFilterById(downId);
			Cursor c = downloadManager.query(q);
			if (wd != null) {
				wd.dismiss();
			}
			if (c.moveToFirst()) {
				int status = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
				if (status == DownloadManager.STATUS_SUCCESSFUL) {
					// process download
					String uri = c.getString(c
							.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setDataAndType(Uri.parse(uri),
							"application/vnd.android.package-archive");
					context.startActivity(i);
				}
			}
			/*
			 * if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) { //
			 * unregisterReceiver(receiver); startActivity(new
			 * Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)); }
			 */
		}
	};

	private String getFileName(String fileName, String extension) {
		File file = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String[] files = file.list(new MyFileFilter(fname, extension));
		String fname = fileName;
		if (files.length > 0) {
			fname = fileName + "-" + files.length;
		}
		return fname + extension;
	}

	public class MyFileFilter implements FilenameFilter {
		private String prefix;
		private String extension;

		public MyFileFilter(String prefix, String extension) {
			this.prefix = prefix;
			this.extension = extension;
		}

		public boolean accept(File file, String path) {
			return path.startsWith(prefix) && path.endsWith(extension);
		}
	}

}
