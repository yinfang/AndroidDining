package com.clubank.device;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.SerializableMyData;
import com.clubank.util.JsonUtil;
import com.clubank.util.MyData;

/**
 * 入厨方式
 */
public class SelectCookieTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private MyData cookieTypeData;//入厨方式
    private SelectCookieTypeAdapter st;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cookie_type);
        TextView title =(TextView)findViewById(R.id.header_title);
        title.setText(R.string.lbl_select_cookie_type);
        findViewById(R.id.header_menu_scan).setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.lvw_cookie_select);
        String json = (String) getIntent().getStringExtra("cookie");

        cookieTypeData = JsonUtil.getMyData(json);
        st = new SelectCookieTypeAdapter(this,cookieTypeData);
        listView.setAdapter(st);

        AdapterView.OnItemLongClickListener ilc = new ItemLongClickListener();
        listView.setOnItemLongClickListener(ilc);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        i.putExtra("kitchenTypeName",(String) cookieTypeData.get(position).get("Name"));
        i.putExtra("kitchenTypeCode",(String) cookieTypeData.get(position).get("Code") );
        setResult(65432,i);
        finish();
    }

    class ItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            return false;
        }
    }
}
