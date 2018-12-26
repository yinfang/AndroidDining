package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class CheckinTwoAdapter extends BaseAdapter {
	public CheckinTwoAdapter(BaseActivity context, int resId, MyData data) {
		super(context, R.layout.item_checkin_two, data);
	}

	@Override
	protected void display(int position, View v, MyRow o) {
		super.display(position, v, o);
		if (o != null) {
			setEText(v, R.id.name, o.getString("GuestName"));
			setEText(v, R.id.memno, o.getString("MemNo"));
			setEText(v, R.id.cardNo, o.getString("CardNo"));		
		}
	}
}
