package com.clubank.device;

import java.util.TimerTask;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.op.GetDiningPrinter;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

public class QueryPrinterTask extends TimerTask {
	private Service service;
	private NotificationManager nm;
	private GetDiningPrinter ms;
	private SharedPreferences sp;

	public QueryPrinterTask(Service service) {
		this.service = service;
		this.nm = (NotificationManager) service
				.getSystemService(Context.NOTIFICATION_SERVICE);
		this.ms = new GetDiningPrinter(service);
		this.sp = service.getSharedPreferences(C.APP_ID, Service.MODE_PRIVATE);
	}

	public void run() {
		try {
			Result result = ms.execute();
			MyData list = (MyData) result.obj;
			for (int i = 0; i < list.size(); i++) {
				MyRow row = list.get(i);
				int status = Integer.valueOf((String) row.get("Status"));
				String ip = (String) row.get("IP");
				if (status != 0 && ip != null && !ip.equals("")) {
					showPrinterError(row);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void showPrinterError(MyRow p) {
		Notification n = new Notification();
		n.icon = R.drawable.printererror;
		String content = String.format(
				UI.getText(service, "printer_error_content").toString(),
				p.get("Name"));
		n.tickerText = content;//
		n.defaults = Notification.DEFAULT_LIGHTS;

		if (sp.getBoolean("printer_alert_sound", true)) {
			n.defaults |= Notification.DEFAULT_SOUND;
		}
		if (sp.getBoolean("printer_alert_vibrate", true)) {
			n.defaults |= Notification.DEFAULT_VIBRATE;
		}

		n.flags = Notification.FLAG_ONLY_ALERT_ONCE
				| Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(service, PrinterActivity.class);
		PendingIntent pendIntent = PendingIntent.getActivity(service, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(service,
				UI.getText(service, "printer_error_title"), content, pendIntent);
		nm.notify(BC.NOTIFY_PRINTER_ERROR, n);// ִ��֪ͨ.
	}
}
