package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubank.common.R;

/*
 * 一种传统的Tab实现框架，跟用Fragment实现的tab页的区别在于：
 * fragment实现的tab页，不用切换整体页面，显得更平滑，但是代码更复杂，并且由于Fragment和Activity属于两种不同的对象，
 * 需要处理这二层之间的数据交互，代码具有较强的耦合性，不利于扩展维护。
 * 用这种传统的方式实现，每个Tab页界面仍然是一个独立的Activity可以共享BaseActivity的诸多功能，代码结构简单，
 * 便于扩展维护，缺点是整体页面切换，执行效率比Fragment略低。
 */

/**
 * 传统的Tab页
 * 
 * @author chenyh
 * 
 */
public abstract class ActionActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 指定的Tab实现类，必须在子类中定义
	 */
	protected Class<?>[] tabs;
	/**
	 * Tab页的文字，必须在子类中定义
	 */
	protected int[] captions;
	/**
	 * Tab页的 title，在子类中定义，可选
	 */
	protected int[] titles;
	/**
	 * Tab页在非高亮状态的图标，在子类中定义，可选。如果定义的话，必须同时也定义activeIcons，且数量必须与tabs一致
	 */
	protected int[] inactiveIcons;
	/**
	 * Tab页在高亮状态的图标，在子类中定义，可选。如果定义的话，必须同时也定义inactiveIcons，且数量必须与tabs一致
	 */
	protected int[] activeIcons;
	/**
	 * Tab页在高亮状态的layout，在子类中定义，可选,但一般都会要在项目中定义
	 */
	protected int activeLayout;
	/**
	 * Tab页在非高亮状态的layout，在子类中定义，可选,但一般都会要在项目中定义
	 */
	protected int inactiveLayout;

	private LayoutInflater inflater;
	/**
	 * 当前Tab 的index
	 */
	private int active;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		/* some default properties, you can re-define them in sub class */
		activeLayout = R.layout.action_active;
		inactiveLayout = R.layout.action_inactive;
	}

	public void onResume() {
		super.onResume();
		if (tabs == null) {
			throw new RuntimeException("Action activities undefined!");
		}
		if (captions == null) {
			throw new RuntimeException("Action captions undefined!");
		}
		Bundle b = getIntent().getExtras();
		if (b != null) {
			active = b.getInt("active", 0);
		}
		if (titles == null) {
			titles = captions;
		}
		for (int i = 0; i < tabs.length; i++) {
			loadAction(i);
		}
	}

	private void loadAction(int active) {
		if (tabs == null) {
			return;
		}
		ViewGroup v = (ViewGroup) findViewById(R.id.actionbar);
		v.removeAllViews();
		for (int i = 0; i < tabs.length; i++) {
			View view = getAction(i);
			v.addView(view);
			if (i > 0) {
				// View divider = inflater.inflate(R.layout.divider, null);
				// ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				// LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				// lp.leftMargin = 1;
				// v.addView(divider);
			}
		}
	}

	/**
	 * 
	 * @param i
	 *            第 i 个Tab
	 * 
	 * @return
	 */
	private View getAction(int i) {
		ViewGroup view = null;
		int iconId = 0;
		int layout = 0;
		if (i == active) {
			setHeaderTitle(getText(titles[i]));
			layout = activeLayout;
		} else {
			layout = inactiveLayout;
		}
		view = (ViewGroup) inflater.inflate(layout, null);
		if (view.getId() != R.id._tab) {
			throw new RuntimeException("ID not defined in tab layout!");
		}
		if (i != active) {
			view.setOnClickListener(this);
		}

		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		if (activeIcons == null) {
			iv.setVisibility(View.GONE);
		} else {
			if (i == active) {
				iconId = activeIcons[i];
			} else {
				iconId = inactiveIcons[i];
			}
			iv.setImageResource(iconId);
		}
		TextView tv = (TextView) view.findViewById(R.id.name);
		tv.setText(captions[i]);
		view.setTag(i);

		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.width = dm.widthPixels / tabs.length;
		view.setLayoutParams(lp);

		return view;
	}

	public void onClick(View view) {
		if (view.getId() != R.id._tab) {
			/* action_active.xml, action_inactive.xml中顶级元素必须定义ID为_tab */
			return;
		}
		int active = (Integer) view.getTag();
		try {
			Intent intent = new Intent(this, tabs[active]);
			/* 屏蔽切换activity时系统的缺省动画，这样可以消除屏幕闪烁 */

			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("active", active);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
