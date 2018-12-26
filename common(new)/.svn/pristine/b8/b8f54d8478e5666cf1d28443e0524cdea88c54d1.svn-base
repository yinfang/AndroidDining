package com.clubank.device;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ImagePagerAdapter extends PagerAdapter {

	private List<View> views;
	Context context;

	public ImagePagerAdapter(Context context, List<View> views) {
		this.views = views;
		this.context = context;
	}

	@Override
	public void destroyItem(View collection, int position, Object arg2) {
		((ViewPager) collection).removeView(views.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {

		View view = views.get(position);

		((ViewPager) collection).addView(view);

		return view;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
