package com.clubank.device;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetBookingInfo;
import com.clubank.op.GetPendingBil;
import com.clubank.op.GetTableByCardNo;
import com.clubank.op.VerifyConsumeCard;
import com.clubank.util.CustomDialogView;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import java.io.File;
import java.util.Date;

/**
 * 桌台中心
 *
 * @author jishu0416
 */
@SuppressLint("InflateParams")
public class TableActivity extends BasebizActivity {

    private MyData areas;
    private String[] areaNames;
    private MyData tables;
    private MyData pending;
    private MyData booking;
    private MyData carding;
    private TableAdapter ta;
    private int QUERY_TABLE = 20;
    private String cardNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_table);

        findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.header_title)).setText(getString(R.string.menu_Table));
        File f = new File(this.getExternalFilesDir(null), BC.DB_NAME);
        // String is;
        // if(f.exists()){
        // is="true";
        // }else{
        // is="f";
        // }
        String sql = "select AreaCode,AreaName from t_DiningArea where shopcode=?";
        areas = db.getData(sql, new String[]{S.shopCode},
                new String[]{f.getAbsolutePath(), "true"});
        if (areas != null) {
            int size = areas.size();
            areaNames = new String[size];
            for (int i = 0; i < size; i++) {
                areaNames[i] = (String) areas.get(i).get("AreaName");
            }
        }
        menus = new String[]{"menu_area", "menu_refresh", "menu_query_table"};
    }

    @Override
    public void onResume() {
        loadTable();
        super.onResume();
        findViewById(R.id.menu).setVisibility(View.GONE);
        findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.header_home).setVisibility(View.GONE);
    }

    public void back(View view) {
        goMain();
    }

    @Override
    public void onBackPressed() {
        goMain();
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetPendingBil.class) {
            pending = (MyData) result.obj;
            Log.d("------", "onPostExecute: ");
            if (pending != null && pending.size() > 0) {
                ta.notifyDataSetChanged();
            }
        } else if (op == GetBookingInfo.class) {
            booking = (MyData) result.obj;
            if (booking != null && booking.size() > 0) {
                ta.notifyDataSetChanged();
            }
        } else if (op == GetTableByCardNo.class) {
            if (carding != null) {
                carding.clear();
            }
            carding = (MyData) result.obj;
            ta.notifyDataSetChanged();

        }else if (op == VerifyConsumeCard.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                if (carding != null) {
                    carding.clear();
                }
                if (!BC.engineerMode){
                    new MyAsyncTask(this, GetTableByCardNo.class).execute(cardNumber, S.token);
                }
                // 无效消费卡号
            } else if (result.code == BC.RESULT_INVALID_CONSUME_CARD) {
                UI.showError(this, String.format(getText(R.string.msg_invalid_card_no).toString(),cardNumber));
                // 未登记的消费卡号 消费卡号验证 下单
            } else if (result.code == BC.RESULT_REGIST_CONSUME_CARD) {
                UI.showInfo(this, R.string.lbl_codeNOregist);
            } else if (result.code == BC.RESULT_BILL_CHECKED_OUT) {
                UI.showError(this, R.string.msg_bill_checked_out);
            } else if (result.code == BC.RESULT_UNCHARGEABLE_BILL) {
                UI.showError(this, R.string.msg_unchargeable_bill);
            } else if (result.code == BC.RESULT_CONSUME_CARD_LEVEL) {
                UI.showError(this, R.string.msg_consume_card_level);
            }
        }
    }

    /**
     * 初始化桌台数据
     */
    public void loadTable() {
        TextView areaName = (TextView) findViewById(R.id.areaName);
        if (areas != null && areas.size() > 0) {//防止数组越界

            if (S.areaPosition >= areas.size()) {
                S.areaPosition = 0;
            }
            areaName.setText(areas.get(S.areaPosition).getString("AreaName"));
            String areaCode = areas.get(S.areaPosition).getString("AreaCode");


            // new MyAsyncTask(this, Action.GET_BOOKING_INFO).execute(areaCode);
            tables = db.getData(
                    "select Code,Name from T_Diningtable where areacode=? and status<9",
                    new String[]{areaCode});
            GridView gridview = (GridView) findViewById(R.id.gridViewTable);
//            this.registerForContextMenu(gridview);

            ta = new TableAdapter(this, R.layout.gvi_table, tables);
            gridview.setAdapter(ta);
            if (!BC.engineerMode){
                new MyAsyncTask(this, GetPendingBil.class).execute(S.shopCode,
                        areaCode, BC.df_yMd.format(new Date()), "", "", "");
            }
            Log.d("------", "loadTable: ");;
            gridview.setOnItemClickListener(new ItemClickListener());
            gridview.setOnItemLongClickListener(new ItemLongClickListener());
        } else {

            UI.showError(this, R.string.msg_update_data_first);
            return;

        }
    }

    class ItemLongClickListener implements OnItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // arg1.showContextMenu();
            return true;
        }
    }

    /**
     * 点击桌台正在使用 跳转ChargeListActivity ，未使用 跳转QuickOpenActivity
     *
     * @author jishu0416
     */
    class ItemClickListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            String code = tables.get(arg2).getString("Code");
            boolean using = false;
            for (int i = 0; pending != null && i < pending.size(); i++) {
                if (pending.get(i).get("TableNo").equals(code)) {
                    using = true;
                    break;
                }
            }
            if (using) {// 是否正在使用
                MyData areas;
                String sql = "select AreaCode,AreaName from t_DiningArea where shopcode=?";

                areas = db.getData(sql, new String[]{S.shopCode});
                if (S.areaPosition >= areas.size()) {
                    S.areaPosition = 0;
                }
                String areaCode = areas.get(S.areaPosition).getString(
                        "AreaCode");
                String areaName = areas.get(S.areaPosition).getString(
                        "AreaName");
                String date = BC.df_yMd.format(new Date());
                String condition = date + "  " + areaName;
                Intent intent = new Intent(TableActivity.this,
                        ChargeListActivity.class);
                intent.putExtra("title",
                        UI.getText(TableActivity.this, "menu_Charge"));
                intent.putExtra("condition", condition);
                intent.putExtra("date", date);
                intent.putExtra("areaCode", areaCode);
                intent.putExtra("code", code);
                intent.putExtra("from", "");
                startActivity(intent);
            } else {
                Intent intent = new Intent(TableActivity.this,
                        QuickOpenActivity.class);
                intent.putExtra("tablePosition", arg2);
                intent.putExtra("title",
                        UI.getText(TableActivity.this, "menu_QuickOpen"));
                startActivity(intent);
            }
        }
    }

    @Override
    public void menuSelected(View view, int index) {
        if (index == 0) {// 区域
            showListDialog(view, areaNames);
        } else if (index == 1) {// 刷新
            loadTable();
        } else if (index == 2) {//根据消费卡号查询此卡在那些桌台下过单
            showCustomDialog(QUERY_TABLE, getString(R.string.input_card_number),
                    R.layout.query_table_item, -1);
        }
    }


    public void showCustomDialog(final int type, CharSequence title,
                                 int layout, int resIcon) {

        CustomDialogView cd = new CustomDialogView(this);
        cd.setInitializer(new CustomDialogView.Initializer() {
            public void init(View view) {
                initDialog(type, view);
            }
        });
        cd.setOKProcessor(new CustomDialogView.OKProcessor() {
            public boolean process(Dialog ad) {
                return finishDialog(type, ad);
            }
        });
        cd.show(title, layout, resIcon);

    }

    protected void initDialog(int type, View view) {
        if (type == QUERY_TABLE) {

        }
    }

    protected boolean finishDialog(int type, Dialog dialog) {
        if (type == QUERY_TABLE) {
            EditText cardNoText = (EditText) dialog.findViewById(R.id.input);
             cardNumber = cardNoText.getText().toString().trim();
            if (!TextUtils.isEmpty(cardNumber)) {
                VerifyConsumeCard(cardNumber);
            } else {
                UI.showInfo(this, "消费卡号不能为空");
            }
        }
        return true;
    }

    private void VerifyConsumeCard(String cardNo) {
        if (!"".equals(cardNo)) {
            if (!BC.engineerMode){
                new MyAsyncTask(this, VerifyConsumeCard.class).execute(cardNo, false);
            }
        }
    }

    @Override
    public void listSelected(View view, int index) {
        S.areaPosition = index;
        saveSetting("areaPostion", index);
        loadTable();
    }

    private class TableAdapter extends ArrayAdapter<MyRow> {


        public TableAdapter(Context context, int resId, MyData data) {
            super(context, resId, data);

        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context
                        .LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.gvi_table, null);
            }
            MyRow o = getItem(position);
            if (o != null) {
                String code = (String) o.get("Code");
                ImageView t1 = (ImageView) v.findViewById(R.id.table_image);
                TextView t2 = (TextView) v.findViewById(R.id.table_name);
                boolean using = false;
                boolean booked = false;
                for (int i = 0; pending != null && i < pending.size(); i++) {
                    if (pending.get(i).get("TableNo").equals(code)) {
                        using = true;
                        break;
                    }
                }
                //查询到消费卡号开过的台号
                if (carding != null) {
                    String tableName = (String) o.get("Name");
                    String areaCode = areas.get(S.areaPosition).getString("AreaCode");
                    boolean cardUse = false;
                    for (int i = 0; i < carding.size(); i++) {
                        String Name = carding.get(i).getString("Name");
                        String Code = carding.get(i).getString("Code");
                        String AreaCode = carding.get(i).getString("AreaCode");
                        if (Code.equals(code) && Name.equals(tableName) && areaCode.equals(AreaCode) ) {
                            cardUse = true;
                            break;
                        }
                    }
                    if (cardUse) {
                        t2.setBackground(getResources().getDrawable(R.drawable.table_tv_bg_orange));
//                        t2.setBackgroundColor(getResources().getColor(R.color.orange));
                        t2.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        t2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        t2.setTextColor(getResources().getColor(R.color.yellow_title));
                    }
                } else {
                    t2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    t2.setTextColor(getResources().getColor(R.color.yellow_title));
                }


                if (!using) {

                    for (int i = 0; booking != null && i < booking.size(); i++) {
                        if (booking.get(i).get("TableNo").equals(code)) {
                            booked = true;
                            break;
                        }
                    }
                }
                if (using) {
                    t1.setImageResource(R.drawable.table_using);
                } else if (booked) {
                    t1.setImageResource(R.drawable.table_empty);
                } else {
                    t1.setImageResource(R.drawable.table_empty);
                }
                t2.setText((String) o.get("Name"));
            }
            return v;
        }
    }

}
