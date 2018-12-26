package com.clubank.device;

import android.view.View;
import android.widget.ImageView;

import com.clubank.common.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

public class MenuAdapter extends BaseAdapter {

	public MenuAdapter(BaseActivity a, MyData data) {
		super(a, R.layout.pop_item_light, data);
	}

	@Override
	protected void display(int position, View v, MyRow o) {
		ImageView iv = (ImageView) v.findViewById(R.id.image);
		int imageResId = o.getInt("imageResId");
		if (imageResId > 0) {
			iv.setImageResource(imageResId);
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.GONE);
		}
		setEText(v, R.id.name, o.getString("name"));
	}
}
