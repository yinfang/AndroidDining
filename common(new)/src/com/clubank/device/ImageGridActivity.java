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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import com.clubank.common.R;
import com.clubank.util.MyData;
import com.clubank.util.U;

public class ImageGridActivity extends BaseActivity implements
		AdapterView.OnItemClickListener {
	private ImageGridAdapter adapter;

	public ImageGridActivity() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid);
		
		final int thumbSpacing = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_spacing);

		Bundle b = getIntent().getExtras();
		MyData data = (MyData) U.getData(b, "data");
		adapter = new ImageGridAdapter(this, data, b);
		final GridView gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		gridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						final int columnWidth = (gridView.getWidth() / gridView
								.getNumColumns()) - thumbSpacing;
						adapter.setItemHeight(columnWidth);

					}
				});

	}

	// @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		final Intent i = new Intent(this, ImageLargeActivity.class);
		i.putExtras(getIntent().getExtras());
		i.putExtra("pos", (int) id);
		startActivity(i);
	}
}
