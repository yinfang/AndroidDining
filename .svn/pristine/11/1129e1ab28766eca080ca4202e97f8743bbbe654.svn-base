package com.clubank.device;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.S;
import com.clubank.util.MyData;
import com.clubank.util.U;

/**
 * 首页---> 消费记账
 * 
 * @author jishu0416
 *
 */
public class ChargeActivity extends BasebizActivity {
	private final int DATE_DIALOG_ID = 1;
	private MyData areas;
	String[] areaNames = new String[0];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge);

		setEText(R.id.dateDisplay, BC.df_yMd.format(new Date()));
		String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
		areas = db.getData(sql, new String[] { S.shopCode });
		areaNames = new String[areas.size()];
		int d = areas.size();
		if (d > 0) {
			for (int i = 0; i < d; i++) {
				areaNames[i] = (String) areas.get(i).get("AreaName");
			}
			setEText(R.id.areaName, (String) areas.get(S.areaPosition).get("AreaName"));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		findViewById(R.id.menu).setVisibility(View.GONE);
	}

	public void selectArea(View view) {
		showListDialog(view, areaNames);
	}

	@Override
	public void listSelected(View view, int index) {
		S.areaPosition = index;
		setEText(R.id.areaName, (String) areas.get(S.areaPosition).get("AreaName"));
	}

	public void submitQuery(View view) {
		String date = getEText(R.id.dateDisplay);
		String areaCode = areas.get(S.areaPosition).getString("AreaCode");
		String areaName = areas.get(S.areaPosition).getString("AreaName");
		Intent intent = new Intent(this, ChargeListActivity.class);
		intent.putExtra("areaCode", areaCode);
		intent.putExtra("areaName", areaName);
		intent.putExtra("date", date);
		String condition = date + "  " + areaName;
		intent.putExtra("condition", condition);
		intent.putExtra("title", getText(R.string.lbl_bill_list));
		intent.putExtra("from", "");
		startActivity(intent);
	}

	public void selectDate(View view) {
		showDateDialog(view, getEText(R.id.dateDisplay));
	}

	@Override
	public void dateSet(View view, int year, int month, int day) {
		String date = U.getDateString(year, month, day);
		setEText(R.id.dateDisplay, date);
	}
}
