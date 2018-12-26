package com.clubank.device;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {

	private String[] mTitle;
	private BaseFragment[] mFragment;

	public MyFragmentAdapter(BaseActivity activity, BaseFragment[] data,
			String[] titles) {
		super(activity.getSupportFragmentManager());
		mTitle = titles;
		mFragment = data;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragment[position];
	}

	@Override
	public int getCount() {
		return mFragment.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitle[position];
	}

}
