package com.clubank.device;

import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * @author jishu0416
 *	沽清菜界面
 */
public class SoldOutActivity extends BasebizActivity {
	DishAdapter da;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soldout);
		ListView listView = (ListView) findViewById(R.id.listView1);
		da = new DishAdapter(this, new MyData());
		listView.setAdapter(da);
		refreshData();
		findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
		menus = new String[] { "menu_refresh" };
	}

	@Override
	public void menuSelected(View view, int index) {
		if (index == 0) {// refresh
			refreshData();
		}
	}

	public void refreshData() {
		String sql = "select code,name,price,hprice,candiscount,soldout from t_dish where soldout='true'";
		MyData dishes = db.getData( sql);
		TextView t = (TextView) findViewById(R.id.dish_count);
		t.setText("" + dishes.size());
		da.clear();
		for (MyRow row : dishes) {
			da.add(row);
		}
		da.notifyDataSetChanged();
	}
}
