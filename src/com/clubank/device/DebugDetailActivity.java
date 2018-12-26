package com.clubank.device;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.clubank.dining.R;

public class DebugDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_detail);
        findViewById(R.id.header_menu_scan).setVisibility(View.GONE);
//        ((TextView) findViewById(R.id.header_title)).setText("测试");
        Bundle bundle = getIntent().getExtras();
        String content = bundle.getString("content");
//        TextView textView = (TextView) findViewById(R.id.debug_detail_response);
//        textView.setText(content);
        if (content.startsWith("<?xml version=\"1.0\" encoding")){
            setEColor(R.id.debug_detail_response,R.color.green);
        }else {
            setEColor(R.id.debug_detail_response,R.color.red);
        }
        setEText(R.id.debug_detail_response,content);
    }
}
