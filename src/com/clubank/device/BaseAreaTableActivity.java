package com.clubank.device;

import android.os.Bundle;

import android.view.View;

import com.clubank.dining.R;
import com.clubank.domain.S;
import com.clubank.util.MyData;

/**
 * 
 * @author jishu0416 选择区域，台号等
 */
public class BaseAreaTableActivity extends BasebizActivity {
	protected MyData areas;
	protected MyData tables;
	protected String[] areaNames;
	protected String[] areaCode;
	protected String[] tableNames;
	public String AreaCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void listSelected(View view, int index) {
		int id = view.getId();
		if (id == R.id.TableName) {
			S.bill.TableNo = tables.get(index).getString("Code");
			S.bill.TableName = tables.get(index).getString("Name");

			setEText(R.id.TableName, S.bill.TableName);
			int tablePosition = index;
			S.bill.TableNo = tables.get(tablePosition).getString("Code");

		} else if (id == R.id.AreaName) {
			S.areaPosition = index;
			loadTable();
		}
	}

	protected void init() {
		if (S.bill.TableName != null) {
			setEText(R.id.TableName, S.bill.TableName);
		} else {
			setEText(R.id.TableName, getString(R.string.lbl_not_selected));
		}
		String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
		areas = db.getData(sql, new String[] { S.shopCode });
		areaNames = new String[areas.size()];
		areaCode = new String[areas.size()];
		int n = areas.size();
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				areaNames[i] = (String) areas.get(i).get("AreaName");
				areaCode[i] = (String) areas.get(i).get("AreaCode");
			}
		}
		loadTable();
		setEText(R.id.AreaName, (String) areas.get(S.areaPosition).get("AreaName"));
		AreaCode = (String) areas.get(S.areaPosition).get("AreaCode");
	}

	// 区域
	public void selectArea(View view) {
		showListDialog(view, areaNames);
	}

	// 台号
	public void selectTable(View view) {
		showListDialog(view, tableNames);
	}

	protected void loadTable() {
		String areaCode = areas.get(S.areaPosition).getString("AreaCode");
		String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
		tables = db.getData(sql, new String[] { areaCode });
		tableNames = new String[tables.size()];
		int v = tables.size();
		if (v > 0) {
			for (int i = 0; i < v; i++) {
				tableNames[i] = (String) tables.get(i).get("Name");
			}
		}
	}

}
