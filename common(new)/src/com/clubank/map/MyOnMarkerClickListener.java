package com.clubank.map;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.Marker;
import com.clubank.device.BaseActivity;
import com.clubank.util.MapUtil;

public class MyOnMarkerClickListener implements OnMarkerClickListener {
	private BaseActivity a;
	private BaiduMap baiduMap;

	public MyOnMarkerClickListener(BaseActivity a, BaiduMap baiduMap) {
		this.a = a;
		this.baiduMap = baiduMap;
	}

	@SuppressLint("InflateParams")
	public boolean onMarkerClick(final Marker marker) {
		String name = marker.getTitle();
		Bundle b1 = marker.getExtraInfo();
		int index = 0;
		if (b1 != null) {
			index = b1.getInt("index");
		}
		MapUtil.showInfoWindow(a, baiduMap, marker.getPosition(), name, index);
		return true;
	}
}
