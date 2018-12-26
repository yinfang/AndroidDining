package com.clubank.device;
/**
 * 会员挂账
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.clubank.api.PosApi;
import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetBillByCode;
import com.clubank.util.CustomDialogView;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.RxNetworking;
import com.clubank.util.SingleClick;
import com.clubank.util.U;
import com.clubank.util.UI;
import com.clubank.util.WaitingDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observable;
import rx.functions.Action1;

public class MemberAccountActivity extends BaseActivity {
    private static final int DIALOG_ACCOUNT = 1;
    private String name, MemNo;
    private String code, billcode;
    private int guestNum = 1;
    private static final int ENTER_PEOPLE = 2;
    private Button confirmSubmitView;
    private String data;
    private String type;
    private MyRow row;
    private MyData oneDish = new MyData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_account);
        confirmSubmitView = (Button) findViewById(R.id.btn_confirm);
        menus = new String[]{"人数"};
        Intent it = getIntent();
//        type = it.getStringExtra("type");
        code = it.getStringExtra("code");//桌台号
        billcode = it.getStringExtra("billcode");//订单号
       /* if (type.equals("2")) {//订单详情进入，安卓点餐暂无入口
            row = U.getRow(getIntent().getExtras(), "billRow");
            setEText(R.id.tv_total, row.getString("TotalPayable"));
            if (!row.getString("MemNo").isEmpty()) {
                setEText(R.id.tv_member_name, row.getString("GuestName"));
                setEText(R.id.tv_member_number, row.getString("MemNo"));
                getMemberPhoto(row.getString("MemNo"));
            }
        } else {*/
        setEText(R.id.tv_total, new java.text.DecimalFormat("#.###").format(getIntent().getDoubleExtra("total", 0)));
        if (!TextUtils.isEmpty(billcode)) {
            new MyAsyncTask(this, GetBillByCode.class).execute(billcode);
        }
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetBillByCode.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                S.modified = false;
                MyRow row = (MyRow) result.obj;
                oneDish = (MyData) row.get("BillItems");
            }
        }
    }

    @Override
    public void menuSelected(View view, int index) {
        if (index == 0) {
            showCustomDialog(ENTER_PEOPLE,
                    getString(R.string.lbl_enter_people), R.layout.server_url_item,
                    -1);
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
        if (type == ENTER_PEOPLE) {
            EditText et = (EditText) view.findViewById(R.id.input);
            et.setText("1");
        }
    }

    protected boolean finishDialog(int type, Dialog dialog) {
        if (type == ENTER_PEOPLE) {
            EditText et = (EditText) dialog.findViewById(R.id.input);
            guestNum = Integer.parseInt(et.getText().toString());
        }
        return true;
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showMenu(v, 4, 4, menus);
                break;
            case R.id.btn_choose_member:
                chooseMember();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
        }
    }

    private void confirm() {
        //重复点击大于2秒方可通过
        if (SingleClick.isFastDoubleClick()) {
            // confirmSubmitView.setBackgroundColor(Color.parseColor("#828282"));
//            showToast("点击太快啦");
            return;
        } else {
//            if (type.equals("1")) {
            confirmSubmitView.setBackgroundColor(Color.parseColor("#828282"));
            String list = getItemList();
            if (list.length() > 0) {
                Observable.Transformer<Result, Result> networkingIndicator
                        = RxNetworking.bindConnecting(new WaitingDialog(this));

                PosApi.getInstance(this)
                        .PostBillViaMemNo(MemNo,
                                S.shopCode,
                                String.valueOf(guestNum),
                                list, code, "")
//                            .compose(this.<Result>bindToLifecycle())
                        .compose(networkingIndicator)
                        .subscribe(new Action1<Result>() {
                            @Override
                            public void call(Result result) {
                                if (result.code == BC.RESULT_SUCCESS) {
                                    MyRow row = result.data.get(0);
                                    data = row.getString("data");
                                    GoSignature();
                                } else {
                                    UI.showError(sContext, result.msg);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable e) {
                                UI.showError(sContext, e.getMessage());
                            }
                        });
            } else {
                UI.showError(this, R.string.msg_filtered);
                return;
            }
//            } else if (type.equals("2")) {
//                PostBillByMemNo();
//            }
        }
    }

    private void GoSignature() {
        UI.showToast(sContext, "挂账成功，请签名！");
        Intent i = new Intent(MemberAccountActivity.this, SignatureActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("data", data);
        startActivityForResult(i, RESULT_OK);
        finish();
    }

    /**
     * 取得商品列表json格式
     * [{"ItemCode":"123","Name":"雪碧","Quantity":"2","Price":"15"}]
     *
     * @return
     */
    private String getItemList() {
        String list;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (MyRow row : oneDish) {
            double quantity = row.getDouble("Quantity");
            if (quantity > 0 || quantity != 0) {

                String str = String.format("{\"ItemCode\":\"%1$s\",\"Name\":\"%2$s\",\"Quantity\":\"%3$s\",\"Price\":\"%4$s\"}",
                        row.getString("ItemCode"),
                        row.getString("ItemName"),
                        String.valueOf(quantity),
                        String.valueOf(row.getDouble("TotalPayable") / quantity));
                sb.append(str);
                sb.append(",");
            }
        }
        //去逗号
        if (sb.length() > 2) {
            list = sb.substring(0, sb.length() - 1) + "]";
            list = encodeUrl(list);
        } else {
            list = "";
        }
        return list;
    }

    private void chooseMember() {
        Intent i = new Intent(this, MemCardListActivity.class);
        i.putExtra("MemNo", "");
        startActivityForResult(i, BC.REQUEST_CHOOSE_MEMBER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BC.REQUEST_CHOOSE_MEMBER) {
            if (resultCode == RESULT_OK) {
                name = data.getStringExtra("name");
                MemNo = data.getStringExtra("MemNo");
                setEText(R.id.tv_member_number, MemNo);
                setEText(R.id.tv_member_name, name);
                byte[] b = data.getByteArrayExtra("MemPicture");
                if (b != null) {
                    setImage(R.id.member_image, b, true);
                }
                PosApi.getInstance(this)
                        .GetMemberSignPhoto(MemNo)
//                        .compose(this.<Result>bindToLifecycle())
                        .subscribe(new Action1<Result>() {
                            @Override
                            public void call(Result result) {
                                if (result.code == 0) {
                                    MyRow row = result.data.get(0);
                                    ImageView member_sign = (ImageView) findViewById(R.id.member_sign);
                                    if (TextUtils.isEmpty(row.getString("data"))) {
                                        member_sign.setImageResource(R.drawable.signature_photo);
                                    } else {
                                        member_sign.setImageBitmap(U.base64ToBitmap(row.getString("data")));
                                    }
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable e) {
                                //UI.Toast(mContext, e.getMessage());
                            }
                        });
            }
        }
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
}
