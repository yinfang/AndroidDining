package com.clubank.map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.clubank.device.BaseActivity;
import com.clubank.domain.MapPoint;

public class MyOnGetGeoCoderResultListener implements
		OnGetGeoCoderResultListener {

	private BaseActivity a;

	public MyOnGetGeoCoderResultListener(BaseActivity a) {
		this.a = a;
	}

	public void onGetGeoCodeResult(GeoCodeResult result) {
	}

	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
			LatLng ll = result.getLocation();
			MapPoint p = new MapPoint(ll.latitude, ll.longitude);
			p.name = result.getAddress();
			a.openMap(p);
		}
	}
}
