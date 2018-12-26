package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.clubank.alipay.Base64;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.CheckinCriteria;
import com.clubank.domain.Result;
import com.clubank.op.GetMemberInfoByMemNoNew;
import com.clubank.util.ImageUtil;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 会员卡信息获取
 */
public class MemCardListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private MemCardInfoAdapter adapter;
    private String search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memcard_info_list);
        hide(R.id.header_menu_scan);
        setEText(R.id.header_title, getString(R.string.msg_card_info));
        ListView listview = (ListView) findViewById(R.id.list);
        adapter = new MemCardInfoAdapter(this, new MyData());
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
        search = getIntent().getStringExtra("MemNo");
        if (!TextUtils.isEmpty(search)) {
            setEText(R.id.memno, search);
            refreshData();
        }
    }

    public void queryCheckin(View v) {
        search = getEText(R.id.memno);
        if (TextUtils.isEmpty(search)) {
            UI.showToast(this, getString(R.string.book_search_hint));
            return;
        }
        refreshData();
    }

    @Override
    public void refreshData() {
        new MyAsyncTask(this, GetMemberInfoByMemNoNew.class).execute(search);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (result.code == BC.RESULT_SUCCESS) {
            MyData date = (MyData) result.obj;
            if(null!=adapter){
                adapter.clear();
            }
            if (date.size() > 0) {
                for (MyRow row : date) {
                    adapter.add(row);
                }
                adapter.notifyDataSetChanged();
            } else {
                show(R.id.empty_view);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyRow row = (MyRow) parent.getItemAtPosition(position);
        Intent data = new Intent();
        data.putExtra("name", row.getString("MemName"));
        data.putExtra("MemNo", row.getString("MemNo"));
        if(!TextUtils.isEmpty(row.getString("MemPicture"))){
            byte[] b = Base64.decode(row.getString("MemPicture"));
            data.putExtra("MemPicture", b  );
        }
//        data.putExtra("MemPicture", Base64.decode(row.get("MemPicture").toString()));
        setResult(RESULT_OK, data);
        finish();
    }
}

class MemCardInfoAdapter extends BaseAdapter {
    public MemCardInfoAdapter(BaseActivity context, MyData data) {
        super(context, R.layout.item_memcard, data);
    }

    @Override
    protected void display(int position, View v, MyRow o) {
        super.display(position, v, o);
        if (o != null) {
            if(!TextUtils.isEmpty(o.getString("MemPicture"))){
                byte[] b = Base64.decode(o.getString("MemPicture"));
                setImage(v, R.id.img, b, true);
            }
            setEText(v, R.id.name, o.getString("MemName"));
            setEText(v, R.id.memNo, o.getString("MemNo"));
            setEText(v, R.id.cardNo, o.getString("CardNo"));
            String statue = o.getString("StatusName");
            setEText(v, R.id.cardState, statue);
            setEText(v, R.id.acountLeft, "￥" + BC.nf_a.format(o.getDouble("Tot")));
        }
    }
}