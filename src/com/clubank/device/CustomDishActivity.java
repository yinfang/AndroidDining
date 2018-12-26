package com.clubank.device;

import com.clubank.device.BaseActivity;
import com.clubank.dining.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//此文件已废弃，
public class CustomDishActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dish);
	}   

	public void ok(View view) {

		String name = getEText(R.id.itemName);
		String price = getEText(R.id.price);
		String qty = getEText(R.id.qty);
		if ("".equals(name) || "".equals(price) || "".equals("qty")) {
			return;
		}
		double d_price = 0;
		double d_qty = 0;
		try {
			d_price = Double.valueOf(price);
			d_qty = Double.valueOf(qty);
		} catch (Exception e) {

		}
		Intent intent = new Intent();
		intent.putExtra("itemName", name);
		intent.putExtra("price", d_price);
		intent.putExtra("qty", d_qty);
		setResult(RESULT_OK, intent);
		finish();
	}

}
