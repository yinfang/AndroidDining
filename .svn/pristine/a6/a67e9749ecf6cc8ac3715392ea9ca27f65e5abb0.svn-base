package com.clubank.device;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.ESubmitType;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.util.UI;

public class SingleDiscountActivity extends OrderActionActivity {
    private int discountType = BC.DISCOUNT_TYPE_BY_RATE;
    private EditText et;

    private BillItemInfo billItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_discount);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            BillItemInfo bIInfo = (BillItemInfo) bundle.getSerializable("singleBill");
            if (bIInfo != null) {
                setEText(R.id.tv_bill_info,"菜名: "+bIInfo.ItemName+" 数量: "+((int)bIInfo.Quantity));
                billItem = S.bill.getItem(bIInfo.ItemCode, ESubmitType.NotSubmitted);
                if (billItem == null){
                    billItem = new BillItemInfo();
                }
            }
        }
        et = (EditText) findViewById(R.id.discount_value);
//        setEText(R.id.bill_total, "" + S.bill.TotalPayable);//总金额
//        setEText(R.id.total_payable, "" + S.bill.TotalPayable);//折后金额
        if (billItem.FixedDiscount > 0){
            RadioButton byFix = (RadioButton) findViewById(R.id.by_fixed);
            byFix.setChecked(true);
            changeDiscountType(byFix);
        }else {
            discountType = BC.DISCOUNT_TYPE_BY_RATE;
        }
        setEText(R.id.bill_total, "" + billItem.ItemPrice * billItem.Quantity);//总金额
        setEText(R.id.total_payable, "" + billItem.ItemPrice * billItem.Quantity);//折后金额
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
            double v = billItem.ItemPrice * billItem.Quantity;
            if (discountType == BC.DISCOUNT_TYPE_BY_RATE) {

                if (settings.getBoolean("disaccountint", false)) {//取整,小数点后有大于0的数就加1
                    v = v * d;
                    double yu = v % 100;
                    v = (v - yu) / 100;
                    if (yu > 0) {
                        v = v + 1;
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
        setEText(R.id.total_payable, "" + billItem.ItemPrice * billItem.Quantity);
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
//        if (S.modified) {
//        Intent intent = new Intent();
//        intent.putExtra("discountType", discountType);
//        intent.putExtra("value", d);
//        setResult(RESULT_OK, intent);
        if (discountType == BC.DISCOUNT_TYPE_BY_RATE){
            billItem.DiscountRateCache = d;
            billItem.FixedDiscountCache = 0;
        }else if (discountType == BC.DISCOUNT_TYPE_BY_FIXED){
            billItem.FixedDiscountCache = d;
            billItem.DiscountRateCache = 100;
        }
        billItem.hasChangeSingleDiscount = true;
        finish();
//        } else {
//            new MyAsyncTask(this, BillDiscount.class).execute(
//                    S.bill.BillCode, discountType, d, S.userCode);
//        }

    }

//    @Override
//    public void onPostExecute(Class<?> op, Result result) {
//        super.onPostExecute(op, result);
//        if (op == BillDiscount.class) {
//            if (result.code == BC.RESULT_SUCCESS) {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            } else if (result.code == BC.RESULT_INSUFFICIENT_PREVILEGE) {
//                UI.showError(this, R.string.insufficient_previlege);
//            }
//        }
//    }
}
