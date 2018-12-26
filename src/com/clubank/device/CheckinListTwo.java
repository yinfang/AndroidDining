package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.CheckinCriteria;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.op.QueryCheckinInfo;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;

/**
 * 记账-根据姓名查询消费卡号
 * Created by duxy on 2018/2/5.
 */

public class CheckinListTwo extends BasebizActivity implements AdapterView.OnItemClickListener {
    private CheckinCriteria criteria = new CheckinCriteria();
    private CheckinTwoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_list_two);

        hide(R.id.header_menu_scan);

        ListView listview = (ListView) findViewById(R.id.checkin_list);
        adapter = new CheckinTwoAdapter(this, R.layout.item_checkin_two, new MyData());
        listview.setOnItemClickListener(this);
        initList(listview, adapter, new Criteria(), QueryCheckinInfoByCard.class);

    }

    public void queryCheckin(View view) {
        refreshData(true, QueryCheckinInfoByCard.class);
    }

    @Override
    public void refreshData() {
        criteria.Name = getEText(R.id.name);
        if (!criteria.Name.equals("")) {
            new MyAsyncTask(this, QueryCheckinInfoByCard.class).execute(criteria);
        }

    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (result.code == BC.RESULT_SUCCESS) {
            MyData date = (MyData) result.obj;
            if (date.size() > 0) {
                for (MyRow row : date) {
                    adapter.add(row);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        MyRow myRow = adapter.getItem(arg2);
        Intent intent = new Intent();
//        intent.putExtra("name", myRow.getString("GuestName"));
//        intent.putExtra("memno", myRow.getString("MemNo"));
        intent.putExtra("cardno", myRow.getString("CardNo"));
        setResult(RESULT_OK, intent);
        finish();
    }

}
