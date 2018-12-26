/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clubank.device;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.clubank.common.R;
import com.clubank.domain.ImageDispProp;
import com.clubank.util.ImageUtil;
import com.clubank.util.MyData;
import com.clubank.util.U;

import java.util.ArrayList;
import java.util.List;

public class ImageLargeActivity extends BaseActivity implements
		OnClickListener, OnLongClickListener {

	private List<View> views = new ArrayList<View>();
	private MyData data;
	private ImageDispProp prop;

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.image_pager);
		setEText(R.id.header_title, getIntent().getStringExtra("title"));

		Bundle b = getIntent().getExtras();
		data = (MyData) U.getData(b, "data");
		int pos = b.getInt("pos");
		prop = (ImageDispProp) b.getSerializable("prop");
		setEText(R.id.total, "" + data.size());
		// Set up ViewPager and backing adapter
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setPageMargin((int) getResources().getDimension(
				R.dimen.image_detail_pager_margin));
		pager.setOnPageChangeListener(new MyPageChangeListener());
		pager.setOffscreenPageLimit(2);

		// Set up activity to go full screen
		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		for (int i = 0; i < data.size(); i++) {
			// View v = getLayoutInflater().inflate(R.layout.ad_banner, null);
			ImageView v = new ImageView(this);
			v.setScaleType(ScaleType.FIT_CENTER);
			views.add(v);
		}
		ImagePagerAdapter adapter = new ImagePagerAdapter(this, views);
		pager.setAdapter(adapter);
		pager.setCurrentItem(pos);
		if (data.size() > 0) {
			showPage(pos);
		}

	}

	class MyPageChangeListener extends SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int pos) {
			super.onPageSelected(pos);
			showPage(pos);
		}
	}

	void showPage(int pos) {
		ImageView iv = (ImageView) views.get(pos);
		iv.setTag(pos);

		if (data.size() > 0) {
			String large = prop.baseImageUrl
					+ data.get(pos).getString(prop.largePicCol);
			String small = prop.baseImageUrl
					+ data.get(pos).getString(prop.smallPicCol);
			setImage(iv, large);
			if(prop.captionCols != null){
				String caption = U.getFormatted(prop.captionFormat,
						prop.captionCols, data.get(pos));
				setEText(R.id.name, caption);
			}
		}
		setEText(R.id.pos, "" + (pos + 1));

		iv.setOnLongClickListener(this);
		iv.setOnClickListener(this);
	}

	public boolean onLongClick(View v) {
		showListDialog(v, new String[] { getString(R.string.save_picture) });
		return false;
	}

	public void onClick(View v) {
		if (prop.closeOnClickImage) {
			finish();
		}
	}

	public void listSelected(View v, int index) {
		int pos = (Integer) v.getTag();
		String large = prop.baseImageUrl
				+ data.get(pos).getString(prop.largePicCol);
		int i = large.lastIndexOf("/");
		int j = large.lastIndexOf(".");
		String fname = large.substring(i + 1, j);
		Bitmap bmp = getBitmapFromDiskCache(large);
		ImageUtil.saveImage(this, bmp, getPackageLastSection(), fname);
	}

	private String getPackageLastSection() {
		String name = "my_images";
		try {
			String pname = getPackageName();
			int i = pname.lastIndexOf('.');
			name = pname.substring(i + 1);
		} catch (Exception e) {

		}
		return name;
	}
}
