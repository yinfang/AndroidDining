package com.clubank.device;

import android.content.Context;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.clubank.device.op.UpdateLocation;
import com.clubank.domain.C;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.U;

public class MyLocationListener implements BDLocationListener {

	private static long lastTimestamp;
	private Context context;

	public MyLocationListener(Context context) {
		this.context = context;
	}

	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		C.location = location;
		long timestamp = System.currentTimeMillis();
		if (lastTimestamp == 0
				|| timestamp - lastTimestamp > C.UPDATE_LOCATION_INTERVAL) {
			lastTimestamp = timestamp;
			Bundle b = U.getMeta(context);
			if (b != null && b.getBoolean("UpdateLocation") != false) {
				String udid = U.getUdid(context);
				new MyAsyncTask(context, UpdateLocation.class, false).run(udid,
						location.getLongitude(), location.getLatitude());
			}
		}
	}

	public void onReceivePoi(BDLocation poiLocation) {
	}
}
