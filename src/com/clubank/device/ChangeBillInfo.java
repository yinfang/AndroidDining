package com.clubank.device;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Bill;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetBillLock;
import com.clubank.op.SaveBillInfo;
import com.clubank.op.VerifyConsumeCard;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

/**
 * 修改账单信息
 *
 * @author jishu0416
 */
public class ChangeBillInfo extends BasebizActivity {
    private MyRow row;
    private int COMMITOK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_billinfo);
        Bundle b = getIntent().getExtras();
        row = U.getRow(b, "info");
        // row = (MyRow) getIntent().getSerializableExtra("info");
        init();
    }

    private void init() {
        setEText(R.id.TableName, row.getString("TableName"));
        setEText(R.id.bill_code, row.getString("BillCode"));
        setEText(R.id.qo_card_no, row.getString("CardNo"));
        setEText(R.id.qo_total_guest, row.getString("TotalGuest"));
        setEText(R.id.username, row.getString("GuestName"));
        setEText(R.id.lbl_ordersign, row.getString("DiningRemarks"));
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == VerifyConsumeCard.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyRow row = (MyRow) result.obj;
                setEText(R.id.username, row.getString("GuestName"));
                commit();
            } else if (result.code == 2001) {
                UI.showError(this, R.string.lbl_codeNOregist);
            } else if (result.code == 2002) {
                UI.showError(this,
                        String.format(getText(R.string.msg_invalid_card_no).toString(), getEText(R.id.qo_card_no)));
            }
        } else if (op == SaveBillInfo.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_changesuccess, COMMITOK);
            } else {
                UI.showError(this, R.string.msg_changefail);
            }
        } else if (op == GetBillLock.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                if (BC.type == 0) {//标准版消费卡号是否必填设置
                    if (settings.getBoolean("inputcardno", false)) {//消费卡必填，验证消费卡
                        new MyAsyncTask(this, VerifyConsumeCard.class).execute(getEText(R.id.qo_card_no));
                    } else {
                        commit();
                    }
                } else {
                    new MyAsyncTask(this, VerifyConsumeCard.class).execute(getEText(R.id.qo_card_no));
                }
            } else if (result.code == BC.LOGIN_OUT_TIME) {
                if (BC.type != 2) {// 吉云轩版本不提示登陆过期 2016.10.10
                    UI.showError(this, getResources().getString(R.string.relogin), 3007, "");
                }
            } else {
                UI.showInfo(this, result.obj.toString());
                return;
            }
        }

    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == COMMITOK) {
            finish();
        } else if (type == 3007) {
            biz.openLoginActivity();
        }
    }

    private void commit() {
        if (getEText(R.id.qo_total_guest).equals("")) {
            UI.showError(this, R.string.msg_inputnum);
            return;
        }
        EditText et = (EditText) findViewById(R.id.lbl_ordersign);
        EditText card = (EditText) findViewById(R.id.qo_card_no);
        Bill bill = new Bill();
        bill.TableNo = row.getString("TableNo");
        bill.TableName = row.getString("TableName");
        bill.BillCode = row.getString("BillCode");
        // bill.GuestName = row.getString("GuestName");
        bill.GuestName = getEText(R.id.username);
        bill.TotalGuest = Integer.parseInt(getEText(R.id.qo_total_guest));// row.getInt("TotalGuest");
        bill.CardNo = card.getText().toString();// row.getString("CardNo");
        bill.MemNo = getEText(R.id.qo_total_guest);
        bill.TotalPayable = row.getDouble("TotalPayable");
        bill.TxDate = row.getString("TxDate");
        bill.BillShop = row.getString("BillShop");
        bill.BillTotal = row.getDouble("BillTotal");
        bill.DiscountRate = row.getDouble("DiscountRate");
        bill.RateDiscount = row.getDouble("RateDiscount");
        bill.FixedDiscount = row.getDouble("FixedDiscount");
        bill.OpenUser = row.getString("OpenUser");
        bill.BillItems = null;
        bill.OpenTime = row.getString("OpenTime");
        bill.BillStatus = row.getInt("BillStatus");
        bill.DiningRemarks = et.getText().toString();
        new MyAsyncTask(ChangeBillInfo.this, SaveBillInfo.class).run(bill);
    }

    public void submit(View view) {
        if (BC.type == 0) {//标准版消费卡号是否必填设置
            if (settings.getBoolean("inputcardno", false) && getEText(R.id.qo_card_no).equals("")) {//消费卡必填
                UI.showError(this, R.string.msg_inputcardno);
                return;
            }
        } else {
            if (getEText(R.id.qo_card_no).equals("")) {//消费卡必填
                UI.showError(this, R.string.msg_inputcardno);
                return;
            }
        }
        new MyAsyncTask(this, GetBillLock.class).execute(S.token, getEText(R.id.bill_code));
    }
}