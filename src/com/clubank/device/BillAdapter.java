package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class BillAdapter extends BaseAdapter {

    public BillAdapter(BaseActivity context, int resId, MyData data) {
        super(context, R.layout.lvi_billtocharge, data);
    }

    @Override
    protected void display(int position, View v, MyRow o) {
        super.display(position, v, o);
        setEText(v, R.id.cl_tableno, o.getString("TableName"));
        setEText(v, R.id.cl_billcode, o.getString("BillCode"));
        setEText(v, R.id.cl_card_no, o.getString("CardNo"));
        setEText(v, R.id.cl_total_payable, BC.nf_a.format(o.getDouble("TotalPayable")));
        setEText(v, R.id.cl_guest_name, o.getString("GuestName"));
        setEText(v, R.id.cl_total_guest, o.getString("TotalGuest"));
        setEText(v, R.id.cl_bill_date, o.getString("OpenTime").substring(0, 16));
        setEText(v, R.id.cl_open_user, o.getString("OpenUser"));
        setEText(v, R.id.bill_sign, o.getString("DiningRemarks"));
        Button pay = (Button) v.findViewById(R.id.pay_bill);
        pay.setTag(position);
        if (o.getInt("BillStatus") == 0) {//未结账
            if (a.settings.getBoolean("jiezhang", false)) {//开启结账功能
                show(v, R.id.pay_bill);
            } else {
                hide(v, R.id.pay_bill);
            }
        } else {
            hide(v, R.id.pay_bill);
        }
    }
}