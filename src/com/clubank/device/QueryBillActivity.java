package com.clubank.device;

import java.util.Date;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.S;
import com.clubank.util.MyData;
import com.clubank.util.U;

/**
 * 账单查询
 *
 */
public class QueryBillActivity extends BasebizActivity {

	private  final int DATE_DIALOG_ID = 1;
	private int pos2;
	private MyData tables;
	private MyData areas;
	private String[] areaNames = new String[0];
	private String[] tableNames = new String[0];

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_bill);

		setEText(R.id.dateDisplay, BC.df_yMd.format(new Date()));
		String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
		areas = db.getData(sql, new String[] { S.shopCode });

		areaNames = new String[areas.size()];
		int size=areas.size();
		for (int i = 0; i <size; i++) {
			areaNames[i] = (String) areas.get(i).get("AreaName");
		}
		if (size== 0) {
			Button b = (Button) findViewById(R.id.button2);
			b.setEnabled(false);
		}
		if (size> 0) {
			setEText(R.id.areaName,
					(String) areas.get(S.areaPosition).get("AreaName"));
			loadTable();
		}

	}
/**
 * 区域
 * @param view
 */
	public void selectArea(View view) {
		showListDialog(view, areaNames);
	}
/**
 * 桌台
 * @param view
 */
	public void selectTable(View view) {
		showListDialog(view, tableNames);
	}

	@Override
	public void listSelected(View view, int index) {
		int id = view.getId();
		if (id == R.id.areaName) {
			S.areaPosition = index;
			setEText(R.id.areaName, areaNames[index]);
			setEText(R.id.TableName, getString(R.string.lbl_all));
			pos2 = 0;
			loadTable();
		} else if (id == R.id.TableName) {
			pos2 = index;
			setEText(R.id.TableName, tableNames[index]);
		}
	}
/**
 * 确定 查询
 * @param view
 */
	public void queryBill(View view) {
		CheckBox c = (CheckBox) findViewById(R.id.chkMyself);
		String date = getEText(R.id.dateDisplay);
		String areaCode = areas.get(S.areaPosition).getString("AreaCode");
		String areaName = areas.get(S.areaPosition).getString("AreaName");
		String queryStr=getEText(R.id.queryStr);
		Intent intent = new Intent(this, BillListActivity.class);
		intent.putExtra("areaCode", areaCode);
		intent.putExtra("areaName", areaName);
		intent.putExtra("isMyself", c.isChecked());
		intent.putExtra("date", date);
		intent.putExtra("queryStr", queryStr);
		String condition = date + "  " + areaName;
		if (pos2 > 0) {
			intent.putExtra("tableName", tables.get(pos2 - 1).getString("Name"));
			intent.putExtra("tableCode", tables.get(pos2 - 1).getString("Code"));
			condition += "(" + tables.get(pos2 - 1).get("Name") + ")";
		}
		intent.putExtra("condition", condition);
		intent.putExtra("title", getText(R.string.lbl_bill_list));
		startActivity(intent);
	}

	public void loadTable() {
		String areaCode = (String) areas.get(S.areaPosition).get("AreaCode");
		// new MyAsyncTask(this, Action.GET_PENDING_BILL).execute(areaCode,
		// C.df_yMd.format(new Date()));

		String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
		tables = db.getData(sql, new String[] { areaCode });

		tableNames = new String[tables.size() + 1];
		tableNames[0] = getString(R.string.msg_all);
		int  tablesSize= tables.size();
		for (int i = 0; i <tablesSize; i++) {
			tableNames[i + 1] = (String) tables.get(i).get("Name");
		}
	}
/**
 * 日期
 * @param view
 */
	public void selectDate(View view) {
		showDateDialog(view, getEText(R.id.dateDisplay));
	}

	
	
	@Override
	public void dateSet(View view, int year, int month, int day) {
		String date = U.getDateString(year, month, day);
		setEText(R.id.dateDisplay, date);
	}
}
