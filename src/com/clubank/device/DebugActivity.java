package com.clubank.device;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.clubank.dining.R;
import com.clubank.util.FileUtil;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.WaitingDialog;

import java.io.File;


public class DebugActivity extends BaseActivity implements OnItemClickListener,OnItemLongClickListener {
    String rootPath;
    private static final int readDebugFile = 3000;

    private ListView lv;
    DebugAdapter adapter;
    private MyData data;
    WaitingDialog wd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        rootPath = this.getFilesDir().getAbsolutePath() + "/" + "DiningDebug/";
        findViewById(R.id.header_menu_scan).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.header_title)).setText("测试");

        wd = new WaitingDialog(this);
        wd.setCanceledOnTouchOutside(false);
        wd.show();
        initView();
        readDebugFile();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
//        lv.setOnItemLongClickListener(this);
        adapter = new DebugAdapter(new MyData());
        lv.setAdapter(adapter);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == readDebugFile) {
                data = (MyData) msg.obj;
//
                adapter.addData(data);
//                adapter = new DebugAdapter(data);
//                lv.setAdapter(adapter);
                wd.dismiss();
            }
        }
    };

    public void readDebugFile() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                MyData data = FileUtil.getDirFilesContent(rootPath);
                Message message = handler.obtainMessage(readDebugFile);
                message.obj = data;
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MyRow row = (MyRow) parent.getItemAtPosition(position);
//        if (row != null) {
//            Toast.makeText(this, row.getString(row.keyAt(0)), Toast.LENGTH_SHORT).show();
//        }
//        String key = row.keyAt(0);
//        Bundle bun = new Bundle();
//        bun.putString("content",row.getString(key));
//        openIntent(DebugDetailActivity.class,key,bun);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
         Builder builder = new Builder(this);
        builder.setMessage("是否删除");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyRow row = (MyRow) parent.getItemAtPosition(position);
//                File file = new File(rootPath,row.keyAt(0));
//                if (file.exists()){
//                   boolean delete = file.delete();
//                    if (delete){
//                        Toast.makeText(DebugActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//                        MyData deleteData = new MyData();
//                        deleteData.add(row);
//                        adapter.removeData(deleteData);
//                    }else {
//                        Toast.makeText(DebugActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return true;
    }
}
