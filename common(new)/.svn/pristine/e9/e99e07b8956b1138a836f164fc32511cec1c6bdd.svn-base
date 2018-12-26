package com.clubank.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.common.R;
import com.clubank.domain.C;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 轮播广告图片基础类,子类覆盖2个方法 GetImages, initImage, goSpecificModule
 * 
 * @author chenyh
 * 
 */
@SuppressLint("Registered")
public class ImageBannerActivity extends BaseActivity {
	private MyData pics = new MyData();
	private List<View> views = new ArrayList<View>();
	private ImagePagerAdapter adapter;
	private ViewPager pager;
	private LinearLayout dots;
	private Timer timer;
	private boolean stop;
	private LayoutInflater inflater;
	private String baseImageUrl = "";

	protected String picColName = "PicUrl";
	protected String goUrlColName = "GoUrl";
	protected String descColName = "Description";

	@SuppressLint("InflateParams")
	@Override
	public void onStart() {
		super.onStart();
		inflater = LayoutInflater.from(this);
		dots = (LinearLayout) findViewById(R.id.dots);
		if (dots == null) {
			return;
		}
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOnPageChangeListener(new MyPageChangeListener());

		if (pics.size() > 0) {
			// 已经装载过了，第二次进入
			return;
		}
		getImages();

		View v = inflater.inflate(R.layout.ad_banner, null);
		initImage(v);
		views.add(v);
		adapter = new ImagePagerAdapter(this, views);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
		showPage(0);

		timer = new Timer();
		timer.schedule(new UpdateTimeTask(), 3000, 5000);
		pager.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View view, MotionEvent e) {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					stop = true;
				} else if (e.getAction() == MotionEvent.ACTION_UP) {
					stop = false;
				}
				return false;
			}
		});
	}

	/**
	 * 子类覆盖此方法，获取远程图片URL
	 */
	protected void getImages() {
		// 子类中调用远程方法
		// new MyAsyncTask(this, GetAdPicture.class).run();
	}

	/**
	 * 初始化图片
	 * 
	 * @param v
	 *            ViewPager第一帧
	 */
	protected void initImage(View v) {
	}

	@Override
	public void onResume() {
		super.onResume();
		stop = false;
	}

	class UpdateTimeTask extends TimerTask {
		public void run() {
			if (stop || views.size() <= 1) {
				return;
			}
			int pos = pager.getCurrentItem();
			int next = pos + 1;
			if (pos == views.size() - 1) {
				next = 0;
			}
			Message message = new Message();
			message.what = 1;
			message.obj = next;
			handler.sendMessage(message);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int pos = (Integer) msg.obj;
				pager.setCurrentItem(pos, false);// 第二个参数false表示不显示拖动的动画效果
				showPage(pos);
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 显示图片,
	 * 
	 * @param pics
	 *            图片数据，包含 PicUrl, Description, GoUrl 等属性
	 */
	protected void displayImages(MyData pics) {
		displayImages(pics, C.baseImageUrl);
	}

	/**
	 * 添加一个参数，有一些图片不需要前缀如 http://www.baidu.com/1.jpg
	 * 类似于这样的不需要添加C.baseImageUrl的图片地址
	 * 
	 * @author zgh
	 * @date 2014-11-11下午4:39:58
	 * @param pics
	 * @param baseImageUrl
	 */
	@SuppressLint("InflateParams")
	protected void displayImages(MyData pics, String baseImageUrl) {
		if (pics.size() == 0) {
			return;
		}
		baseImageUrl = baseImageUrl == null ? "" : baseImageUrl;
		this.pics = pics;
		views.clear();
		this.baseImageUrl = baseImageUrl;
		for (int i = 0; i < pics.size(); i++) {
			View v = inflater.inflate(R.layout.ad_banner, null);
			ImageView iv = (ImageView) v.findViewById(R.id.adImage);
			iv.setOnClickListener(new MyOnClickListener(i));
			setImage(iv, baseImageUrl + pics.get(i).getString(picColName));
			views.add(v);
			View dot = inflater.inflate(R.layout.dot, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
			lp.setMargins(10, 0, 0, 0);
			dots.addView(dot, lp);
		}
		adapter = new ImagePagerAdapter(this, views);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
		showPage(0);
	}

	private void showPage(int pos) {
		if (pics.size() == 0) {
			return;
		}
		View v = views.get(pos);
		ImageView iv = (ImageView) v.findViewById(R.id.adImage);
		setImage(iv, baseImageUrl + pics.get(pos).getString(picColName));
		for (int i = 0; i < dots.getChildCount(); i++) {
			View dot = dots.getChildAt(i);
			int color = R.color.gray_cd;
			if (i == pos) {
				color = R.color.red;
			}
			dot.setBackgroundColor(getResources().getColor(color));
		}
		TextView tvname = (TextView) v.findViewById(R.id.adName);
		tvname.setText(pics.get(pos).getString(descColName));
		stop = false;
	}

	/**
	 * Click an ad picture
	 * 
	 * @param pos
	 */
	private void clickPic(int pos) {
		if (pics.size() == 0) {
			return;
		}
		String url = pics.get(pos).getString("GoUrl");
		if (url != null && !url.isEmpty() && url.startsWith("http://")) {
			/* 如果有链接就跳到网页链接 */
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			startActivity(intent);
		} else {
			/* 跳至广告商家APP页 */
			goSpecificModule(pics.get(pos));
		}
	}

	/**
	 * 点击广告图需要跳转到APP内的某个功能模块, 子类需要覆盖此方法
	 * 
	 * @param row
	 *            广告图数据
	 */
	protected void goSpecificModule(MyRow row) {
	}

	public class MyPageChangeListener extends SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			super.onPageSelected(position);
			showPage(position);
		}

	}

	public class MyOnClickListener implements OnClickListener {
		private int pos = 0;

		public MyOnClickListener(int pos) {
			this.pos = pos;
		}

		public void onClick(View v) {
			clickPic(pos);
		}
	}

}
