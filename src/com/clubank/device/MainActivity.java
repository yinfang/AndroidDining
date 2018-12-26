package com.clubank.device;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.util.AsyncCommonTask;
import com.clubank.util.DownloadDemoFile;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyData;
import com.clubank.util.MyDebugView;
import com.clubank.util.U;
import com.clubank.util.UI;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * dining这个应用打包时注意把C.DBNAME改为Dining.db
 */
@SuppressWarnings("unused")
public class MainActivity extends BasebizActivity {

    private final int REQUEST_LOGIN = 102;
    private final int REQUEST_SETTING = 101;
    private boolean startedService;
    private int CMENU_MAIN = 1;
    private final int MENU_EXIT = 1;
    private final int MENU_SETTING = 2;
    private final int MENU_FEEDBACK = 3;
    // private static final int MENU_GUIDE = 4;
    private final int MENU_CHECK_VERSION = 5;
    private final int MENU_SHARED = 6;
    private final int MENU_ABOUT = 7;
    private MyData areas = new MyData();
    private int currVersion = 1;
    private boolean autoChecked;
    private final int DLG_CONFIRM_EXIT = 1;
    private final int DLG_CONFIRM_DOWNLOAD_DEMO_DATA = 3;
    private final int ID_DOWNLOAD_DEMO_DATA = 1;
    private final int ID_UNZIP_FILE = 2;
//    private static int debugCount = 0;
//    private static int debugWhat = 0x1111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        timeTask();
        ((MyDebugView) findViewById(R.id.tv_debug)).setDebugLongPress(new DebugLongPressClick());
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        biz.checkLogin(C.REQUEST_LOGIN);
    }

    public void logined(int type) {

        if (!BC.engineerMode && !startedService) { // 非工程模式
            // 启动服务（默认每个三分钟检测打印机是否正常，可设置一分钟检测）
            startedService = true;
            Intent it = new Intent();
            it.setClass(MainActivity.this, NotifyService.class);
            startService(it);
        }
        if (!BC.engineerMode && !autoChecked) {
            autoChecked = true;
            // int type = C.clubCode == null ? 1 : 2;
            // new MyAsyncTask(this, CheckNewVersion.class, false).execute(
            // currVersion, false);
//			biz.checkVersion(false);
        }
    }

    ;

    /**
     * 跳转到各功能界面
     */
    public void clickMenu(View view) {
        if (BC.engineerMode && !checkDemoData()) {// 工程模式 先监测Demo数据
            // app.db文件不存在时，提示是否下载Demo数据
            return;
        }


        int id = view.getId();
        // 根据餐厅编码查询区域名称和区域编码为空，且用户未点击 设置，更新数据 或 扫描菜品
        // 时提示“请先更新数据”--UpdateDataActivity


        if (areas.size() == 0
                && !(id == R.id.m_update_data || id == R.id.m_setting || id == R.id
                .m_scan_deliver)) {
            UI.showError(MainActivity.this, R.string.msg_update_data_first);
            return;
        }

        if (BC.engineerMode) {
            if (id == R.id.m_charge_bill || id == R.id.m_query_bill || id == R.id.m_scan_deliver
                    || id == R.id.m_delayed || id == R.id.m_printer || id == R.id.m_update_data) {
                UI.showInfo(this, R.string.engineer_mode_do_not_support);
                return;
            }
        }

        if (id == R.id.m_table_center) {// 桌台中心
            Intent it = new Intent(this, TableActivity.class);
            it.putExtra("from", "");
            it.putExtra("title", getText(R.string.menu_Table));
            startActivity(it);
        } else if (id == R.id.m_quick_open) {// 快速开台
            openIntent(QuickOpenActivity.class, R.string.menu_QuickOpen);
        } else if (id == R.id.m_charge_bill) {// 消费记账
            openIntent(ChargeActivity.class, R.string.menu_Charge);
        } else if (id == R.id.m_query_bill) {// 账单查询
            openIntent(QueryBillActivity.class, R.string.menu_QueryBill);
        } else if (id == R.id.m_dish_list) {// 菜品推荐
            S.orderMode = false;
            openIntent(DishJianListActivity.class, R.string.recommend);
        } else if (id == R.id.m_scan_deliver) {// 扫描上菜
            openScanner();
        } else if (id == R.id.m_delayed) {// 菜品延迟
            openIntent(DelayedActivity.class, R.string.menu_Delayed);
        } else if (id == R.id.m_printer) {// 厨打监控
            openIntent(PrinterActivity.class, R.string.menu_Printer);
        } else if (id == R.id.m_update_data) {// 更新数据
            openIntent(UpdateDataActivity.class, R.string.menu_UpdateData);
        } else if (id == R.id.m_setting) {// 设置
            setting(view);

            // openIntent(SettingActivity.class, R.string.menu_Setting);
        }else if (id == R.id.query_break) {
            openIntent(QueryBreakfastTicketActivity.class, R.string.menu_query_breakfast_use);
        }
    }

    @Override
    public void onResume() {
        TextView loginInfo = (TextView) this.findViewById(R.id.textLoginInfo);
        loginInfo.setText(S.userName + "(" + S.shopName + ") "
                + BC.df_yMdHm.format(new Date()));

        loadArea();
        super.onResume();
    }

    private void loadArea() {
        if (S.shopCode != null) {
            String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
            areas = db.getData(sql, new String[]{S.shopCode});
//			areas = db.getData(sql,new String[]{"CT"});
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UI.showOKCancel(MainActivity.this, DLG_CONFIRM_EXIT,
                    R.string.msg_confirm_exit, R.string.app_name);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {// 停止服务
        super.onDestroy();
        try {
            Intent intent = new Intent(this, NotifyService.class);
            stopService(intent);
            NotificationManager nm = (NotificationManager) getSystemService(Context
                    .NOTIFICATION_SERVICE);
            nm.cancel(BC.NOTIFY_PRINTER_ERROR);
        } catch (Exception e) {
        }
    }

    @Override
    public void processDialogOK(int type, final Object tag) {
        super.processDialogOK(type, tag);
        if (type == DLG_CONFIRM_EXIT) {
            try {
                logout();
                exit(true);
            } catch (Exception e) {
                // TODO: handle exception
            }

        } else if (type == DLG_CONFIRM_DOWNLOAD_DEMO_DATA) {
            new DownloadDemoFile(this, DownloadDemoFile.class, BC.zipfile)
                    .execute(BC.demoData);
        }

    }

    public void menu(View view) {
        openOptionsMenu();
    }

    Dialog d;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (d == null) {
            d = createDialog(CMENU_MAIN, null);
        }
        if (d != null) {
            d.show();
        }
        return false;// 返回为true 则显示系统menu
    }

    public void setting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("title", getText(R.string.menu_setting));
        startActivityForResult(intent, REQUEST_SETTING);
        // startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Dialog createDialog(int id, Object tag) {
        // final Object mytag = tag;
        IconContextMenu m = null;
        if (id == CMENU_MAIN) {
            m = new IconContextMenu(this, tag);
            m.add(R.string.m_exit, R.drawable.cm_exit, MENU_EXIT);
            m.add(R.string.lbl_Share, R.drawable.cm_share, MENU_SHARED);
            m.add(R.string.m_feedback, R.drawable.cm_feedback, MENU_FEEDBACK);
            m.add(R.string.m_check_version, R.drawable.cm_check_version,
                    MENU_CHECK_VERSION);
            m.add(R.string.m_network_setting, R.drawable.cm_setting,
                    MENU_SETTING);
            m.add(R.string.title_about, R.drawable.cm_about, MENU_ABOUT);
            // m.add(R.string.m_welcome, R.drawable.cm_guide, MENU_GUIDE);

            m.setOnClickListener(new IconContextMenuOnClickListener() {
                public void onClick(int menuId) {
                    switch (menuId) {
                        case MENU_SETTING:
                            startActivityForResult(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS), 0);
                            break;
                        case MENU_FEEDBACK:
                            Intent i3 = new Intent(MainActivity.this,
                                    FeedbackActivity.class);
                            i3.putExtra("title", getText(R.string.title_feedback));
                            startActivity(i3);
                            break;
                        // case MENU_GUIDE:
                        // startActivity(new Intent(MainActivity.this,
                        // GuideActivity.class));
                        // break;
                        case MENU_CHECK_VERSION:
//						biz.checkVersion(true);
                            break;
                        case MENU_SHARED:
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT,
                                    getText(R.string.lbl_ShareTitle));
                            intent.putExtra(Intent.EXTRA_TEXT,
                                    getText(R.string.lbl_ShareText));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent,
                                    getText(R.string.lbl_ShareTo)));
                            break;
                        case MENU_ABOUT:
                            Intent i5 = new Intent(MainActivity.this,
                                    AboutActivity.class);
                            i5.putExtra("title", getText(R.string.title_about));
                            startActivity(i5);
                            break;
                        case MENU_EXIT:
                            UI.showOKCancel(MainActivity.this, DLG_CONFIRM_EXIT,
                                    R.string.msg_confirm_exit, R.string.app_name);
                            break;
                    }
                }
            });
            return m.createMenu(getText(R.string.lbl_select).toString());
        }
        return null;
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == DownloadDemoFile.class) {
            File dir = getExternalFilesDir(null);
            File f = new File(dir, BC.zipfile);
            if (f.exists()) {
                new AsyncCommonTask(this, AsyncCommonTask.class).execute(
                        f.getAbsolutePath(), dir.getAbsolutePath());
            }
        }
    }

    /**
     * 解压缩 dining.zip Demo 文件
     */
    public void doWork(Object... args) {
        U.unzip((String) args[0], (String) args[1]);
        loadArea();
    }

    private boolean checkDemoData() {
        File f = new File(getExternalFilesDir(null), BC.DB_NAME);
        if (!f.exists()) {
            UI.showOKCancel(this, DLG_CONFIRM_DOWNLOAD_DEMO_DATA,
                    R.string.lbl_confirm_download_demo_data,
                    R.string.lbl_missing_demo_data);
            return false;
        }
        if (areas == null || areas.size() == 0) {
            UI.showOKCancel(this, DLG_CONFIRM_DOWNLOAD_DEMO_DATA,
                    R.string.lbl_confirm_download_demo_data,
                    R.string.lbl_missing_demo_data);
            return false;
        }
        return true;
    }


    class DebugLongPressClick implements MyDebugView.DebugLongPress {
        @Override
        public void debugLongPress(View v) {
            Intent intent = new Intent(MainActivity.this, DebugActivity.class);
            startActivity(intent);
        }
    }

}
