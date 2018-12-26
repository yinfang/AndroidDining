package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
 * 快速开台——姓名选择按钮事件
 *
 * @param view
 */
public class CheckinList extends BasebizActivity implements OnItemClickListener {
    private CheckinCriteria criteria = new CheckinCriteria();
    private CheckinAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_list);
        setData();
        ListView listview = (ListView) findViewById(R.id.checkin_list);
        adapter = new CheckinAdapter(this, R.layout.item_checkin, new MyData());
        listview.setOnItemClickListener(this);
        initList(listview, adapter, new Criteria(), QueryCheckinInfo.class);
        Bundle b = getIntent().getBundleExtra("bundle");
        if (b != null) {
            MyData data = U.getData(b, "data");
            if (data.size() > 0) {
                for (MyRow myRow : data) {
                    adapter.add(myRow);
                }
            }

        }
    }

    public void setData() {
        Intent intent = getIntent();
        setEText(R.id.name, intent.getStringExtra("name"));
        setEText(R.id.memno, intent.getStringExtra("memno"));
        setEText(R.id.header_title, getString(R.string.lbl_mem_query));
        refreshData();
    }

    @Override
    public void refreshData() {
        criteria.MemNo = getEText(R.id.memno);
        criteria.Name = getEText(R.id.name);
        if (!criteria.MemNo.equals("") || !criteria.Name.equals("")) {
//            if (!BC.engineerMode){
            new MyAsyncTask(this, QueryCheckinInfo.class).execute(criteria);
        }
//        }
    }

    public void queryCheckin(View view) {
//        if (!BC.engineerMode) {
        refreshData(true, QueryCheckinInfo.class);
//        }
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
        intent.putExtra("name", myRow.getString("GuestName"));
        intent.putExtra("memno", myRow.getString("MemNo"));
        intent.putExtra("cardno", myRow.getString("CardNo"));
        setResult(RESULT_OK, intent);
        finish();
    }

}
