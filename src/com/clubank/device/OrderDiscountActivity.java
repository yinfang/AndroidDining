package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.BillDiscount;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.UI;

/**
 * 订单打折
 *
 * @author jishu0416
 */
public class OrderDiscountActivity extends OrderActionActivity {

    private int discountType = BC.DISCOUNT_TYPE_BY_RATE;
    private EditText et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_discount);
        et = (EditText) findViewById(R.id.discount_value);
        setEText(R.id.bill_total, "" + S.bill.TotalPayable);
        setEText(R.id.total_payable, "" + S.bill.TotalPayable);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                changeDiscountValue(s.toString());
            }
        });

    }

    public void changeDiscountValue(String s) {
        try {
            double d = Double.parseDouble(s);
            double v = S.bill.TotalPayable;
            if (discountType == BC.DISCOUNT_TYPE_BY_RATE) {

                if (settings.getBoolean("disaccountint", false)) {//取整,小数点后有大于0的数就加1
                    v = v * d ;
                    double yu = v %100;
                    v = (v -yu)/100;
                    if (yu > 0){
                        v = v+1;
                    }
                } else {
                    v = v * d / 100;
                }
            } else if (discountType == BC.DISCOUNT_TYPE_BY_FIXED) {
                v = v - d;
            }
            setEText(R.id.total_payable, "" + v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeDiscountType(View view) {
        if (view.getId() == R.id.by_rate) {
            discountType = BC.DISCOUNT_TYPE_BY_RATE;
            et.setText("100");
            show(R.id.percent);
            hide(R.id.yuan);
        } else if (view.getId() == R.id.by_fixed) {
            discountType = BC.DISCOUNT_TYPE_BY_FIXED;
            et.setText("0");
            hide(R.id.percent);
            show(R.id.yuan);
        }
        setEText(R.id.total_payable, "" + S.bill.TotalPayable);
    }

    public void submit(View view) {
        int d = 0;
        try {
            d = Integer.valueOf(et.getText().toString());
        } catch (Exception e) {

        }

        if (discountType == BC.DISCOUNT_TYPE_BY_RATE && d >= 100
                || discountType == BC.DISCOUNT_TYPE_BY_FIXED
                && (d == 0 || d > S.bill.TotalPayable)) {
            return;
        }
        if (S.modified) {
            Intent intent = new Intent();
            intent.putExtra("discountType", discountType);
            intent.putExtra("value", d);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (!BC.engineerMode){
                new MyAsyncTask(this, BillDiscount.class).execute(S.bill.BillCode, discountType, d, S.userCode);
            }
        }

    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == BillDiscount.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (result.code == BC.RESULT_INSUFFICIENT_PREVILEGE) {
                UI.showError(this, R.string.insufficient_previlege);
            }
        }
    }
}
