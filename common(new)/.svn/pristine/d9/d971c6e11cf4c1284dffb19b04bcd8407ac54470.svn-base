package com.clubank.map;

import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.clubank.common.R;
import com.clubank.device.BaseActivity;

public class MyOnGetRoutePlanResultListener implements
		OnGetRoutePlanResultListener {
	private BaseActivity a;
	private BaiduMap mBaiduMap;

	public MyOnGetRoutePlanResultListener(BaseActivity a, BaiduMap baiduMap) {
		this.a = a;
		this.mBaiduMap = baiduMap;
	}

	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(a, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// int nodeIndex = -1;
			DrivingRouteLine route = result.getRouteLines().get(0);

			double distance = ((int) (route.getDistance() / 10)) / 100;
			int time = (int) (route.getDuration() / 60.0);
			a.setEText(R.id.distance, "" + distance);
			a.setEText(R.id.time, "" + time);
			a.setEText(R.id.search, R.string.navi);
			a.show(R.id.info);
			a.show(R.id.row_route);

			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			// OverlayManager routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();

		}
	}

	public void onGetTransitRouteResult(TransitRouteResult arg0) {

	}

	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

	}

}
