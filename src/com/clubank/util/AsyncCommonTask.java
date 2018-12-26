package com.clubank.util;

import android.os.AsyncTask;
import android.util.Log;

import com.clubank.device.BaseActivity;
import com.clubank.device.MainActivity;
import com.clubank.domain.BC;

import com.clubank.domain.Result;

import java.io.File;


/**
 * 解压缩 dining.zip 文件
 */
public class AsyncCommonTask extends AsyncTask<Object, Result, Result> {

	BaseActivity a;
	MyWaitingDialog wd;
	int id;
	Class<?> op;
	public boolean showWaiting = true;
	BizDBHelper helper ;

	public AsyncCommonTask(BaseActivity a, Class<?> op) {
		this.a = a;
		this.op = op;
	}

	public AsyncCommonTask(BaseActivity a, Class<?> op, boolean showWaiting) {
		this(a, op);
		this.showWaiting = showWaiting;
	}

	public void onPreExecute() {
		if (showWaiting) {
			wd = new MyWaitingDialog(a);
			wd.show();
		}
	}

	@Override
	public Result doInBackground(Object... args) {
		Result result = new Result();
		try {
            MainActivity  main=(MainActivity)a;
			main.doWork(args);

			/*U.unzip((String) args[0], (String) args[1]);
			MyData areas = new MyData();
			if (S.shopCode != null) {
			    helper= new BizDBHelper(a, BC.DB_NAME, null, 1);
			    DB db = new DB(helper);
				String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
				areas = db.getData( sql, new String[] { S.shopCode });
			}*/
			// publishProgress((int) ((count / (float) length) * 100));
			/*result.obj = areas;*/
		} catch (Exception e) {
			result.code = BC.RESULT_UNKNOWN_ERROR;
			result.obj = e.getCause().getMessage();

			Log.e(e.getMessage(), "");
			e.printStackTrace();
			e.getMessage();
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Result result) {
		try {
			if (showWaiting) {
				wd.dismiss();
			}
			if (showWaiting) {
			}
			a.onPostExecute(op, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onProgressUpdate(Result... values) {
		//
		// System.out.println("" + values[0]);
		// message.setText(""+values[0]);
		// pdialog.setProgress(values[0]);
	}
}