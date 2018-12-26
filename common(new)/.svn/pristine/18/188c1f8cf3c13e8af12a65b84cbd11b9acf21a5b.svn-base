package com.clubank.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.clubank.common.R;
import com.clubank.domain.C;
import com.clubank.domain.DeviceInfo;
import com.clubank.util.U;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zgh
 * @date 2014-12-22
 * @time 下午4:20:27
 * @content
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			String notifyClass = "com.clubank.device.NotifyActivity";
			try {
				String c = U.getMeta(context).getString("notifyClass");
				if (c != null) {
					notifyClass = c;
				}

				Class<?> clazz = Class.forName(notifyClass);

				Intent i = new Intent(context.getApplicationContext(), clazz);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("title", context.getString(R.string.message));
				// 打开自定义的Activity
				context.getApplicationContext().startActivity(i);
				// 用于统计
				JPushInterface.reportNotificationOpened(context,
						bundle.getString(JPushInterface.EXTRA_MSG_ID));

			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				Log.e("", "Click notification failed", e);
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
			// 保存RegistrationId 方便后续操作
			if (connected && C.udid != null) {
				if (U.getMeta(context).getBoolean("SignDevice", true)) {
					DeviceInfo info = new DeviceInfo();
					info.DeviceModel = Build.MODEL;
					info.OSVersion = Build.VERSION.RELEASE;
					info.RegistrationId = JPushInterface
							.getRegistrationID(context);
					info.Udid = C.udid;
					info.Version = U.getVersion(context);

				}
			}

		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
}
