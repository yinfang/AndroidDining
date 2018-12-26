package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;
import com.clubank.device.BaseActivity;
import com.clubank.device.BaseAdapter;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class Bill2Adapter extends BaseAdapter {

	public Bill2Adapter(BaseActivity a, MyData data) {
		super(a, R.layout.lvi_bill, data);
	}

	@Override
	protected void display(int position, View v, MyRow row) {
		super.display(position, v, row);
		setEText(v, R.id.cl_tableno, row.getString("TableName"));
		setEText(v, R.id.cl_billcode, row.getString("BillCode"));
		setEText(v, R.id.cl_card_no, row.getString("CardNo"));
		setEText(v, R.id.cl_total_payable, BC.nf_a.format(row.getDouble("TotalPayable")));
		setEText(v, R.id.cl_total_guest, row.getString("TotalGuest"));
		setEText(v, R.id.cl_bill_date, row.getString("OpenTime").substring(0, 16));
		setEText(v, R.id.cl_billing_by, row.getString("BillingBy"));
		setEText(v, R.id.cl_bill_status, row.getString("TableName"));
		setEText(v, R.id.cl_open_user, row.getString("TableName"));
		setEText(v, R.id.bill_sign, row.getString("DiningRemarks"));
		
		if(row.getInt("IsDishComment")==1){
			show(R.id.commented);		   
		}else{
			hide(R.id.commented);	
		}
		int resId = R.string.lbl_bill_status_saved;
		int color = getContext().getResources().getColor(R.color.poi);
		View b = v.findViewById(R.id.buttonPrint);
		b.setTag(row.getString("BillCode"));
		if (row.getInt("BillStatus") == 0) {// saved
			b.setVisibility(View.VISIBLE);
		} else if (row.getInt("BillStatus") == 1) {// charged
			resId = R.string.lbl_bill_status_charged;
			color = getContext().getResources().getColor(R.color.blue3);
			b.setVisibility(View.VISIBLE);
		} else if (row.getInt("BillStatus") == 2) {// paid
			resId = R.string.lbl_bill_status_paid;
			color = getContext().getResources().getColor(R.color.green5);
			b.setVisibility(View.VISIBLE);
		}
		TextView t9 = (TextView) v.findViewById(R.id.cl_bill_status);
		t9.setText(getContext().getText(resId));
		t9.setTextColor(color);
	}
}
