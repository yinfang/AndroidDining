package com.clubank.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.clubank.dining.R;
import com.clubank.domain.SerializableMyData;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author jishu0416 口味码选
 */
public class SelectMa extends BaseActivity {
	private MyData cookingFlavors;
	private FlavorAdapter allAdapter;
	private FlavorAdapter hasAdapter;
	private ListView allList;
	private ListView hasList;
	private MyData allData;
	private MyData hasData;
	private String flavor = "";
	private String flavorCode = "";
	private MyData flavorsTrans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_ma);
		SerializableMyData data = (SerializableMyData) getIntent()
				.getBundleExtra("flavorBundle").getSerializable(
						"cookingFlavors");
		cookingFlavors = db.getData("select code,name from T_CookingFlavor");
//		cookingFlavors = data.getData();
//		SerializableMyData data2 = (SerializableMyData) getIntent()
//				.getBundleExtra("flavorBundle").getSerializable("flavorsTrans");
		MyData data2 = new MyData();
		if(null != getIntent().getStringExtra("codeAndName") ){
			String codeAndName = getIntent().getStringExtra("codeAndName");
			if(!TextUtils.isEmpty(codeAndName)){
				String[] ss = codeAndName.split("-");
				for(int i = 0;i<ss.length;i++){
					MyRow row = new MyRow();
					row.put("Code", ss[i].split(",")[0]);
					row.put("Name", ss[i].split(",")[1]);
					data2.add(row);
				}
			}
		}
		if (data2 != null &&data2.size() != 0) {
			flavorsTrans = data2;
		}
		initView();
	}

	private void initView() {
		findViewById(R.id.wuyong).setFocusable(true);
		findViewById(R.id.wuyong).setFocusableInTouchMode(true);
		allList = (ListView) findViewById(R.id.list);
		hasList = (ListView) findViewById(R.id.havechooselist);
		if (flavorsTrans != null) {
			hasData = flavorsTrans;
		} else {
			hasData = new MyData();
		}
		allData = new MyData();
		allAdapter = new FlavorAdapter(this, R.layout.flavoritem, allData);
		hasAdapter = new FlavorAdapter(this, R.layout.flavoritem, hasData);
		allList.setAdapter(allAdapter);
		hasList.setAdapter(hasAdapter);
		EditText et = (EditText) findViewById(R.id.edittext);
		et.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean onfocus) {
				if (onfocus) {
					EditText thiset = (EditText) v;
					thiset.setText("");
				}
			}
		});
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				selectData();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable text) {
				selectData();
			}
		});

	}

	/**
	 * edittext内容变化时，list列表数据变化
	 */
	private void selectData() {
		if (getET().replaceAll(" ", "").equals("")) {
			return;
		}
		allList = (ListView) findViewById(R.id.list);
		allData.clear();
		String thiscode = "";
		String thisname = "";
		if (isNumeric(getET())) { // 纯数字则判断code
			for (int i = 0; i < cookingFlavors.size(); i++) {
				thiscode = cookingFlavors.get(i).getString("Code");
				if (thiscode.contains(getET())) {
					allData.add(cookingFlavors.get(i));
				}
			}
		} else { // 文字判断
			for (int i = 0; i < cookingFlavors.size(); i++) {
				thisname = cookingFlavors.get(i).getString("Name");
				if (thisname.contains(getET())) {
					allData.add(cookingFlavors.get(i));
				}
			}
		}
		allAdapter.dataChange(allData);
	}

	private String getET() {
		return getEText(R.id.edittext);
	}

	/**
	 * 查询Button
	 * 
	 * @param view
	 */
	public void select(View view) {
		allData.removeAll(allData);
		if (getET().replace(" ", "").equals("")) {
			return;
		}
		allList = (ListView) findViewById(R.id.list);
		selectData();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		findViewById(R.id.wuyong).requestFocus();
	}

	/**
	 * 添加
	 * 
	 * @param view
	 */
	public void add(View view) {
		for (int i = 0; i < allAdapter.ischecked.length; i++) {
			if (allAdapter.ischecked[i]) {
				if(hasData.contains(allData.get(i))){
			
				}else{
					hasData.add(allData.get(i));
				}
				
			}
		}
		hasAdapter.dataChange(hasData);
	}

	/**
	 * 删除
	 * 
	 * @param view
	 */
	public void delete(View view) {
		View v;
		int hasDelete = 0;
		for (int i = 0; i < hasList.getChildCount(); i++) {
			v = hasList.getChildAt(i);
			CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkbox);
			if (checkbox.isChecked()) {
				hasData.remove(i - hasDelete);
				checkbox.setChecked(false);
				hasDelete++;
			}
		}
		hasAdapter.dataChange(hasData);
	}

	public void commit(View view) {
		// 添加code
		for (int j = 0; j < hasData.size(); j++) {
			flavorCode = flavorCode.equals("") ? hasData.get(j).getString(
					"Code") : flavorCode + ","
					+ hasData.get(j).getString("Code");
			flavor = flavor.equals("") ? hasData.get(j).getString("Name")
					: flavor + "," + hasData.get(j).getString("Name");
		}
		Intent data = new Intent();
		data.putExtra("flavor", flavor);
		data.putExtra("flavorCode", flavorCode);
		setResult(54321, data);
		finish();
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
