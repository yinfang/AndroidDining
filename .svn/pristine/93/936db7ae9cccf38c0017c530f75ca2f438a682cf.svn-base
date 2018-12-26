package com.clubank.device;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetCouponResult;
import com.clubank.op.ModifyCouponResult;
import com.clubank.util.CustomDialogView;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * 早餐券 校验卡 和使用早餐券
 */
public class QueryBreakfastTicketActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final int DLG_USE_TICKET = 652;
    EditText edt_card_no;
    EditText edt_use_num;
    TextView all_ticket, used_ticket, unUsed_ticket;

    ListView listView;
    BreakFastTickerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_breakfast_ticket);
        hide(R.id.header_menu_scan);
        edt_card_no = (EditText) findViewById(R.id.edt_card_no);
        edt_use_num = (EditText)findViewById(R.id.edt_use_num);
        all_ticket =(TextView) findViewById(R.id.all_ticket);
        used_ticket = (TextView)findViewById(R.id.used_ticket);
        unUsed_ticket = (TextView)findViewById(R.id.unUsed_ticket);
        listView = (ListView) findViewById(R.id.listView);
        MyData data = new MyData();
//        MyRow r = new MyRow();
//        r.put("RoomNo", "sss");
//        r.put("CardNo", "sss");
//        r.put("GuestName", "sss");
//        r.put("Tel", "sss");
//        r.put("Sex", "男");
//        r.put("CouponNumTotal", "10");
//        r.put("UsedCouponNum", "5");
//        r.put("NotUsedCouponNum", "5");
//        data.add(r);
//        data.add(MyRow.fromMap(r));

        adapter = new BreakFastTickerAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.btn_check_card:
            case R.id.btn_check_card_1:
                checkTicketList(getEText(R.id.edt_card_no_1));
                break;
            case R.id.btn_gan_card:
            case R.id.btn_gan_card_1:
                break;
            case R.id.btn_use:
                break;
            case R.id.btn_item_use:
                itemUse((MyRow) view.getTag());
                break;
            default:
                break;
        }
    }

    /**
     * 选中列表的某一项使用早餐券
     *
     * @param row
     */
    private void itemUse(MyRow row) {
        if (row != null) {
            showCustomDialog(DLG_USE_TICKET, "使用早餐券", R.layout.dialog_use_breakfast, 0, row);
        }
    }


    /**
     * 根据房卡号或者消费卡号查询早餐券
     *
     * @param cardNo
     */
    private void checkTicketList(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            UI.showToast(this, "请输入卡号");
            return;
        }

        new MyAsyncTask(this, GetCouponResult.class).run(cardNo);

    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetCouponResult.class) {
            adapter.clear();
            if (result.code == BC.RESULT_SUCCESS) {
                MyData data = (MyData) result.obj;
                if (data != null) {
                    adapter.addAll(data);
                }
            } else {
                UI.showToast(this, "未查询到相关信息");
            }
        } else if (op == ModifyCouponResult.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showToast(this, "使用成功");
                checkTicketList(getEText(R.id.edt_card_no_1));
            } else {
                if (result.obj != null){
                    UI.showToast(this, result.obj.toString());
                }
            }
        }
    }


    /**
     * 自定义对话框
     *
     * @param type    用于区分哪个功能的对话框
     * @param title   标题
     * @param layout  内容布局文件
     * @param resIcon 标题前图标（左），默认为Alert
     */
    public void showCustomDialog(final int type, CharSequence title,
                                 int layout, int resIcon, final Object tag) {

        CustomDialogView cd = new CustomDialogView(this);
        cd.setInitializer(new CustomDialogView.Initializer() {
            public void init(View view) {
                initDialog(type, view, tag);
            }
        });
        cd.setOKProcessor(new CustomDialogView.OKProcessor() {
            public boolean process(Dialog ad) {
                return finishDialog(type, ad, tag);
            }
        });
        cd.setCancelProcessor(new CustomDialogView.CancelProcessor() {
            @Override
            public void cancel(Dialog dialog) {
                hideSoftKeyboard();
            }
        });
        cd.show(title, layout, resIcon);

    }

    protected void initDialog(int type, View view, Object tag) {
        if (type == DLG_USE_TICKET) {
            MyRow row = (MyRow) tag;
            EditText nowUseNum = (EditText) view.findViewById(R.id.input);
            setDEText(view, R.id.tv_house_no, row.getString("RoomCode"));
            setDEText(view, R.id.tv_card_no, row.getString("CardNo"));
            setDEText(view, R.id.tv_name, row.getString("GuestName"));
//            setDEText(view,R.id.tv_mobile, row.getString("Tel"));
//            setDEText(view,R.id.tv_sex, row.getString("Sex"));
            setDEText(view, R.id.tv_all_ticket, row.getInt("CouponNumTotal") + "");
            setDEText(view, R.id.tv_used_ticket, row.getInt("UsedCouponNum") + "");
            setDEText(view, R.id.tv_unused_ticket, row.getInt("NotUsedCouponNum") + "");
        }
    }

    public void setDEText(View view, int resId, CharSequence value) {
        if (value != null) {
            TextView tv = (TextView) view.findViewById(resId);
            if (tv != null) {
                tv.setText(value.toString().trim());
            }
        }
    }


    protected boolean finishDialog(int type, Dialog dialog, Object tag) {
        if (type == DLG_USE_TICKET) {
            hideSoftKeyboard();
            MyRow row = (MyRow) tag;
            EditText nowUseNum = (EditText) dialog.findViewById(R.id.input);
            String noNu = nowUseNum.getText().toString();
            if (TextUtils.isEmpty(noNu)) {
                UI.showToast(this, "请输入数量");
                return false;
            }
            int num = Integer.parseInt(noNu.trim());
            String userCode = settings.getString("userCode", S.userCode);
            new MyAsyncTask(this, ModifyCouponResult.class).run(row.getString("CardNo"), num, userCode);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideSoftKeyboard();
    }
}
