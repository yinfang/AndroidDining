package com.clubank.device;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * Created by long on 17-5-2.
 * 入厨方式的适配器
 */
@SuppressLint("ViewConstructor")
public class SelectCookieTypeAdapter extends BaseAdapter{
    public SelectCookieTypeAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.lvi_cookie_type, data);
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);

        TextView cookieType = (TextView) v.findViewById(R.id.tv_cookie_type_name);
        if (row != null) {
           if (!TextUtils.isEmpty((String)row.get("Name"))){
               cookieType.setText((String)row.get("Name"));
           }

        }
    }
}
