package com.clubank.util;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

import com.clubank.common.R;
import com.clubank.device.BaseActivity;
import com.clubank.device.DownloadActivity;
import com.clubank.device.op.GetUpPayAmountString;
import com.clubank.device.op.GetUpPayString;
import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.unionpay.UPPayAssistEx;

public class InitPayTask extends AsyncTask<Object, Void, String> {

	public static final String PAY_DATA = "PayData";
	public static final String SP_ID = "SpId";
	public static final String SECURITY_CHIP_TYPE = "SecurityChipType";
	public static final String SYS_PROVIDE = "SysProvide";
	public static final String USE_TEST_MODE = "UseTestMode";
	protected String tn;
	protected BaseActivity a;
	private AlertDialog dialog;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;
	private String mMode = "00"; // 接入模式 00为生产模式，正式版用 01为测试版
	private String url = "http://app.golfbaba.com/app/android/UPPayPluginEx.apk";

	@SuppressLint("UseSparseArrays")
	protected Map<Integer, Integer> msgs = new HashMap<Integer, Integer>();

	public InitPayTask(BaseActivity activity) {
		super();
		this.a = activity;
	}

	@Override
	protected void onPreExecute() {
		dialog = new AlertDialog(a) {
		};
		dialog.setTitle(R.string.prompt);
		dialog.setMessage(a.getText(R.string.get_order_info));
		dialog.setCancelable(false);
		dialog.show();
		msgs.put(RT.OPERATION_FAILED, R.string.operation_failed);
		msgs.put(RT.ILLEGAL_ACCESS, R.string.illegal_access);
		msgs.put(RT.UNKNOWN_ERROR, R.string.unexpected_error);

	}

	@Override
	protected String doInBackground(Object... args) {
		try {
			Result result = new Result();
			boolean bizOrder = (Boolean) args[0];
			if (bizOrder) {// with a business order
				result = new GetUpPayString(a).execute(args);
			} else if (args[1] instanceof String) {// orderNo
				// just pay simple amt, Order No generated starts with char 'B'
				result = new GetUpPayAmountString(a).execute(args);
			}
			if (result.obj != null) {
				tn = (String) result.obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return null;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(String errMsg) {
		super.onPostExecute(errMsg);
		if (errMsg != null) {
			if (errMsg.length() > 118) {
				errMsg = errMsg.substring(0, 118) + "...";
			}
			dialog.dismiss();
			dialog = new AlertDialog(a) {
			};
			dialog.setTitle(R.string.error);
			dialog.setMessage(errMsg);
			dialog.setButton(a.getText(android.R.string.ok),
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.show();
			dialog.setCancelable(false);
		} else {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			// String spId = a.settings.getString("spId", "4899");
			// String sysProvider = a.settings
			// .getString("sysProvider", "48025840");
			// int ret = UPPayAssistEx.startPay(a, spId, sysProvider, tn,
			// mMode);
			int ret = UPPayAssistEx.startPay(a, null, null, tn, mMode);
			if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
				DialogInterface.OnClickListener clickedOk = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dialog instanceof AlertDialog) {
							// UPPayAssistEx.installUPPayPlugin(a);
							if (a instanceof DownloadActivity) {
								((DownloadActivity) a).downloadFile(url);
							} else {
								U.runAssetsApk(a, "UPPayPluginEx.apk");
							}
						}
					}
				};

				DialogInterface.OnClickListener clickedCancel = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dialog instanceof AlertDialog) {
							// activity.finish();
						}
					}
				};
				CharSequence msg = a.getText(R.string.unionpay_plugin_required);
				UPPayUtils.showAlertDlg(a, null, msg,
						a.getText(android.R.string.ok), clickedOk,
						a.getText(android.R.string.cancel), clickedCancel);
			}
		}
	}

}
