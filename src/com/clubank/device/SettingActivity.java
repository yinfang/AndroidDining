package com.clubank.device;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.util.CustomDialogView;
import com.clubank.util.CustomDialogView.Initializer;
import com.clubank.util.CustomDialogView.OKProcessor;
import com.clubank.util.UI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author jishu0416 设置
 */
public class SettingActivity extends BasebizActivity {

    private String[] printerQueryInterval;
    private static final int DLG_SERVER_URL = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        printerQueryInterval = getResources().getStringArray(
                R.array.printerQueryInterval);

        setEText(R.id.wsUrl, C.wsUrl);
        setEText(
                R.id.printerQueryInterval,
                printerQueryInterval[settings.getInt("printerQueryInterval", 0)]);
        //是否开启结账账功能 默认不开启
        showBoolean("jiezhang", false);
        //是否开启记账功能 默认开启
        showBoolean("jizhang", true);
        //是否开启微信结账功能 默认开启
        showBoolean("weipay", true);
        //是否开启支付宝结账功能 默认开启
        showBoolean("alipay", true);
        //点菜时是否必选做法 默认不开启
        showBoolean("popupCookWindow", false);
        //下单后不可退菜 默认不开启
        showBoolean("popupCancelDish", false);

        //是否显示口味分类的选项,根据后台返回的数据是否含有FlavorType,TypeName,Description
        if (settings.getBoolean("showflavorClassify", false)) {
            show(R.id.frame_flavorClassify);
            showBoolean("flavorClassify", false);
        } else {
            hide(R.id.frame_flavorClassify);
            saveSetting("flavorClassify", false);
        }
        showBoolean("popupQtyWindow", false);
        showBoolean("quickInputLockNumber", false);
        showBoolean("alertPrinterWarning", true);
        showBoolean("warningSound", true);
        showBoolean("warningVibration", true);
        showBoolean("customFlavor", false);
        showBoolean("kitchentype", BC.showKitchenType);
        showBoolean("guestsnum", false);
        showBoolean("ganka", false);
        showBoolean("disaccountint", false);
        if (BC.type == 0) {//标准版消费卡号是否必填设置
            show(R.id.card_no_ll);
            showBoolean("inputcardno", true);
        }
    }

    public void showUri(View view) {
        showCustomDialog(DLG_SERVER_URL, getString(R.string.lbl_enter_url),
                R.layout.server_url_item, -1);
    }

    public void showCustomDialog(final int type, CharSequence title,
                                 int layout, int resIcon) {

        CustomDialogView cd = new CustomDialogView(this);
        cd.setInitializer(new Initializer() {
            public void init(View view) {
                initDialog(type, view);
            }
        });
        cd.setOKProcessor(new OKProcessor() {
            public boolean process(Dialog ad) {
                return finishDialog(type, ad);
            }
        });
        cd.show(title, layout, resIcon);

    }

    public void selectValue(View view) {
        int id = view.getId();
        if (id == R.id.row_printerQueryInterval) {
            showListDialog(view, printerQueryInterval);
        }
    }

    @Override
    public void listSelected(View view, int index) {
        int id = view.getId();
        if (id == R.id.row_printerQueryInterval) {
            String v = printerQueryInterval[index];
            setEText(R.id.printerQueryInterval, v);
            saveSetting("printerQueryInterval", index);
        }
    }

    public void toggle(View view) {
        CheckBox c = (CheckBox) view;
        boolean value = c.isChecked();
        String name = (String) view.getTag();
        save(name, value);
    }

    private void showBoolean(String name, boolean defValue) {
        boolean value = settings.getBoolean(name, defValue);
        int id = UI.getId(this, name, "id");
        CheckBox iv = ((CheckBox) findViewById(id));
        iv.setChecked(value);
        iv.setTag(name);
        save(name, value);
    }

    private void save(String name, boolean value) {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        String wsUrl = settings.getString("wsUrl", "");
        if (wsUrl != null && !wsUrl.equals("")) {
            setEText(R.id.wsUrl, wsUrl);
        }
    }

    public void logout(View view) {
        setResult(RESULT_OK);
        finish();
    }

    protected void initDialog(int type, View view) {
        if (type == DLG_SERVER_URL) {
            EditText url = (EditText) view.findViewById(R.id.input);
            String wsUrl = settings.getString("wsUrl", C.wsUrl);
            if (wsUrl != null && !wsUrl.equals("")) {
                url.setText(wsUrl);
            }
        }
    }

    protected boolean finishDialog(int type, Dialog dialog) {
        if (type == DLG_SERVER_URL) {
            EditText url = (EditText) dialog.findViewById(R.id.input);
            String wsUrl = url.getText().toString();
            if (wsUrl != null && !wsUrl.equals("")) {
                setEText(R.id.wsUrl, wsUrl);
                saveSetting("wsUrl", wsUrl);
            }
        }
        return true;
    }


}
