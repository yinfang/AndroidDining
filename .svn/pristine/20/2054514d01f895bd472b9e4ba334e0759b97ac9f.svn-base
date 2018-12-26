package com.clubank.device;

import android.view.View;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

public class BreakFastTickerAdapter extends BaseAdapter {
    public BreakFastTickerAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.break_fast_ticket_item, data);
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        setEText(R.id.tv_house_no, row.getString("RoomCode"));
        setEText(R.id.tv_card_no, row.getString("CardNo"));
        setEText(R.id.tv_name, row.getString("GuestName"));
        setEText(R.id.tv_mobile, row.getString("Tel"));
        setEText(R.id.tv_sex, row.getString("Sex") );
        setEText(R.id.tv_all_ticket, row.getInt("CouponNumTotal") + "");
        setEText(R.id.tv_used_ticket, row.getInt("UsedCouponNum") + "");
        setEText(R.id.tv_unused_ticket, row.getInt("NotUsedCouponNum") + "");
        v.findViewById(R.id.btn_item_use).setTag(row);
        int canUse = row.getInt("NotUsedCouponNum");
        if (canUse > 0) {
            v.findViewById(R.id.btn_item_use).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.btn_item_use).setVisibility(View.GONE);
        }

    }
}
