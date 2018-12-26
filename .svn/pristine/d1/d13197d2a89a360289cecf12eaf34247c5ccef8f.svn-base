package com.clubank.device;

import java.util.Date;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Bill;
import com.clubank.domain.CheckinCriteria;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.ChangeTable;
import com.clubank.op.GetBillByCode;
import com.clubank.op.GetPendingBil;
import com.clubank.op.QueryCheckinInfo;
import com.clubank.op.VerifyConsumeCard;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * @author jishu0416 快速开台
 */
public class QuickOpenActivity extends BasebizActivity {

    private MyData areas;
    private MyData tables;
    private MyData pending;// pending order, using table
    /* pending order on one specific table */
    private MyData tablePending = new MyData();
    private int pendingIndex = -1;
    private int tablePosition = -1;
    private String[] areaNames;
    private String[] tableNames = new String[0];
    private String[] billNames = new String[0];
    private int REQUEST_CHANGE_TABLE = 1;
    private int REQUEST_QUERY_CHECKIN = 2;
    private boolean shareTable;// 并台
    private EditText qo_card_no;
    private boolean agenVerify;

    private final int NFC_REQUEST_CODE = 14382;
    private final int OPEN_NFC_TYPE_DIALOG = 25483;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_open);
        if (BC.type == 0) {
            //            hide(R.id.remark);
        } else {
            show(R.id.remark);
        }
        findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
        /*
         * EditText remark = (EditText)findViewById(R.id.lbl_ordersign);//备注
         * remark.addTextChangedListener(new TextWatcher() {
         *
         * @Override public void onTextChanged(CharSequence s, int start, int
         * before, int count) {
         *
         *
         * }
         *
         * @Override public void beforeTextChanged(CharSequence s, int start,
         * int count, int after) { // TODO Auto-generated method stub
         *
         * }
         *
         * @Override public void afterTextChanged(Editable s) { // TODO
         * Auto-generated method stub
         *
         * } });
         */
        qo_card_no = (EditText) findViewById(R.id.qo_card_no);
        qo_card_no.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                qo_card_no.setTextColor(getResources().getColor(R.color.my_black));
                findViewById(R.id.row_identityName).setVisibility(View.GONE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        qo_card_no.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean isFocus) {
                if (!getEText(R.id.qo_card_no).equals("") && !isFocus) {
                    VerifyConsumeCard();
                }
            }
        });

        // criteria.MemNo = getEText(R.id.memno);
        // EditText qo_mem_no = (EditText) findViewById(R.id.qo_mem_no);
        /*
         * qo_mem_no.setOnFocusChangeListener(new OnFocusChangeListener() {
         *
         * @Override public void onFocusChange(View v, boolean hasFocus) {
         * EditText et = (EditText) v; if(!hasFocus){ if
         * (!et.getText().toString().equals("")) { criteria.MemNo =
         * et.getText().toString(); new MyAsyncTask(QuickOpenActivity.this,
         * QueryCheckinInfo.class).execute(criteria); } } } });
         */

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tablePosition = bundle.getInt("tablePosition", -1);
        }

        areas = db.getData("select AreaCode,AreaName from T_DiningArea where shopcode=?", new
                String[]{S.shopCode});
        int size = areas.size();
        areaNames = new String[size];
        for (int i = 0; i < size; i++) {
            areaNames[i] = (String) areas.get(i).get("AreaName");
        }
        loadTable();
        setEText(R.id.AreaName, (String) areas.get(S.areaPosition).get("AreaName"));
        menus = new String[]{"menu_verify_card", "menu_change_table"};

        if (settings.getBoolean("ganka", false)) {
            show(R.id.button_gan_card);
        } else {
            hide(R.id.button_gan_card);
        }
    }

    public void remark(View v) {
        showListDialog(v, getResources().getStringArray(R.array.manual_number));
    }

    @Override
    protected void onStart() {
        String isShow = settings.getString("isShow1", "false");
        if (isShow.equals("false")) {
            findViewById(R.id.imageView).setVisibility(View.VISIBLE);
            findViewById(R.id.tip).setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    private void VerifyConsumeCard() {
        EditText cardNo = (EditText) findViewById(R.id.qo_card_no);
        String s = cardNo.getText().toString().trim();
        if (!"".equals(s)) {
            if (!BC.engineerMode) {
                new MyAsyncTask(this, VerifyConsumeCard.class).execute(s, false);
            }
        }
    }

    @Override
    public void menuSelected(View view, int index) {
        if (index == 0) {// 校验消费卡
            VerifyConsumeCard();
        } else if (index == 1) {// 转台
            if (pendingIndex >= 0) {
                Intent intent = new Intent(this, ChangeTableActivity.class);
                intent.putExtra("oldCode", tables.get(tablePosition).getString("Code"));
                intent.putExtra("oldName", tables.get(tablePosition).getString("Name"));
                intent.putExtra("title", getText(R.string.menu_change_table));
                intent.putExtra("billName", billNames[pendingIndex]);
                intent.putExtra("guestName", getEText(R.id.qo_guest_name));
                startActivityForResult(intent, REQUEST_CHANGE_TABLE);
            } else {
                UI.showError(QuickOpenActivity.this, R.string.lbl_no_bill_code);
            }
        }
    }

    public void selectArea(View view) {
        showListDialog(view, areaNames);
    }

    public void selectTable(View view) {
        showListDialog(view, tableNames);
    }

    public void selectBill(View view) {
        if (tablePending.size() > 0) {
            showListDialog(view, billNames);
        }
    }

    @Override
    public void listSelected(View view, int index) {
        int id = view.getId();
        if (id == R.id.AreaName) {
            S.areaPosition = index;
            setEText(R.id.AreaName, areaNames[index]);
            loadTable();
        } else if (id == R.id.TableName) {
            tablePosition = index - 1;
            setEText(R.id.TableName, tableNames[index]);
            // fillTablePending();
            loadTable();
        } else if (id == R.id.bill_code) {
            if (index == billNames.length - 1) {// share table
                pendingIndex = -1;
            } else {
                pendingIndex = index;
                // billCode = tablePending.get(index).getString("BillCode");
            }
            fillOneBill();
        }
        if (id == R.id.remark) {// 备注
            setEText(R.id.lbl_ordersign, getResources().getStringArray(R.array.manual_number)
                    [index]);

        }
    }

    /**
     * fill screen according to pending bill data
     *
     * @param
     */
    private void fillTablePending() {
        tablePending.clear();
        pendingIndex = -1;
        // 获得当前桌台的账单
        if (tablePosition >= 0) {
            String tableNo = tables.get(tablePosition).getString("Code");
            int size = pending.size();
            for (int i = 0; pending != null && i < size; i++) {
                MyRow row = pending.get(i);
                if (tableNo.equals(row.get("TableNo"))) {
                    tablePending.add(row);
                }
            }
            if (tablePending.size() > 0) {
                pendingIndex = 0;
            }
            int tablesize = tablePending.size();
            // 填充账单名称数组
            billNames = new String[tablesize + 1];
            for (int i = 0; i < tablesize; i++) {
                String b = tablePending.get(i).getString("BillCode");
                String s = tablePending.get(i).getString("GuestName");
                String t = tablePending.get(i).getString("OpenTime").substring(11, 16);// time
                s = s == null ? "" : s;
                billNames[i] = b + " (" + t + s + ")";
            }
            billNames[tablesize] = getString(R.string.lbl_share_table);
        }
        fillOneBill();
    }

    private void fillOneBill() {
        // no pending bill on this table
        // setEText("qo_card_no", "");
        if (BC.type == 2) {// 安吉轩默认人数为空
            setEText(R.id.qo_total_guest, "");
        } else {
            if (settings.getBoolean("guestsnum", false)) {
                setEText(R.id.qo_total_guest, "");
            }
        }
        /*
         * setEText("qo_mem_no", ""); setEText("qo_guest_name", "");
         */

        if (tablePending.size() == 0) {
            setEText(R.id.bill_code, "");
        }

        if (tablePending.size() == 0 || pendingIndex == -1 || shareTable) {
            return;
        }

        MyRow b = tablePending.get(pendingIndex);
        setEText(R.id.bill_code, billNames[pendingIndex]);
        setEText(R.id.qo_card_no, b.getString("CardNo"));
        setEText(R.id.qo_guest_name, b.getString("GuestName"));
        setEText(R.id.qo_total_guest, b.getString("TotalGuest"));
        setEText(R.id.qo_mem_no, b.getString("MemNo"));

    }

    @Override
    public void onResume() {
        if (areas.size() > 0) {
            loadTable();
        }
        super.onResume();
        findViewById(R.id.menu).setVisibility(View.GONE);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetBillByCode.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyRow row = (MyRow) result.obj;
                if (!row.get("BillItems").equals("")) {
                    MyData items = (MyData) row.get("BillItems");
                    S.bill = new Bill();
                    if (items != null && items.size() > 0) {
                        BU.fillBillItems(items);
                    }
                    BU.fillBill(row);
                    fillBillMaster();
                }
                startOrder(2);
            }
        } else if (op == GetPendingBil.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                pending = (MyData) result.obj;
                fillTablePending();
            }
        } else if (op == VerifyConsumeCard.class) {
            String cardno = getEText(R.id.qo_card_no);
            int color = R.color.my_black;
            if (result.code == BC.RESULT_SUCCESS) {
                MyRow info = (MyRow) result.obj;
                afterVerifiedCard(info);
                String s = String.format(getText(R.string.msg_valid_card_no).toString(), cardno);
                // UI.showInfo(this, s, 0);
                setEText(R.id.identityName, info.getString("IdentityName"));
                findViewById(R.id.row_identityName).setVisibility(View.VISIBLE);
                // 通过点击开台进行消费卡号验证 下单
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                color = R.color.green5;
                if (agenVerify) {
                    openOrder();
                }
                // 无效消费卡号
            } else if (result.code == BC.RESULT_INVALID_CONSUME_CARD) {
                String s = String.format(getText(R.string.msg_invalid_card_no).toString(), cardno);
                UI.showError(this, s);
                setEText(R.id.qo_card_no, "");
                findViewById(R.id.qo_card_no).requestFocus();
                // 未登记的消费卡号 消费卡号验证 下单
            } else if (result.code == BC.RESULT_REGIST_CONSUME_CARD) {
                color = R.color.red;
                UI.showInfo(this, R.string.lbl_codeNOregist);
                if (agenVerify) {
                    openOrder();
                }
            } else if (result.code == BC.RESULT_BILL_CHECKED_OUT) {
                UI.showError(this, R.string.msg_bill_checked_out);
            } else if (result.code == BC.RESULT_UNCHARGEABLE_BILL) {
                UI.showError(this, R.string.msg_unchargeable_bill);
            } else if (result.code == BC.RESULT_CONSUME_CARD_LEVEL) {
                UI.showError(this, R.string.msg_consume_card_level);
            }
            qo_card_no.setTextColor(getResources().getColor(color));

        } else if (op == ChangeTable.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_operation_success);
                // String areaCode = (String) areas.get(S.areaPosition).get(
                // "AreaCode");
                // new AsyncRemoteTask(this, Action.GET_PENDING_BILL).execute(
                // areaCode, C.df_yMd.format(new Date()));
            }
        } else if (op == QueryCheckinInfo.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyData data = (MyData) result.obj;
                if (data != null && data.size() != 0) {
                    if (data.size() == 1) {
                        MyRow row = data.get(0);
                        setEText(R.id.qo_guest_name, row.getString("GuestName"));
                        setEText(R.id.qo_card_no, row.getString("CardNo"));
                    } else {
                        Intent i = new Intent(this, CheckinList.class);
                        Bundle b = new Bundle();
                        b.putSerializable("data", data);
                        i.putExtra("bundle", b);
                        startActivityForResult(i, 11);
                    }
                }
            } else {
                UI.showError(this, R.string.msg_no_checkin_exist);
            }
        }
    }

    private void afterVerifiedCard(MyRow info) {

        EditText guestName = (EditText) findViewById(R.id.qo_guest_name);
        guestName.setText((String) info.get("GuestName"));
        EditText memno = (EditText) findViewById(R.id.qo_mem_no);
        memno.setText((String) info.get("MemNo"));
    }

    public void verifyConsumeCard(View view) {
        EditText consumeCard = (EditText) findViewById(R.id.qo_card_no);
        String cardno = consumeCard.getText().toString();
        if (cardno != null) {
            if (!BC.engineerMode) {
                new MyAsyncTask(this, VerifyConsumeCard.class).execute(cardno);
            }
        }
    }

    public void ganCard(View view) {
        NFCinit nfcinit = NFCinit.getInstance(this);
        NfcAdapter mAdapter = nfcinit.getAdapter();
        if (mAdapter != null) {
            if (mAdapter.isEnabled()) {
                openIntent(NFCMainActivity.class, "感卡", new Bundle(), NFC_REQUEST_CODE);
            } else {
                UI.showOKCancel(this, OPEN_NFC_TYPE_DIALOG, "NFC功能未开启,是否立即开启?", "提示", 12);
            }

        } else {
            UI.showInfo(this, R.string.lbl_noNFC);
        }
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == OPEN_NFC_TYPE_DIALOG) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_NFC_SETTINGS);
            startActivity(intent);
        }
    }

    private void loadTable() {
        String areaCode = areas.get(S.areaPosition).getString("AreaCode");
        if (!BC.engineerMode) {
            new MyAsyncTask(this, GetPendingBil.class).execute(S.shopCode, areaCode, BC.df_yMd
                    .format
                            (new Date()), "", "", "");
        }
        String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
        tables = db.getData(sql, new String[]{areaCode});
        int size = tables.size();
        tableNames = new String[size + 1];
        tableNames[0] = getString(R.string.lbl_not_selected);
        for (int i = 0; i < size; i++) {
            tableNames[i + 1] = (String) tables.get(i).get("Name");
        }
        if (tablePosition >= 0) {
            setEText(R.id.TableName, tables.get(tablePosition).getString("Name"));
        }
        if (getIntent().getStringExtra("tableNo") != null && !getIntent().getStringExtra
                ("tableNo").equals("")) {
            for (int i = 0; i < size; i++) {
                if (getIntent().getStringExtra("tableNo").equals(tables.get(i).getString("Code"))) {
                    setEText(R.id.TableName, tables.get(i).getString("Name"));
                }
            }
        }
    }

    /**
     * 开台/点餐
     *
     * @param view
     */
    public void openOrder(View view) {
        String cardno = getEText(R.id.qo_card_no);
        if (cardno.equals("")) {
            openOrder();
        } else {
            agenVerify = true;
            VerifyConsumeCard();
        }

    }

    private void openOrder() {
        if (settings.getBoolean("guestsnum", false)) {
            String s = getEText(R.id.qo_total_guest);
            if (TextUtils.isEmpty(s)) {
                UI.showInfo(this, "请填写就餐人数");
                return;
            } else {
                try {
                    long nu = Long.parseLong(s);
                } catch (NumberFormatException e) {
                    UI.showInfo(this, "请填写就餐人数");
                    return;
                }
            }
        }
        agenVerify = false;
        S.bill.BillCode = null;
        S.bill.DiningRemarks = "";
        if (tablePending.size() > 0 && pendingIndex >= 0) {// 会员卡
            S.bill.BillCode = tablePending.get(pendingIndex).getString("BillCode");
        }

        if (S.bill.BillCode != null) {
            if (!BC.engineerMode) {
                new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
            }
        } else {
            S.bill = new Bill();
            fillBillMaster();
            if (!getEText(R.id.TableName).equals("")) {
                int size = tables.size();
                for (int i = 0; i < size; i++) {
                    if (getEText(R.id.TableName).equals(tables.get(i).getString("Name"))) {
                        S.bill.TableName = tables.get(i).getString("Name");
                        S.bill.TableNo = tables.get(i).getString("Code");
                    }
                }
            }
            startOrder(0);
        }
    }

    private void fillBillMaster() {
        S.bill.TotalGuest = 1;
        try {
            if (TextUtils.isEmpty(getEText(R.id.qo_total_guest))) {
                S.bill.TotalGuest = 1; // 人数
            } else {
                S.bill.TotalGuest = Integer.parseInt(getEText(R.id.qo_total_guest)); // 人数
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        S.bill.GuestName = getEText(R.id.qo_guest_name); // 姓名
        S.bill.CardNo = getEText(R.id.qo_card_no);
        S.bill.MemNo = getEText(R.id.qo_mem_no);
        S.bill.DiningRemarks = getEText(R.id.lbl_ordersign);

        if (tablePosition >= 0) {
            S.bill.TableNo = tables.get(tablePosition).getString("Code");
            S.bill.TableName = tables.get(tablePosition).getString("Name");
        } else {
            S.bill.TableNo = null;
            S.bill.TableName = null;
        }
    }

    /**
     * @param tab 目前只有0 ， 2。 0 菜品列表 1 快速点菜 2.下单界面 3.订单打折
     */
    private void startOrder(int tab) {
        S.orderMode = true;
        S.modified = false;
        Intent intent = null;
        if (tab == 0) {
            intent = new Intent(this, OrderDishListActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return;
        }

        if (tab == 1) {
            intent = new Intent(this, OrderQuickOrderActivity.class);
        } else if (tab == 2) {
            intent = new Intent(this, OrderOrderedActivity.class);
        } else if (tab == 3) {
            intent = new Intent(this, OrderDiscountActivity.class);
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_TABLE && resultCode == RESULT_OK) {
            String newTableNo = data.getExtras().getString("newTableNo");
            int size = tables.size();
            for (int i = 0; i < size; i++) {
                if (tables.get(i).getString("Code").equals(newTableNo)) {
                    tablePosition = i;
                    setEText(R.id.TableName, tables.get(i).getString("Name"));
                    break;
                }
            }
            String billCode = tablePending.get(pendingIndex).getString("BillCode");
            if (!BC.engineerMode) {
                new MyAsyncTask(this, ChangeTable.class).execute(billCode, newTableNo);
            }
        } else if (requestCode == REQUEST_QUERY_CHECKIN && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String memno = data.getStringExtra("memno");
            String cardno = data.getStringExtra("cardno");
            if (null != name && null != memno) {
                setEText(R.id.qo_guest_name, name);
                setEText(R.id.qo_mem_no, memno);
                setEText(R.id.qo_card_no, cardno);
            }

        } else if (requestCode == 11 && resultCode == RESULT_OK) {
            String infoname = data.getStringExtra("name");
            String infomemno = data.getStringExtra("memno");
            String infocardno = data.getStringExtra("cardno");
            if (null != infoname && null != infomemno) {
                setEText(R.id.qo_guest_name, infoname);
                setEText(R.id.qo_mem_no, infomemno);
                setEText(R.id.qo_card_no, infocardno);
            }
        } else if (requestCode == NFC_REQUEST_CODE && data != null) {
            Bundle b = data.getExtras();
            String codeNo = b.getString("cardNo");
            int statusCode = b.getInt("statusCode");
            if (codeNo != null && !codeNo.equals("") && statusCode == 0) {
                setEText(R.id.qo_card_no, codeNo);
                verifyConsumeCard(findViewById(R.id.button1));
            } else {
                UI.showError(this, R.string.msg_readCard_timeout);
            }
        } else if (requestCode == BC.REQUEST_CHOOSE_MEMBER && resultCode == RESULT_OK) {
            String infoname  = data.getStringExtra("name");
            String infomemno = data.getStringExtra("MemNo");
            setEText(R.id.qo_guest_name, infoname);
            setEText(R.id.qo_mem_no, infomemno);
        }
    }

    /**
     * 首次进入页面，提示图片点击隐藏，第二次进入不显示提示图片
     *
     * @param view
     */
    public void closeImage(View view) {
        findViewById(R.id.imageView).setVisibility(View.GONE);
        findViewById(R.id.tip).setVisibility(View.GONE);
        saveSetting("isShow1", "true");
    }

    /**
     * 姓名选择按钮事件
     *
     * @param view
     */
    public void queryCheckin(View view) {
        Intent intent = new Intent(this, CheckinList.class);
        intent.putExtra("name", getEText(R.id.qo_guest_name));
        intent.putExtra("memno", getEText(R.id.qo_mem_no));
        startActivityForResult(intent, REQUEST_QUERY_CHECKIN);
    }

    /**
     * 会员号获取按钮事件
     *
     * @param view
     */
    public void queryMemCheckin(View view) {
        if (!getEText(R.id.qo_mem_no).toString().equals("")) {
            CheckinCriteria criteria = new CheckinCriteria();
            criteria.MemNo = getEText(R.id.qo_mem_no).toString();
            if (!BC.engineerMode) {
                new MyAsyncTask(QuickOpenActivity.this, QueryCheckinInfo.class).execute(criteria);
            }
        }

    }

    /**
     * 会员卡信息获取
     *
     * @param view
     */
    public void queryMemCardInfo(View view) {
        String memNo = getEText(R.id.qo_mem_no);
//        if (!TextUtils.isEmpty(memNo)) {
            Intent i = new Intent(this, MemCardListActivity.class);
            i.putExtra("MemNo", memNo);
            startActivityForResult(i, BC.REQUEST_CHOOSE_MEMBER);
//            startActivity(i);
//        }
    }

}
