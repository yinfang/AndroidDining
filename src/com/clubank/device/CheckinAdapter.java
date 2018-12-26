package com.clubank.device;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.clubank.alipay.Base64;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class CheckinAdapter extends BaseAdapter {
    public CheckinAdapter(BaseActivity context, int resId, MyData data) {
        super(context, R.layout.item_checkin, data);
    }

    @Override
    protected void display(int position, View v, MyRow o) {
        super.display(position, v, o);
        if (o != null) {
            if (!TextUtils.isEmpty(o.getString("MemPicture"))) {
                byte[] b = Base64.decode(o.getString("MemPicture"));
                setImage(v, R.id.img, b, true);
            }
            setEText(v, R.id.name, o.getString("GuestName"));
            setEText(v, R.id.memno, o.getString("MemNo"));
            setEText(v, R.id.cardNo, o.getString("CardNo"));
            setEText(v, R.id.cardState, o.getString("StatusName"));
            setEText(v, R.id.acountLeft, "￥" + BC.nf_a.format(o.getDouble("Tot")));
        }
    }
}
