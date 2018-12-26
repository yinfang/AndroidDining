package com.clubank.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UPPayUtils {

	public static final int STARTE_UPPAY = 1; // 调用UPPay请求码
	public static final String PAY_RESULT = "pay_result"; // 支付结果bundle中key值
	public static final String PACKAGE_NAME = "com.unionpay.uppay";
	public static final String ACTIVITY_NAME = "com.unionpay.uppay.PayActivity";

	public static final String PAY_RESULT_BROADCAST = "com.unionpay.uppay.payResult";// broadcast
																						// id
	public static final String KEY_PAY_RESULT = "PayResult"; // broadcast key

	// pay result
	public static final String TAG_SUCCESS = "success";
	public static final String TAG_FAIL = "fail";
	public static final String TAG_CANCEL = "cancel";

	public static final String APK_FILE_NAME = "UPPayPlugin.apk";

	public static void showAlertDlg(Context context, CharSequence title,
			CharSequence msg, CharSequence okButton,
			DialogInterface.OnClickListener clickedOk,
			CharSequence cancelButton,
			DialogInterface.OnClickListener clickedCancel) {
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		if (title != null && title.length() > 0) {
			builer.setTitle(title);
		}
		if (msg != null && msg.length() > 0) {
			builer.setMessage(msg);
		}

		if (okButton != null && clickedOk != null) {
			builer.setPositiveButton(okButton, clickedOk);
		}

		if (cancelButton != null && clickedCancel != null) {
			builer.setNegativeButton(cancelButton, clickedCancel);
		}

		AlertDialog adlg = builer.create();
		adlg.show();
	}

}
