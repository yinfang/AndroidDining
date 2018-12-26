package com.clubank.device;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.clubank.domain.ListObject;

public class MyOnScrollListener implements OnScrollListener {
	private BaseActivity a;
	private ListObject lo;
	private static int preLast;

	public MyOnScrollListener(BaseActivity a, ListObject lo) {
		this.a = a;
		this.lo = lo;
	}

	public void onScroll(AbsListView lw, final int firstVisibleItem,
			final int visibleItemCount, final int totalItemCount) {

		final int lastItem = firstVisibleItem + visibleItemCount;
		if (lastItem == totalItemCount) {
			if (preLast != lastItem) {
				// to avoid multiple calls for last item
				/* 调用BaseActivity的moreData方法加载更多数据，列表子类需要覆写refreshData */
				a.moreData(lo);
				preLast = lastItem;
			}
		}

		a.specificOnScroll(lw, firstVisibleItem, visibleItemCount,
				totalItemCount);
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
}
