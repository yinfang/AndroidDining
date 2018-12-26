package com.clubank.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.clubank.common.R;
import com.clubank.device.BaseActivity;
import com.clubank.domain.MapPoint;
import com.clubank.map.MyOnGetRoutePlanResultListener;

public class MapUtil {

	public static void zoomTo(BaiduMap baiduMap, float zoom) {
		MapStatusUpdate s1 = MapStatusUpdateFactory.zoomTo(zoom);
		baiduMap.setMapStatus(s1);// set zoom
	}

	public static void center(BaiduMap baiduMap, LatLng pt) {
		MapStatusUpdate s2 = MapStatusUpdateFactory.newLatLng(pt);
		baiduMap.animateMapStatus(s2);// set center

	}

	public static Marker drawPoint(BaiduMap baiduMap, MapPoint point) {
		int iconMark = point.iconMark;
		if (iconMark == 0) {
			iconMark = R.drawable.icon_marka;
		}
		BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(iconMark);
		baiduMap.setMyLocationEnabled(true);
		LatLng ll = new LatLng(point.latitude, point.longitude);
		OverlayOptions ooA = new MarkerOptions().position(ll).icon(bdA)
				.title(point.name).zIndex(9).draggable(true);
		Marker marker = (Marker) baiduMap.addOverlay(ooA);// draw point
		bdA.recycle();
		return marker;
	}

	public static void initSearch(BaseActivity a, BaiduMap baiduMap,
			RoutePlanSearch search) {
		// 初始化搜索模块，注册事件监听
		MyOnGetRoutePlanResultListener listener = new MyOnGetRoutePlanResultListener(
				a, baiduMap);
		search.setOnGetRoutePlanResultListener(listener);

	}

	public static void drivingSearch(RoutePlanSearch search, LatLng pt1,
			LatLng pt2) {
		PlanNode stNode = PlanNode.withLocation(pt1);
		PlanNode enNode = PlanNode.withLocation(pt2);
		search.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
				enNode));
	}

	public static void center(BaiduMap baiduMap, LatLngBounds.Builder builder) {
		LatLngBounds bounds = builder.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
		baiduMap.animateMapStatus(u);
	}
	public static void navi(NaviParaOption para, final BaseActivity a) {
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, a);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(a);
			builder.setMessage(R.string.install_baidumap);
			builder.setTitle(R.string.prompt);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					/**
					 * OpenClientUtil:调起百度客户端工具类
					 *
					 * public static int getBaiduMapVersion(Context context)
					 * 获取百度地图客户端版本号
					 * 返回0代表没有安装百度地图客户端
					 * */
					OpenClientUtil.getLatestBaiduMapApp(a);
				}
			});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

	}

	@SuppressLint("InflateParams")
	public static void showInfoWindow(final BaseActivity a, BaiduMap baiduMap,
			LatLng ll, String name, final int index) {
		/* 名称框的位置上偏移量，可在自己项目的values里覆盖此integer 变量以适应自己的水滴图标icon_mark */
		int offset = a.getResources().getInteger(
				R.integer.map_popup_text_offset);
		LayoutInflater inflater = LayoutInflater.from(a);
		View v = inflater.inflate(R.layout.map_popup, null);
		UI.setEText(v, R.id.popup, name);

		OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				/* 执行引用Activity的方法以便回调地图节点事件 */
				a.onMapItemClick(index);
			}
		};
		InfoWindow infoWindow = new InfoWindow(
				BitmapDescriptorFactory.fromView(v), ll, offset, listener);
		baiduMap.showInfoWindow(infoWindow);
	}
}
