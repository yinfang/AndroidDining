package com.clubank.util;

import com.clubank.device.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AlarmHelper {

	private Context c;
	private AlarmManager am;
	private Uri data;

	public AlarmHelper(Context c) {
		this.c = c;
		am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
		data = Uri.parse("content://calendar/calendar_alerts/1");
	}

	/**
	 * 设置闹钟
	 * 
	 * @param id
	 *            闹钟标识，必须是唯一的
	 * @param bundle
	 *            闹铃时界面显示的字符串数据，包含 datetime, description
	 * @param time
	 *            设置闹钟的时间
	 */
	public void setAlarm(int id, Bundle bundle, long time) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.putExtras(bundle);
		intent.setClass(c, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(c, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}

	public boolean existAlarm(int id) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(c, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(c, id, intent,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}

	public void cancelAlarm(int id) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(c, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(c, id, intent,
				PendingIntent.FLAG_NO_CREATE
						| PendingIntent.FLAG_CANCEL_CURRENT);
		if (pi != null) {
			am.cancel(pi);
		}
	}
}
