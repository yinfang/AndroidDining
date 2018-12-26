package com.clubank.device;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.UI;

/**
 * 转台
 * 
 * @author jishu0416
 *
 */
public class ChangeTableActivity extends BasebizActivity {
	private String newCode;
	private String[] tableNames;
	private MyData tables;
	private String oldName;
	private String newName;
	private final int DLG_CONFIRM_CHANGE_TABLE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_table);

		Bundle b = getIntent().getExtras();
		String oldCode = b.getString("oldCode");
		oldName = b.getString("oldName");
		setEText(R.id.oldName, oldName);
		setEText(R.id.billName, b.getString("billName"));
		String sql = "select * from t_diningtable where areacode=(select areacode from t_diningtable where code=?) and code!=?";
		tables = db.getData(sql, new String[] { oldCode, oldCode });
		tableNames = new String[tables.size()];
		int v = tables.size();
		if (v > 0) {
			for (int i = 0; i < v; i++) {
				tableNames[i] = tables.get(i).getString("Name");
			}
		}

		/*
		 * if (tables.size() > 0) { newCode = tables.get(0).getString("Code");
		 * newName = tableNames[0]; setEText(R.id.newName, newName); }
		 */

	}

	public void selectTable(View view) {
		showListDialog(view, tableNames);
	}

	@Override
	public void listSelected(View view, int index) {
		int id = view.getId();
		if (id == R.id.newName) {
			newCode = tables.get(index).getString("Code");
			newName = tables.get(index).getString("Name");
			setEText(R.id.newName, newName);
		}
	}

	public void ok(View v) {
		if (newCode == null) {
			UI.showInfo(this, R.string.changetable);
			return;
		}
		String msg = getText(R.string.lbl_change_table_confirm).toString();
		msg = String.format(msg, oldName, newName);
		UI.showOKCancel(this, DLG_CONFIRM_CHANGE_TABLE, msg, getText(R.string.lbl_confirm), null);
	}

	@Override
	public void processDialogOK(int type, Object tag) {
		super.processDialogOK(type, tag);
		if (type == DLG_CONFIRM_CHANGE_TABLE) {
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("newTableNo", newCode);
			b.putString("billName", getEText(R.id.billName));
			intent.putExtras(b);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
