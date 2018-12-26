package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class FlavorAdapter extends BaseAdapter {

	public FlavorAdapter(BaseActivity a, int item_layout, MyData data) {
		super(a, R.layout.flavoritem, data);
		ischecked = new boolean[data.size()];
	}

	@Override
	protected void display(final int position, View v, MyRow row) {
		if (row != null) {
			TextView tv = (TextView) v.findViewById(R.id.textview);
			tv.setText(row.getString("Name"));
			CheckBox box = (CheckBox) v.findViewById(R.id.checkbox);
			box.setChecked(ischecked[position]);
//			v.setTag(ischecked[position]);
//			v.setTag(box);
			box.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox thisbox = (CheckBox) v;
					if(thisbox.isChecked()){
						thisbox.setChecked(true);
						ischecked[position] = true;
					}else{
						thisbox.setChecked(false);
						ischecked[position] = false;
					}
					notifyDataSetChanged();
				}
			});
		}
	}
	
	public boolean[] ischecked;
	public void dataChange(MyData data){
		ischecked = new boolean[data.size()];
		notifyDataSetChanged();
	}
}
