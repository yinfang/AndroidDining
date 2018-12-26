package com.clubank.device;

import android.annotation.SuppressLint;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.clubank.common.R;
import com.clubank.domain.ContactInfo;
import com.clubank.tabpage.TabPageIndicator;
import com.clubank.tabpage.UnderlinePageIndicator;

@SuppressLint("Registered")
public class BaseTabActivity extends BaseActivity implements
		ViewPager.OnPageChangeListener {
	protected BaseFragment[] fragments; // 子类中赋值
	protected String[] titles;
	protected int currentPage;

	private FragmentStatePagerAdapter mAdapter;
	private ViewPager mPager;
	private BaseFragment f;

	@Override
	protected void onStart() {
		if (mPager == null) {
			initPagerView();
		}
		f = (BaseFragment) mAdapter.getItem(mPager.getCurrentItem());
		super.onStart();
	}

	private void initPagerView() {

		mAdapter = new MyFragmentAdapter(this, fragments, titles);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(4);

		mPager.setAdapter(mAdapter);

		TabPageIndicator mIndicator = (TabPageIndicator) findViewById(getIndicator());
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(this);
		mIndicator.setCurrentItem(currentPage);

		UnderlinePageIndicator mIndicatorLine = (UnderlinePageIndicator) findViewById(getIndicatorLine());
		mIndicatorLine.setFades(false);
		mIndicatorLine.setViewPager(mPager);
		mIndicatorLine.setOnPageChangeListener(mIndicator);
		mIndicatorLine.setCurrentItem(currentPage);
	}

	protected int getIndicator() {
		return 0;
	}

	protected int getIndicatorLine() {
		return 0;
	}

	@Override
	public void dateSet(View view, int year, int month, int day) {
		f.dateSet(view, year, month, day);
	}

	@Override
	public void timeSet(View view, int hour, int minute) {
		f.timeSet(view, hour, minute);
	}

	@Override
	public void listSelected(View view, int index) {
		super.listSelected(view, index);
		f.listSelected(view, index);
	}

	@Override
	public void mlistSelected(View view, boolean[] b) {
		f.mlistSelected(view, b);
	}

	/**
	 * 对话框虚方法
	 * 
	 * @param type
	 * @param tag
	 */
	@Override
	public void processDialogOK(int type, Object tag) {
		f.processDialogOK(type, tag);
	}

	/**
	 * 刷新数据虚方法
	 */
	@Override
	public void refreshData() {
		f.refreshData();
	}

	@Override
	public void selectedContact(ContactInfo contact) {
		f.selectedContact(contact);
	}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

}
