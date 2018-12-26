package com.clubank.device;

import java.util.Timer;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * 打印机服务 （默认每个三分钟检测打印机是否正常，可设置一分钟检测）
 * @author jishu0416
 *
 */
public class NotifyService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			public void run() {
				Timer t = new Timer();
				t.schedule(new QueryPrinterTask(NotifyService.this), 3000,
						30000);
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
