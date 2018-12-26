package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.BC;

public class ItemDiscountActivity extends BasebizActivity {
	BillItemInfo bi;
	int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_discount);

		Bundle bundle = getIntent().getExtras();
		bi = (BillItemInfo) bundle.getSerializable("billItem");
		position = bundle.getInt("position");

		TextView itemName = (TextView) findViewById(R.id.discount_dish_item_name);
		itemName.setText(bi.ItemName);

		EditText saleTotal = (EditText) findViewById(R.id.di_sale_total);
		EditText discountRate = (EditText) findViewById(R.id.di_discount_rate);
		EditText fixedDiscount = (EditText) findViewById(R.id.di_fixed_discount);
		EditText totalPayable = (EditText) findViewById(R.id.di_total_payable);

		saleTotal.setText(BC.nf_a.format(bi.SaleTotal));
		discountRate.setText("" + bi.DiscountRate);
		fixedDiscount.setText(BC.nf_a.format(bi.FixedDiscount));
		totalPayable.setText(BC.nf_a.format(bi.TotalPayable));
		discountRate.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeDiscountRate(s.toString());
			}
		});
		fixedDiscount.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeFixedDiscount(s.toString());
			}
		});
	}

	public void changeDiscountRate(String s) {
		EditText totalPayable = (EditText) findViewById(R.id.di_total_payable);
		try {
			bi.DiscountRate = Double.parseDouble((String) s);
		} catch (Exception e) {
		}
		bi.RateDiscount = Math.round(bi.SaleTotal * (100 - bi.DiscountRate)) / 100;
		bi.TotalPayable = bi.SaleTotal - bi.RateDiscount - -bi.FixedDiscount;
		totalPayable.setText("" + bi.TotalPayable);
	}

	public void changeFixedDiscount(String s) {
		EditText totalPayable = (EditText) findViewById(R.id.di_total_payable);
		try {
			bi.FixedDiscount = Double.parseDouble((String) s);
		} catch (Exception e) {
		}
		bi.RateDiscount = Math.round(bi.SaleTotal * (100 - bi.DiscountRate)) / 100;
		bi.TotalPayable = bi.SaleTotal - bi.RateDiscount - -bi.FixedDiscount;
		totalPayable.setText("" + bi.TotalPayable);
	}

	public void submit(View view) {
		Intent intent = new Intent();
		Bundle b = new Bundle();

		b.putSerializable("billItem", bi);
		b.putInt("position", position);
		intent.putExtras(b);
		setResult(RESULT_OK, intent);
		finish();
	}
}
