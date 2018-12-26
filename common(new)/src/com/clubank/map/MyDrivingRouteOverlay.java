package com.clubank.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.clubank.common.R;

public class MyDrivingRouteOverlay extends DrivingRouteOverlay {
	private boolean useDefaultIcon;
	
	public MyDrivingRouteOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	@Override
	public BitmapDescriptor getStartMarker() {
		if (useDefaultIcon) {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}
		return null;
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
		if (useDefaultIcon) {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
		return null;
	}

}
