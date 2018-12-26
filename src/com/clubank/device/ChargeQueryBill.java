package com.clubank.device;

import com.clubank.dining.R;
import com.clubank.util.MyData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 账单查询
 * 
 * @author jishu0416
 *
 */
public class ChargeQueryBill extends BasebizActivity {
	private int tablePosition = -1;
	private String[] tableNames = new String[0];
	private MyData tables;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_querybill);
		setEText(R.id.header_title, getString(R.string.menu_query_bill));
		loadTable();

	}

	private void loadTable() {
		String areaCode = getIntent().getStringExtra("areaCode");
		String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
		tables = db.getData(sql, new String[] { areaCode });
		tableNames = new String[tables.size() + 1];
		tableNames[0] = getString(R.string.lbl_not_selected);
		int c = tables.size();
		if (c > 0) {
			for (int i = 0; i < c; i++) {
				tableNames[i + 1] = (String) tables.get(i).get("Name");
			}
		}
		if (tablePosition >= 0) {
			setEText(R.id.TableName, tables.get(tablePosition).getString("Name"));
		}
	}

	public void selectTable(View view) {
		showListDialog(view, tableNames);
	}

	@Override
	public void listSelected(View view, int index) {
		int id = view.getId();
		if (id == R.id.TableName) {
			tablePosition = index - 1;
			setEText(R.id.TableName, tableNames[index]);

		}

	}

	public void query(View view) {
		Intent intent = new Intent();
		if (tablePosition != -1) {
			intent.putExtra("TableNo", tables.get(tablePosition).getString("Code"));
		} else {
			intent.putExtra("TableNo", "");
		}
		intent.putExtra("CardNo", getEText(R.id.qo_card_no) + "");
		intent.putExtra("DiningRemarks", getEText(R.id.lbl_ordersign) + "");
		setResult(RESULT_OK, intent);
		finish();
	}
}
