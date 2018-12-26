package com.clubank.device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.util.UI;

/**
 * @author jishu0416 点菜基类 extends BasebizActivity
 * 
 *         加载tab选项栏位，响应事件
 */
@SuppressLint({ "DefaultLocale", "CutPasteId" })
public class BaseActionActivity extends BasebizActivity {

	protected String[] actions;
	protected boolean hideIcon;
	/** keep this activity in history,default false */
	// protected boolean keepInHistory;
	private LayoutInflater inflater;

	// private Bundle actionBundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// actionBundle = getIntent().getExtras();
		inflater = LayoutInflater.from(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String name = getClass().getSimpleName();// 获得当前类名
		if (actions.length > 0) {
			for (String action : actions) {
				if (name.startsWith(action)) {
					loadAction(action);
					break;
				}
			}
		}
	}

	private void loadAction(String active) {
		if (screen == null || actions == null) {
			return;
		}
		LinearLayout v = (LinearLayout) findViewById(R.id.actionbar);
		v.removeAllViews();
		int n = actions.length;
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				View view = getAction(actions[i], active);
				v.addView(view);
				if (i < actions.length - 1) {
					View iv = inflater.inflate(R.layout.divider, null);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT);
					lp.width = UI.getPixel(this, 1);
					// lp.topMargin = UI.getPixel(this, 10);
					// lp.bottomMargin = UI.getPixel(this, 10);
					iv.setLayoutParams(lp);
					v.addView(iv);
				}
			}
		}

	}

	/**
	 * 
	 * @param action
	 *            某个Action
	 * @param active
	 *            选中的Action
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("DefaultLocale")
	private View getAction(String action, String active) {
		FrameLayout view = null;
		int iconId = 0;

		CharSequence name = UI.getText(this, "action_" + action);
		CharSequence ttl = UI.getText(this, "title_" + action);
		if (ttl == null) {
			ttl = name.toString();
		}
		view = (FrameLayout) inflater.inflate(R.layout.action_inactive, null);
		iconId = UI.getId(this, "tabi_" + action.toLowerCase(), "drawable");

		if (action.equals(active)) {// tab 按下时图片不同，显示为按下效果
			view = (FrameLayout) inflater.inflate(R.layout.action_active, null);
			iconId = UI.getId(this, "taba_" + action.toLowerCase(), "drawable");
			setHeaderTitle(ttl);// 设置界面title
		}
		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		if (iconId > 0) {
			iv.setImageResource(iconId);
		}
		if (hideIcon) {
			View icon = view.findViewById(R.id.icon);
			icon.setVisibility(View.GONE);
		}
		TextView tv = (TextView) view.findViewById(R.id.name);
		tv.setText(name);
		view.setTag(action);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		lp.width = screen.getWidth() / actions.length;
		view.setLayoutParams(lp);

		return view;
	}

	public void goAction(View view) {
		String action = (String) view.getTag();
		try {
			String pName = getClass().getPackage().getName();
			Class<?> clazz = Class.forName(pName + "." + action + "Activity");
			Intent intent = new Intent(this, clazz);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// if (!keepInHistory) {
			// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			// }
			intent.putExtra("action", action);
			// if (actionBundle != null) {
			// intent.putExtras(actionBundle);
			// }
			if (action.equals("OrderOrdered")) {
				intent.putExtra("from", "quickOpen");
				startActivity(intent);
				finish();
			} else {
				startActivity(intent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
