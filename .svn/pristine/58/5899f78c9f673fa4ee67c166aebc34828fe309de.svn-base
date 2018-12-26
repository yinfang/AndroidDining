package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.clubank.device.BaseActivity;
import com.clubank.device.BaseAdapter;
import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class CategoryAdapter extends BaseAdapter {

	public CategoryAdapter(BaseActivity context, int resId, MyData data) {
		super(context, R.layout.left_item, data);
	}

	@Override
	protected void display(int position, View v, MyRow o) {
		super.display(position, v, o);
		if (o != null) {
			TextView t1 = (TextView) v.findViewById(R.id.left_item);
			t1.setText(o.getString("Name"));
		}

		if (o.getBoolean("selected")) {
			v.setBackgroundColor(getContext().getResources().getColor(
					R.color.left_band_selected));
		} else {
			v.setBackgroundResource(R.drawable.lvi);
		}
	}
}