package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.clubank.api.PosApi;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.CheckoutPayment;
import com.clubank.domain.OnlinePayInfo;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.PrintBill;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.RxNetworking;
import com.clubank.util.UI;
import com.clubank.util.WaitingDialog;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

/**
 * 消费结账
 */
public class CheckoutPaymentActivity extends BaseActivity {
    private static final int REQUEST_PAY_FINISH = 1;
    private static final int PAY_CANCEL_TIP = 3;
    private static final int REQUEST_PAY_FINISH_BACK = 5;
    String billcode = "";
    private int payWay = 0;//银联 0，微信1，支付宝2，挂账3，现金4
    private double totalPrice;
    private boolean isGuaBill = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment);
        hide(R.id.header_menu_scan);
        Intent it = getIntent();
        billcode = it.getStringExtra("billcode");
        totalPrice = getIntent().getDoubleExtra("prices", 0);
        setEText(R.id.tv_total, new java.text.DecimalFormat("#.###").format(totalPrice));
        if (settings.getBoolean("weipay", false)) {//开启微信结账功能
            show(R.id.radio_wechat);
            show(R.id.wei_line);
        }
        if (settings.getBoolean("alipay", false)) {//开启支付宝结账功能
            show(R.id.radio_alipay);
            show(R.id.ali_line);
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radgroup_payment);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_ylpay://银联的支付宝微信支付
                        payWay = 0;
                        changeChecked(R.id.radio_ylpay);
                        break;
                    case R.id.radio_wechat:
                        changeChecked(R.id.radio_wechat);
                        payWay = 1;
                        break;
                    case R.id.radio_alipay:
                        changeChecked(R.id.radio_alipay);
                        payWay = 2;
                        break;
                    case R.id.radio_post://会员挂账
                        changeChecked(R.id.radio_post);
                        payWay = 3;
                        break;
                    case R.id.radio_cash://现金
                        changeChecked(R.id.radio_cash);
                        payWay = 4;
                        break;
                }
            }
        });
    }

    private void changeChecked(int id) {
        int[] ids = new int[]{R.id.radio_ylpay, R.id.radio_wechat, R.id.radio_alipay, R.id.radio_post, R.id.radio_cash};
        for (int idd : ids) {
            ((RadioButton) findViewById(idd)).setChecked(false);
        }
        ((RadioButton) findViewById(id)).setChecked(true);
    }

    public void doWork(View v) {
        if (v.getId() == R.id.btn_checkout) {
            Bundle bundle = new Bundle();
            isGuaBill = false;
            switch (payWay) {
                case 0://银联的微信和支付宝支付
                    bundle.putInt("module", 7);
                    bundle.putDouble("total", totalPrice);
                    openIntent(CheckoutPayOnlineActivity.class, "", bundle, BC.REQUEST_PAY);
                    break;
                case 1://微信
                    bundle.putInt("module", 2);
                    bundle.putDouble("total", totalPrice);
                    openIntent(CheckoutPayOnlineActivity.class, "", bundle, BC.REQUEST_PAY);
                    break;
                case 2://支付宝
                    bundle.putInt("module", 1);
                    bundle.putDouble("total", totalPrice);
                    openIntent(CheckoutPayOnlineActivity.class, "", bundle, BC.REQUEST_PAY);
                    break;
                case 3://挂账
                    isGuaBill = true;
                    Intent it = new Intent(CheckoutPaymentActivity.this, MemberAccountActivity.class);
                    it.putExtra("selectedSave", true);
                    it.putExtra("total", totalPrice);
                    it.putExtra("code", getIntent().getStringExtra("code"));//桌台号
                    it.putExtra("billcode", billcode);
                    it.putExtra("title", getString(R.string.member_account));
                    startActivityForResult(it, REQUEST_PAY_FINISH);
                    break;
                case 4://现金
                    OnlinePayInfo payInfo = new OnlinePayInfo();
                    payInfo.module = 10;
                    checkoutFrontBill(payInfo);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BC.REQUEST_PAY) {
            if (resultCode == RESULT_OK) {
                OnlinePayInfo payInfo = (OnlinePayInfo) data.getSerializableExtra("payInfo");
                checkoutFrontBill(payInfo);
            }
        } else if (requestCode == REQUEST_PAY_FINISH) {
            if (resultCode == RESULT_OK) {
                doPrint();
            }
        } else if (requestCode == REQUEST_PAY_FINISH_BACK) {//挂账完成后点击手机返回按钮
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    public void doPrint() {
        new MyAsyncTask(this, PrintBill.class).execute(billcode, S.userCode);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == PrintBill.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showToast(this, R.string.sent_print_cmd);
                if (isGuaBill) {//挂账
                    setResult(RESULT_OK);
                    finish();
                } else {
                    openIntent(ChectoutPaySuccessfulActivity.class, getString(R.string.checkout_pay_successful), null, REQUEST_PAY_FINISH_BACK);
                }
            }
        }
    }

    private void checkoutFrontBill(OnlinePayInfo payInfo) {
        String payList = getPaylist(payInfo);
        Observable.Transformer<Result, Result> networkingIndicator
                = RxNetworking.bindConnecting(new WaitingDialog(sContext));

        PosApi.getInstance(sContext)
                .CheckoutWorkshopBill(billcode, S.shopCode, payList)
//                .compose(this.<Result>bindToLifecycle())
                .compose(networkingIndicator)
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        if (result.code == BC.RESULT_SUCCESS) {
                            UI.showToast(sContext, R.string.checkout_pay_bill_success);
                            doPrint();
                        } else {
                            UI.showToast(sContext, result.msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        UI.showToast(sContext, e.getMessage());
                    }
                });
    }

    private String getPaylist(OnlinePayInfo payInfo) {
        ArrayList<CheckoutPayment> lstCheckoutPayment = new ArrayList<>();
        CheckoutPayment checkoutPayment = new CheckoutPayment();
        checkoutPayment.PayCode = String.valueOf(payInfo.module);
        checkoutPayment.PayCard = payInfo.PayCard;
        checkoutPayment.Name = payInfo.PayCardName;
        checkoutPayment.Amount = getEText(R.id.tv_total);
        lstCheckoutPayment.add(checkoutPayment);

        Gson gson = new Gson();
        String payList = gson.toJson(lstCheckoutPayment);
        payList = encodeUrl(payList);
        return payList;
    }

    /**
     * Encoder String
     *
     * @param originalString
     * @return
     */
    public static String encodeUrl(String originalString) {
        String codeString = originalString;
        try {
            codeString = URLEncoder.encode(originalString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return codeString;
    }

    @Override
    public void back() {
        showTip();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showTip();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showTip() {
        UI.showOKCancel(this, PAY_CANCEL_TIP, R.string.pay_cancel_tip, R
                .string.msg_prompt);
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == PAY_CANCEL_TIP) {
            Intent i = new Intent(this, MainActivity.class);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }
}
