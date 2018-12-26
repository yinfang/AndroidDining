package com.clubank.device;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.ESubmitType;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.BillDeliverDish;
import com.clubank.op.CallUpDish;
import com.clubank.op.DeleteBillLock;
import com.clubank.op.DeliverDish;
import com.clubank.op.GetBillByCode;
import com.clubank.op.GetPackageChangeItem;
import com.clubank.op.PrintDish;
import com.clubank.op.RemindDish;
import com.clubank.op.SetDelivered;
import com.clubank.op.WithdrawDish;
import com.clubank.util.CustomDialogView;
import com.clubank.util.CustomDialogView.Initializer;
import com.clubank.util.CustomDialogView.OKProcessor;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author 下单界面
 */
public class OrderOrderedActivity extends OrderActionActivity implements OnItemClickListener {

    private int CONTEXT_MENU_ID = 1;
    private int DLG_WITHDRAW = 11;
    private int DLG_CONFIRM_ABORT = 12;
    private int DLG_PRINT_DISH = 13;
    private int DLG_PRINT_DISH_SUCCESS = 14;
    // private static final int REQUEST_ITEM_DISCOUNT = 102;
    private int REQUEST_BILL_DISCOUNT = 103;
    // private static final int REQUEST_CUSTOM_DISH = 104;
    private int REQUEST_SUBMIT_CONFIRM = 200;
    private int MENU_CHANGE_NAME_PRICE = 1;
    private int MENU_CHANGE_QUANTITY = 2;
    private final int MENU_CANCEL_DISH = 3;
    private final int MENU_ADD_DISH = 4;
    private final int MENU_REMIND_DISH = 5;
    private final int MENU_SET_DELIVERED = 6;
    private final int MENU_WITHDRAW_DISH = 7;
    private final int MENU_COOK_DISHES = 9;
    private final int MENU_COOK_ALLDISHES = 23;
    private int CHANGE_DISH_PACKEAGE = 24;
    // private static final int MENU_DISCOUNT = 8;
    private IconContextMenu m;
    private ListView listView;
    private BillItemAdapter sa;
    private int pos;
    public boolean printMode;
    private String saleIds;
    private String from;
    private int changagePos = -1;
    private MyData packageChange;
    private double discountRate = 100.00;
    private double fixedDiscount = 0.00;
    private int BillStatus = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordered);
        from = getIntent().getStringExtra("from");
        setEText(R.id.msg_dish_total, S.bill.DishTotal + "");// 菜品数量
        // keepInHistory = true;
        listView = (ListView) findViewById(R.id.lvw_ordered);

        sa = new BillItemAdapter(this, R.layout.lvi_ordered, S.bill.BillItems);
        listView.setAdapter(sa);
        listView.setOnItemLongClickListener(itemLongClickHandler);
        listView.setOnItemClickListener(this);
        findViewById(R.id.header_home).setVisibility(View.VISIBLE);
        findViewById(R.id.header_menu).setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(from) && from.equals("BillList")) {
            hide(R.id.bottom_layout);
            hide(R.id.actionbar);
            int BillStatus = getIntent().getIntExtra("BillStatus", -1);
            if (BillStatus == 2) {//已结
                menus = new String[]{"menu_refresh", "menu_print_dish"};

            } else {
                menus = new String[]{"menu_refresh", "menu_print_dish", "menu_discount"};
            }
        } else {
            menus = new String[]{"menu_refresh", "menu_print_dish", "menu_discount"};
        }


        if (S.bill != null && !TextUtils.isEmpty(S.bill.BillCode)) {
            boolean hasnew = false;
            for (int i = 0; i < S.bill.BillItems.size(); i++) {
                BillItemInfo b = S.bill.BillItems.get(i);
                if (b.SaleID == 0){
                    hasnew = true;
                    break;
                }
            }
            if (!hasnew){
                if (!BC.engineerMode){
                    new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
                }
            }
        }
        showData();
    }

	/*
     * public void selectma(View view) { parentview = (View) view.getParent();
	 * Intent i = new Intent(this, SelectMa.class); Bundle b = new Bundle();
	 * SerializableMyData data = new SerializableMyData();
	 * data.setData(cookingFlavors); b.putSerializable("cookingFlavors", data);
	 *
	 *
	 * TextView tv = (TextView) parentview.findViewById(R.id.cooking_flavor);
	 * String tvtext = tv.getText().toString(); String[] ss = tvtext.split(",");
	 * for (int j = 0; j < ss.length; j++) { for (int j2 = 0; j2 <
	 * cookingFlavors.size(); j2++) {
	 * if(ss[j].equals(cookingFlavors.get(j2).getString("Name"))){ MyRow row =
	 * new MyRow(); row.put("Name", cookingFlavors.get(j2).getString("Name"));
	 * row.put("", cookingFlavors.get(j2).getString("Code"));
	 *
	 *
	 * } } }
	 *
	 *
	 * i.putExtra("flavorBundle", b); startActivityForResult(i, 12345); }
	 */

    @Override
    protected void onStart() {
        if (S.bill.BillItems.size() > 0 || !S.bill.BillItems.isEmpty()) {
            String isShow2 = settings.getString("isShow2", "false");
            if (isShow2.equals("false")) {
                findViewById(R.id.tip).setVisibility(View.VISIBLE);
            }
        }
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        // showData();
        // 账单查询跳转账单明细隐藏所有功能仅供展示
        if (!TextUtils.isEmpty(from) && from.equals("BillList")) {
            hide(R.id.bottom_layout);
            hide(R.id.actionbar);
        }

    }

    @Override
    public void goHome(View view) {
        if (S.modified) {// 放弃下单时提示 确定退回到主界面
            UI.showOKCancel(this, DLG_CONFIRM_HOME, R.string.msg_abandon_bill, R.string
                    .lbl_confirm);
        } else {
            goHome();
        }
    }

    /**
     * 返回主界面时解锁
     */
    public void goHome() {
        if (!BC.engineerMode){
            new MyAsyncTask(this, DeleteBillLock.class).execute(S.token, S.bill.BillCode);
        }
        S.bill.clearAll();
        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!BC.engineerMode){
            new MyAsyncTask(this, DeleteBillLock.class).execute(S.token, S.bill.BillCode);
        }

        super.onBackPressed();
    }

    @Override
    public void back(View view) {

        if (!BC.engineerMode){
            new MyAsyncTask(this, DeleteBillLock.class, false).execute(S.token, S.bill.BillCode);
        }

        super.back(view);
    }

    @Override
    public void back() {
        if (!BC.engineerMode){
            new MyAsyncTask(this, DeleteBillLock.class, false).execute(S.token, S.bill.BillCode);
        }
        super.back();
    }

    protected void onDestroy() {

        if (TextUtils.isEmpty(from)) {
            if (!BC.engineerMode){
                new MyAsyncTask(this, DeleteBillLock.class, false).execute(S.token, S.bill.BillCode);
            }
        }
        super.onDestroy();
    }

    @Override
    public void menuSelected(View view, int index) {
        if (index == 0) {// 刷新
            if (S.bill.BillCode != null) {
                if (S.modified) {
                    UI.showOKCancel(this, DLG_CONFIRM_ABORT, R.string.msg_abandon_bill, R.string
                            .lbl_confirm);
                } else {
                    if (!BC.engineerMode){
                        new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
                    }
                }
            }
        } else if (index == 1) {// 打印菜品，显示详情
            boolean submitted = false;
            for (BillItemInfo bi : S.bill.BillItems) {
                if (bi.SaleID > 0) {
                    submitted = true;
                    break;
                }
            }
            if (!submitted) {
                UI.showError(this, R.string.msg_select_dish_required);
                return;
            }

            printMode = true;
            showData();

        } else if (index == 2) {// 订单打折
//            if (S.modified) {
//                UI.showInfo(this, R.string.save_bill_first);
//                return;
//            } else if (!S.AllowDiscount) {
//                UI.showInfo(this, R.string.shouldnot_discount);
//                return;
//            } else if (discountRate < 100.00 || fixedDiscount > 0) {
//                UI.showInfo(this, getString(R.string.hasdiscount));
//                return;
//            }
//
            if (S.modified) {
                UI.showInfo(this, R.string.save_bill_first);
                return;
            } else if (!S.AllowDiscount) {
                UI.showInfo(this, R.string.shouldnot_discount);
                return;
            } else if (discountRate < 100.00 || fixedDiscount > 0) {
                UI.showInfo(this, getString(R.string.hasdiscount));
                return;
            } else if (BillStatus == 2) {
                UI.showInfo(this, "已结账,不能打折");
                return;
            }

//            int Count = listView.getChildCount();
//            for (int i = 0; i < Count; i++) {
//                View v = listView.getChildAt(i);
//                CheckBox c = (CheckBox) v.findViewById(R.id.chkPrint);
//                if (c.isChecked()) {
//                    if (saleIds.length() > 0) {
//                        saleIds += ",";
//                    }
//                    saleIds += S.bill.BillItems.get(i).SaleID;
//                }
//            }
            Intent intent = new Intent(this, OrderDiscountActivity.class);
            startActivityForResult(intent, REQUEST_BILL_DISCOUNT);
        }
    }

    // 重打
    public void printDish(View view) {
        saleIds = getSelectedItem();
        if (saleIds == null && saleIds.equals("")) {
            UI.showError(OrderOrderedActivity.this, R.string.lbl_orderDish);
            return;
        }
        UI.showOKCancel(OrderOrderedActivity.this, DLG_PRINT_DISH, R.string.lbl_confirm_print_dish,
                R.string.menu_print_dish);
    }

    public void cancelPrintDish(View view) {
        printMode = false;
        showData();
    }

    private void showData() {
        /* 安吉轩加菜在原有菜品上加数量，不在界面显示新加的菜，需要重新组合显示数据 */
        // if (BC.type == 2) {
        // int n = S.bill.BillItems.size();
        // for (int i = 0; i < n; i++) {
        // BillItemInfo item = S.bill.BillItems.get(i);
        // for (int j = i + 1; j < n; j++) {
        // if (item.ItemCode == S.bill.BillItems.get(j).ItemCode) {
        // item.Quantity += S.bill.BillItems.get(j).Quantity;
        // item.TotalPayable += S.bill.BillItems.get(j).TotalPayable;
        // S.bill.BillItems.remove(j);
        // n = S.bill.BillItems.size();
        // }
        // }
        // }
        // }

        setEText(R.id.billCode, S.bill.BillCode);
        setEText(R.id.ordered_total_payable, BC.nf_a.format(S.bill.TotalPayable));
        sa.notifyDataSetChanged();
        if (S.bill.BillItems.size() > 0 && S.bill.BillStatus == 0 && !printMode) {
            // has details and not charged
            findViewById(R.id.submit_confirm).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.submit_confirm).setVisibility(View.GONE);
        }
        if (S.bill.BillCode != null && !S.modified && S.bill.BillStatus == 0 && !printMode) {//已下单，未结账
            if (settings.getBoolean("jiezhang", false)) {//开启结账功能
                findViewById(R.id.pay_bill).setVisibility(View.VISIBLE);
            }
            if (settings.getBoolean("jizhang", false)) {//开启记账功能
                findViewById(R.id.charge_bill).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.callupbutton).setVisibility(View.GONE);
        } else {
            findViewById(R.id.pay_bill).setVisibility(View.GONE);
            // 记账 屏蔽
            findViewById(R.id.charge_bill).setVisibility(View.GONE);
            findViewById(R.id.callupbutton).setVisibility(View.VISIBLE);
        }
        if (printMode) {
            findViewById(R.id.buttonPrint).setVisibility(View.VISIBLE);// 重打按钮
            findViewById(R.id.cancelPrint).setVisibility(View.VISIBLE);// cancel按钮
            findViewById(R.id.callupbutton).setVisibility(View.GONE);
            findViewById(R.id.up).setVisibility(View.GONE);
        } else {
            findViewById(R.id.buttonPrint).setVisibility(View.GONE);
            findViewById(R.id.cancelPrint).setVisibility(View.GONE);
        }

        if (S.bill.DishTotal != 0.0) {
            setEText(R.id.msg_dish_total, S.bill.DishTotal + "");// 菜品数量
        } else {
            // 修改菜品价格
            double y = 0;// 菜品总数
            int size = S.bill.BillItems.size();
            for (int i = 0; i < size; i++) {
                y += S.bill.BillItems.get(i).Quantity;
            }
            setEText(R.id.msg_dish_total, y + "");
        }

        boolean ifcallup = false;
        // 有一个菜被叫起就要有起菜
        int size = S.bill.BillItems.size();
        for (int i = 0; i < size; i++) {
            if (S.bill.BillItems.get(i).IsCallUp) {
                ifcallup = true;
                break;
            }
        }
        if (ifcallup && S.bill.BillCode != null && !S.bill.BillCode.equals("")) {
            findViewById(R.id.up).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.up).setVisibility(View.GONE);
        }
    }

    private CallupPopup callpopup;

    /**
     * 叫起
     *
     * @param v
     */
    public void callUp(View v) {
        callpopup = new CallupPopup(OrderOrderedActivity.this, true, callupclick);
        callpopup.showAtLocation(OrderOrderedActivity.this.findViewById(R.id.button_band),
                Gravity.CENTER, 0, 0);
    }

    private boolean ALLCALLUP;
    private OnClickListener callupclick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            ALLCALLUP = false; // 整单是否被选
            callUpCode = new ArrayList<String>();
            int cont = 0;
            int size = S.bill.BillItems.size();
            for (int i = 0; i < size; i++) {
                if (S.bill.BillItems.get(i).IsCallUp) {
                    callUpCode.add(S.bill.BillItems.get(i).ItemCode);
                } else {
                    cont++;
                }
            }
            if (cont != 0) {
                ALLCALLUP = false;
            } else {
                ALLCALLUP = true;
            }
            callpopup.dismiss();
            showData();
        }
    };

    public void up(View v) {
        callpopup = new CallupPopup(this, false, upclick);
        callpopup.showAtLocation(OrderOrderedActivity.this.findViewById(R.id.button_band),
                Gravity.CENTER, 0, 0);
    }

    private List<String> upCode;
    private int upsize;
    private OnClickListener upclick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            boolean AllUp = false;
            int cont = 0;
            int size = S.bill.BillItems.size();
            for (int i = 0; i < size; i++) {
                if (S.bill.BillItems.get(i).IsUp) {
                } else {
                    cont++;
                }
            }
            if (cont != 0) {
                AllUp = false;
            } else {
                AllUp = true;
            }
            /*
             * if (AllUp) { // 整单起菜 new MyAsyncTask(OrderOrderedActivity.this,
			 * BillDeliverDish.class).run(S.bill.BillCode); AllUp = false; }
			 * else {
			 */
            if (S.bill.BillItems != null && S.bill.BillItems.size() != 0) {
                upsize = 0;
                upCode = new ArrayList<String>();
                int size1 = S.bill.BillItems.size();
                for (int i = 0; i < size1; i++) {
                    if (S.bill.BillItems.get(i).IsUp) {
                        upCode.add(S.bill.BillItems.get(i).SaleID + "");
                    }
                }
                int codeSize = upCode.size();
                for (int j = 0; j < codeSize; j++) {
                    if (!BC.engineerMode){
                        new MyAsyncTask(OrderOrderedActivity.this, DeliverDish.class).execute(Integer.parseInt(upCode.get(j)));
                    }
                }
            }
            // /}
            callpopup.dismiss();
            showData();
        }
    };

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == WithdrawDish.class) {
            if (result.code == BC.RESULT_SUCCESS) {

                UI.showInfo(this, R.string.msg_withdraw_dish_success, DLG_CONFIRM_ABORT);

				/*
                 * int qty = (Integer) result.obj; if (qty ==
				 * S.bill.BillItems.get(pos).Quantity) { S.bill.removeItem(pos);
				 * } else { S.bill.BillItems.get(pos).Quantity -= qty; }
				 * setEText(R.id.ordered_total_payable,
				 * BC.nf_a.format(S.bill.TotalPayable)); showData();
				 */
            } else if (result.code == 2) {// 退菜数量不正确或其他不符合条件的
                UI.showInfo(this, R.string.msg_food_back);
            }
        } else if (op == RemindDish.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_remind_dish_success);
            }
        } else if (op == PrintDish.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_print_dish_success, DLG_PRINT_DISH_SUCCESS);
            }
        } else if (op == SetDelivered.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyRow row = (MyRow) result.obj;
                boolean scanFlag = row.getBoolean("scanFlag");
                if (!scanFlag) {
                    /* not scanned sale id */
                    UI.showInfo(this, R.string.msg_operation_success);
                    S.bill.BillItems.get(pos).Delivered = !S.bill.BillItems.get(pos).Delivered;
                    showData();
                }
            }
        } else if (op == GetBillByCode.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                S.modified = false;
                MyRow row = (MyRow) result.obj;
                double DiscountRate = row.getDouble("DiscountRate");
                double FixedDiscount = row.getDouble("FixedDiscount");
                BillStatus = row.getInt("BillStatus");
                this.discountRate = DiscountRate;
                this.fixedDiscount = FixedDiscount;
                MyData items = (MyData) row.get("BillItems");
                if (items != null && items.size() > 0) {
                    int size = items.size();
                    for (int i = 0; i < size; i++) {
                        // if (S.bill.BillItems.get(i).IsCallUp)
                        if (S.bill.BillItems.size() > i
                                && S.bill.BillItems.get(i).SaleID == items.get(i).getInt("SaleID")
                                && S.bill.BillItems.get(i).IsCallUp)
                            items.get(i).put("IsCallUp", true);
                    }
                    for (int i = 0; i < size; i++) {
                        // if (S.bill.BillItems.get(i).IsUp)
                        if (S.bill.BillItems.size() > i
                                && S.bill.BillItems.get(i).SaleID == items.get(i).getInt("SaleID")
                                && S.bill.BillItems.get(i).IsUp)
                            items.get(i).put("IsUp", true);
                    }
                    row.put("PrintDish", S.bill.PrintDish);

                }
                BU.fillBillItems(items);
                BU.fillBill(row);

                showData();
                /*
                 * if (!ALLCALLUP) { // 如果全部叫起就不用走这个 for (int i = 0; i <
				 * S.bill.BillItems.size(); i++) { if
				 * (S.bill.BillItems.get(i).IsCallUp) { new MyAsyncTask(this,
				 * CallUpDish.class, true) .run(items.get(i).getInt("SaleID"));
				 * } } }
				 */

            }
            // feng
        } else if (op == DeliverDish.class) {
            upsize++;
            if (result.code == BC.RESULT_SUCCESS) {
                if (upsize == upCode.size()) {
                    UI.showInfo(this, R.string.msg_deliver_dish_success);
                    if (!BC.engineerMode){

                        new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
                    }
                }
            } else {
                UI.showError(this, R.string.msg_deliver_dish_fail);
            }
        } else if (op == CallUpDish.class) { // 获取到订单，循环上传叫起的菜，叫起完成后再刷新数据
            if (result.code == BC.RESULT_SUCCESS) {
            } else {
                UI.showError(this, R.string.lbl_callupfail);
            }
        } else if (op == BillDeliverDish.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_deliver_dish_success);
            } else {
                UI.showError(this, R.string.msg_deliver_dish_fail);
            }
        } else if (op == DeleteBillLock.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                // super.back();
            } else if (result.code == BC.LOGIN_OUT_TIME) {
                if (BC.type != 2) {// 吉云轩版本不提示登陆过期 2016.10.10
                    UI.showError(this, getResources().getString(R.string.relogin), 3007, "");
                }
            }
        } else if (op == GetPackageChangeItem.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                packageChange = (MyData) result.obj;
                String[] packageChageNameA = new String[packageChange.size()];
                for (int i = 0; i < packageChange.size(); i++) {
                    packageChageNameA[i] = packageChange.get(i).getString("ItemName");
                }
                showListDialog(findViewById(R.id.ordered_total_payable), packageChageNameA);
            } else {
                UI.showError(this, R.string.msg_deliver_dish_fail);
            }
        }
    }

    private OnItemLongClickListener itemLongClickHandler = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


            final BillItemInfo bi = S.bill.BillItems.get(position);
            if (!TextUtils.isEmpty(from) && from.equals("BillList") || bi.ComposeType == 3 && bi
                    .IfPackage == 2) {//套餐明细替换)
                return true;
            } else {
                pos = position;

                if (bi.SaleID == 0) {
                    Dialog d = createDialog(CONTEXT_MENU_ID, pos);
                    if (d != null) {
                        d.show();
                    }
                }
                return true;
            }


        }
    };

    private List<String> callUpCode;

    // 开始下单
    public void submitConfirm(View view) {
        if (BC.engineerMode){
            UI.showInfo(this,R.string.engineer_mode_do_not_support);
            return;
        }

        if (S.bill.BillStatus != 0) {
            UI.showInfo(this, R.string.submitorder);// 账单已记账
            return;
        }
        /**
         * 判断是否有菜品的ItemCode为空，为空不能下单
         */
        for (int i = 0; i < S.bill.BillItems.size(); i++) {
            if (S.bill.BillItems.get(i).ItemCode.isEmpty()) {
                String ItemName = S.bill.BillItems.get(i).ItemName;
                if (ItemName.isEmpty()) {
                    UI.showInfo(this, "有菜品的Code为空");
                } else {
                    UI.showInfo(this, S.bill.BillItems.get(i).ItemName);
                }
                return;
            }
        }
        if (settings.getBoolean("popupCookWindow", false)) {//做法必选
            for (int i = 0; i < S.bill.BillItems.size(); i++) {
                if (S.bill.BillItems.get(i).IfStyle && S.bill.BillItems.get(i).CookingStyle == 0) {//做法必选但未选，提示
                    String itemName = S.bill.BillItems.get(i).ItemName;
                    UI.showInfo(this, "请选择" + itemName + "的做法!");
                    return;
                }
            }
        }

        Intent it = new Intent(this, SubmitConfirmActivity.class);
        it.putExtra("title", getText(R.string.menu_submit_order));

        if (S.modified) {
            if (discountRate < 100) {
                for (int i = 0; i < S.bill.BillItems.size(); i++) {
                    S.bill.BillItems.get(i).DiscountRate = discountRate;
                }
            } else if (fixedDiscount > 0) {
                S.bill.BillTotal = S.bill.BillTotal - fixedDiscount;
            }
        }
        /*
		 * callUpCode = new ArrayList<String>(); for (int i = 0; i <
		 * S.bill.BillItems.size(); i++) { if (S.bill.BillItems.get(i).IsCallUp)
		 * { callUpCode.add(S.bill.BillItems.get(i).ItemCode); } } int cont = 0;
		 * for (int i = 0; i < S.bill.BillItems.size(); i++) { if
		 * (S.bill.BillItems.get(i).IsCallUp) { cont++; } } if
		 * (callUpCode.size() < cont) { ALLCALLUP = false; } else { ALLCALLUP =
		 * true; }
		 */
        if (from.equals("quickOpen")) {
            it.putExtra("from", "quickOpen");
            it.putExtra("ALLCALLUP", ALLCALLUP);
            startActivityForResult(it, REQUEST_SUBMIT_CONFIRM);
        } else {
            it.putExtra("from", "");
            it.putExtra("ALLCALLUP", ALLCALLUP);
            startActivityForResult(it, REQUEST_SUBMIT_CONFIRM);
        }

    }

    // 记账
    public void chargeBill(View view) {
        if (!S.AllowCreditToCard) {
            UI.showInfo(this, R.string.shouldnot_remember);
            return;
        }
        Intent it = new Intent(this, ChargeBillActivity.class);
        it.putExtra("title", getText(R.string.msg_action_chargeOrder));
        startActivityForResult(it, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //码选
        if (requestCode == 12345) {
            if (data != null) {
                String flavor = data.getStringExtra("flavor");
                String flavorCode = data.getStringExtra("flavorCode");
                TextView tv = (TextView) parentview.findViewById(R.id.cooking_flavor);
                tv.setText(flavor);
                flavorString = flavorCode;
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_BILL_DISCOUNT) {
                if (S.modified) {
//                    intent.putExtra("discountType", discountType);
//                    intent.putExtra("value", d);
                    int discountType = data.getIntExtra("discountType", BC.DISCOUNT_TYPE_BY_RATE);
                    int value;
                    if (discountType == BC.DISCOUNT_TYPE_BY_RATE) {
                        value = data.getIntExtra("value", 100);
                        S.bill.DiscountRate = (double) value;
//                        S.bill.FixedDiscount = S.bill.BillTotal * (S.bill.DiscountRate * 0.01);

                    } else if (discountType == BC.DISCOUNT_TYPE_BY_FIXED) {
                        value = data.getIntExtra("value", 0);
                        S.bill.FixedDiscount = (double) value;
//                        S.bill.DiscountRate = S.bill.FixedDiscount / S.bill.BillTotal *100;
                    }
                } else {
                    if (!BC.engineerMode){

                        new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
                    }
                }
                // } else if (requestCode == REQUEST_CUSTOM_DISH) {
                // Bundle b = data.getExtras();
                // BillItemInfo bi = new BillItemInfo();
                // bi.ItemName = b.getString("itemName");
                // bi.SalePrice = b.getDouble("price");
                // bi.Quantity = b.getDouble("qty");
                // bi.SaleTotal = bi.SalePrice * bi.Quantity;
                // bi.TotalPayable = bi.SaleTotal;
                // BU.addBillItem(bi);
                // S.modified = true;
                // sa.notifyDataSetChanged();

                // } else if (requestCode == REQUEST_ITEM_DISCOUNT) {
                // Bundle b = data.getExtras();
                // int i = b.getInt("position");
                // BillItemInfo bi1 = (BillItemInfo)
                // b.getSerializable("billItem");
                // if (bi1.TotalPayable == bi1.SaleTotal) {
                // return;
                // }
                // S.bill.BillItems.get(i).SeparateDiscount = true;
                // S.bill.BillItems.get(i).DiscountRate = bi1.DiscountRate;
                // S.bill.BillItems.get(i).RateDiscount = bi1.RateDiscount;
                // S.bill.BillItems.get(i).FixedDiscount = bi1.FixedDiscount;
                // S.bill.BillItems.get(i).TotalPayable = bi1.TotalPayable;
                // S.bill.calcTotal();
                // S.modified = true;
                // TextView st = (TextView)
                // findViewById(R.id.ordered_total_payable);
                // st.setText("" + S.bill.TotalPayable);
                // showData();
            } else if (requestCode == REQUEST_SUBMIT_CONFIRM) { //
                // 下单完成后，获取订单详情，刷新下单界面
                if (!BC.engineerMode){

                    new MyAsyncTask(this, GetBillByCode.class, false).execute(S.bill.BillCode);
                }
                back();
            }
        }
        if (requestCode == 11 && resultCode == RESULT_OK) {
            back();
        }

        if (requestCode == 23456) {
            if (settings.getBoolean("kitchentype", BC.showKitchenType)) {
                if (data != null) {
                    String cookieType = data.getStringExtra("kitchenTypeName");
                    TextView textView = (TextView) parentview.findViewById(R.id.tv_cooking_type);
                    textView.setText(cookieType);
                    String kitchenTypeCode = data.getStringExtra("kitchenTypeCode");
                    kitchenType = kitchenTypeCode;
                }
            }
        }
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        if (type == DLG_WITHDRAW) {
            double quantity;
            String reason;
            if (BC.type == 2) {
                MyRow ro = (MyRow) tag;
                quantity = (Double) ro.getDouble("quantity");
                reason = ro.getString("reason");
            } else {
                quantity = (Double) tag;
            }
            int saleId = S.bill.BillItems.get(pos).SaleID;

            if (!BC.engineerMode){
                if (BC.type == 2) {
                    new MyAsyncTask(this, WithdrawDish.class).execute(saleId, quantity, reason);
                } else {
//				int qua = (int) quantity;
                    new MyAsyncTask(this, WithdrawDish.class).execute(saleId, quantity, "Cancelled");
                }
            }

        } else if (type == DLG_CONFIRM_ABORT) {
            if (!BC.engineerMode){

                new MyAsyncTask(this, GetBillByCode.class).execute(S.bill.BillCode);
            }
        } else if (type == DLG_PRINT_DISH) { // 重打按了确定按钮
            if (!BC.engineerMode){

                new MyAsyncTask(this, PrintDish.class).execute(saleIds);
            }
        } else if (type == DLG_PRINT_DISH_SUCCESS) {
            printMode = false;
            showData();
        } else if (type == DLG_CONFIRM_HOME) {
            goHome();
        } else if (type == 3007) {
            biz.openLoginActivity();
        } else if (type == CHANGE_DISH_PACKEAGE) {//替换套餐
            BillItemInfo packageItem = (BillItemInfo) tag;
            String PackageCode = "";
            String DetailCode = "";

            if (packageItem.IfCahnge == 2) {//替换明细可以替换
                PackageCode = packageItem.PackageCode;
                DetailCode = packageItem.DetailCode;
            } else {
                PackageCode = packageItem.PackageCode;
                DetailCode = packageItem.ItemCode;
            }
            if (!BC.engineerMode){
                new MyAsyncTask(this, GetPackageChangeItem.class).execute(PackageCode, DetailCode);
            }


        }
        super.processDialogOK(type, tag);
    }

    private String getSelectedItem() {
        String saleIds = "";
        int Count = listView.getChildCount();
        for (int i = 0; i < Count; i++) {
            View v = listView.getChildAt(i);
            CheckBox c = (CheckBox) v.findViewById(R.id.chkPrint);
            if (c.isChecked()) {
                if (saleIds.length() > 0) {
                    saleIds += ",";
                }
                saleIds += S.bill.BillItems.get(i).SaleID;
            }
        }
        return saleIds;
    }

    private Dialog createDialog(int id, Object tag) {
        if (id == CONTEXT_MENU_ID) {
            final BillItemInfo bi = S.bill.BillItems.get(pos);
            m = new IconContextMenu(this, tag);
            // List<IconContextMenuItem> l = new
            // ArrayList<IconContextMenuItem>();
            if (bi.SaleID == 0) {
				/* 未下单 */
				/*
				 * int mresid = 0; if (bi.Pricing == 1) { mresid =
				 * R.string.menu_change_price; } else if (bi.Pricing == 2) {
				 * mresid = R.string.menu_change_nameprice; } else if
				 * (bi.Pricing == 3) { mresid = R.string.menu_change_name; } if
				 * (mresid > 0) { m.add(mresid, R.drawable.changenameprice,
				 * MENU_CHANGE_NAME_PRICE); }
				 */
                // m.add(R.string.menu_change_quantity,
                // R.drawable.changequantity, MENU_CHANGE_QUANTITY);
                m.add(R.string.menu_cancel_dish, R.drawable.canceldish, MENU_CANCEL_DISH);
            } else {
				/* 已下单 */
                if (S.bill.getItem(bi.ItemCode, ESubmitType.NotSubmitted) == null && (S.bill
                        .BillItems.size() > 0 && S.bill.BillStatus == 0 && !printMode)) {
                    m.add(R.string.menu_add_dish, R.drawable.adddish, MENU_ADD_DISH);

                }
                // Delivered=true 表示已经上来了
                if (!bi.Delivered) {
                    m.add(R.string.menu_remind_dish, R.drawable.reminddish, MENU_REMIND_DISH);
                    m.add(R.string.menu_set_delivered, R.drawable.delivered, MENU_SET_DELIVERED);
                    // 单品起菜

                    m.add(R.string.lbl_deliverDishes, R.drawable.adddish, MENU_COOK_DISHES);

                } else {
                    m.add(R.string.menu_set_not_delivered, R.drawable.delivered,
                            MENU_SET_DELIVERED);
                }
//                if (S.allowWithdrawDish) {
                //if(bi.IfPackage!=1&&bi.IfPackage!=2){//套餐退菜服务器处理
                if (!settings.getBoolean("popupCancelDish", false)) {//开启退菜功能
                    m.add(R.string.menu_withdraw, R.drawable.withdrawdish, MENU_WITHDRAW_DISH);
                }
                //}
//                }
            }
            // m.add(R.string.action_OrderDiscount, R.drawable.discount,
            // MENU_DISCOUNT);

            m.setOnClickListener(new IconContextMenuOnClickListener() {
                @Override
                public void onClick(int menuId) {
                    CustomDialogView cd = new CustomDialogView(OrderOrderedActivity.this);
                    cd.setInitializer(new Initializer() {
                        public void init(View view) {
                            EditText input = (EditText) view.findViewById(R.id.textEntryValue);
                            if (input != null) {
                                input.setText("" + bi.Quantity);
                            }
                        }
                    });
                    switch (menuId) {
					/*
					 * case MENU_CHANGE_NAME_PRICE:// 改价格，改名称
					 * cd.setInitializer(new Initializer() { public void
					 * init(View view) { EditText name = (EditText)
					 * view.findViewById(R.id.itemname); name.setText("" +
					 * bi.ItemName); EditText price = (EditText)
					 * view.findViewById(R.id.price); price.setText("" +
					 * bi.SalePrice); if (bi.Pricing == 1) {
					 * price.setEnabled(true); name.setEnabled(false); } else if
					 * (bi.Pricing == 2) { price.setEnabled(true);
					 * name.setEnabled(true);
					 *
					 * } else if (bi.Pricing == 3) { price.setEnabled(false);
					 * name.setEnabled(true); } } }); cd.setOKProcessor(new
					 * OKProcessor() { public boolean process(Dialog ad) {
					 * EditText name = (EditText)
					 * ad.findViewById(R.id.itemname); String sname =
					 * name.getText().toString(); EditText price = (EditText)
					 * ad.findViewById(R.id.price); String sprice =
					 * price.getText().toString(); if ("".equals(sname) ||
					 * "".equals(sprice)) { return false; } double d_price =
					 * Double.parseDouble(sprice); if (d_price == 0d) { return
					 * false; } if (BC.type == 2) { int stylen =
					 * bi.CookingStyle; if (stylen == 0) { addprice = 0; } else
					 * { addprice = getStylePrice(stylen); }
					 * S.bill.changeNamePrice(pos, sname, d_price, addprice); }
					 * else { S.bill.changeNamePrice(pos, sname, d_price); }
					 * S.modified = true; showData(); return true; } });
					 * cd.show(bi.ItemName, R.layout.dlg_change_nameprice, -1);
					 * break;
					 */

					/*
					 * case MENU_CHANGE_QUANTITY:// 改数量 cd.setOKProcessor(new
					 * OKProcessor() { public boolean process(Dialog ad) {
					 * EditText te = (EditText)
					 * ad.findViewById(R.id.textEntryValue); String s =
					 * te.getText().toString(); if ("".equals(s)) { return
					 * false; } Double quantity = Double.parseDouble(s); if
					 * (quantity == 0) { return false; } if (BC.type == 2) { int
					 * stylen = bi.CookingStyle; if (stylen == 0) { addprice =
					 * 0; } else { addprice = getStylePrice(stylen); }
					 * S.bill.changeQuantity(pos, quantity, addprice); } else {
					 * S.bill.changeQuantity(pos, quantity); } S.modified =
					 * true; showData(); return true; } }); cd.show(bi.ItemName,
					 * R.layout.text_entry, -1); break;
					 */

                        case MENU_CANCEL_DISH:// 取消菜品

                            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd "); //HH:mm:ss
                            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                            String ee = dff.format(new Date());
//						new MyAsyncTask(OrderOrderedActivity.this, CheckSoldOut.class).execute(ee,
// bi.ItemCode,  bi.Quantity);   //检查估清
//						new MyAsyncTask(OrderOrderedActivity.this, ChangeSoldOut.class).execute
// (ee, bi.ItemCode,  bi.Quantity);//修改估清

                            S.bill.removeItem(pos);
                            StringBuffer str = new StringBuffer();
                            if (bi.ComposeType == 3) {//套餐

                                for (int i = 0; i < S.bill.BillItems.size(); i++) {
                                    BillItemInfo Item = S.bill.BillItems.get(i);
                                    if (Item.IfPackage == 2 && Item.PackageCode.equals(bi
                                            .ItemCode)) {//删除套餐明细
                                        //str.append(i+",");
                                        S.bill.removeItem(i);
                                        i = -1;//删除后顺序变化需重新遍历删除
                                    }

                                }

                            }
					/*	if(str.length()>0){
						String[] pos=str.toString().split(",");
							for (String p:pos){*/

						/*	}
						}
*/

                            S.modified = true;
                            sa.notifyDataSetChanged();
                            showData();
                            break;

                        case MENU_ADD_DISH:// 增加菜品
                            cd.setOKProcessor(new OKProcessor() {
                                public boolean process(Dialog ad) {
                                    EditText te = (EditText) ad.findViewById(R.id.textEntryValue);
                                    if (te.getText().toString().equals("")) {
                                        return true;
                                    }
                                    Double quantity = Double.parseDouble(te.getText().toString());

                                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
                                    //HH:mm:ss
                                    dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                                    String ee = dff.format(new Date());
//								new MyAsyncTask(OrderOrderedActivity.this, CheckSoldOut.class)
// .execute(ee, bi.ItemCode,  quantity);   //检查估清
//								new MyAsyncTask(OrderOrderedActivity.this, ChangeSoldOut.class)
// .execute(ee, bi.ItemCode,  quantity);//修改估清


                                    BillItemInfo bi1 = new BillItemInfo();
                                    bi1.ItemCode = bi.ItemCode;
                                    bi1.ItemName = bi.ItemName;
                                    bi1.Pricing = bi.Pricing;
                                    bi1.CanDiscount = bi.CanDiscount;
                                    bi1.Quantity = quantity;
                                    bi1.IfStyle = bi.IfStyle;
                                    if (BC.type == 2) {// 吉云轩选做法后加菜单价改变问题
                                        int elsePrice = 0;
                                        if (!TextUtils.isEmpty(bi.CookingStyleList)) {
                                            String[] s = bi.CookingStyleList.split(",");
                                            for (String code : s) {
                                                elsePrice += getStylePrice(Integer.parseInt(code));
                                            }
                                        }
                                        bi1.ItemPrice = bi.ItemPrice - elsePrice;
                                        bi1.SalePrice = bi1.ItemPrice;
                                    } else {
                                        bi1.ItemPrice = bi.ItemPrice;
                                        bi1.SalePrice = bi.SalePrice;
                                    }
                                    bi1.SaleTotal = bi1.SalePrice * quantity;
                                    bi1.TotalPayable = bi1.SaleTotal;
                                    bi1.SmallPicture = bi.SmallPicture;
                                    bi1.IfPackage = bi.IfPackage;
                                    BU.addBillItem(bi1);
                                    MyData packageDetail = null;

                                    //获取套餐明细
                                    if (bi.IfPackage == 1) {
                                        String sql = "select ParentCode,ItemCode,ItemName," +
                                                "IfCahnge,Pricing,Updated,ExtCode from " +
                                                "t_PackageDish where ParentCode=? ";
                                        String[] a = new String[]{bi.ItemCode};
                                        packageDetail = db.getData(sql, a);

                                    }
                                    //套餐加入套餐明细到订单列表
                                    if (packageDetail != null) {
                                        for (MyRow packageDish : packageDetail) {
                                            BillItemInfo packageInfo = U.toObject(packageDish,
                                                    BillItemInfo.class);
                                            packageInfo.Quantity = quantity;
                                            packageInfo.IfPackage = 2;
                                            packageInfo.ComposeType = 3;
                                            packageInfo.PackageCode = packageInfo.ParentCode;
                                            BU.addBillItem(packageInfo);
                                        }


                                    }


                                    S.modified = true;
								/*
								 * sa.isCallUp = new boolean[S.bill.BillItems
								 * .size()];
								 */
                                    showData();
                                    return true;
                                }
                            });
                            cd.show(bi.ItemName, R.layout.text_entry, -1);
                            break;
                        case MENU_REMIND_DISH:// 备菜
                            if (!BC.engineerMode){

                                new MyAsyncTask(OrderOrderedActivity.this, RemindDish.class).execute(bi.SaleID);
                            }
                            break;
                        case MENU_SET_DELIVERED:// 已上
                            if (!BC.engineerMode){

                                new MyAsyncTask(OrderOrderedActivity.this, SetDelivered.class).execute(bi.SaleID, !bi.Delivered);
                            }
                            break;
                        case MENU_WITHDRAW_DISH:// 退菜


                            cd.setInitializer(new Initializer() {
                                public void init(View view) {
                                    TextView tv = (TextView) view.findViewById(R.id.te_quantity);
                                    tv.setText(getText(R.string.msg_withdraw_quantity));
                                    EditText input = (EditText) view.findViewById(R.id
                                            .textEntryValue);
                                    input.setText("" + bi.Quantity);
                                }
                            });

                            cd.setOKProcessor(new OKProcessor() {
                                public boolean process(Dialog ad) {
                                    EditText te = (EditText) ad.findViewById(R.id.textEntryValue);
                                    EditText re = (EditText) ad.findViewById(R.id.withdraw_reason);
                                    Double quantity = Double.parseDouble(te.getText().toString());

                                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
                                    //HH:mm:ss
                                    dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                                    String ee = dff.format(new Date());
//								new MyAsyncTask(OrderOrderedActivity.this, CheckSoldOut.class)
// .execute(ee, bi.ItemCode,  quantity);   //检查估清
//								new MyAsyncTask(OrderOrderedActivity.this, ChangeSoldOut.class)
// .execute(ee, bi.ItemCode,  quantity);//修改估清

                                    MyRow ro = new MyRow();
                                    if (BC.type == 2) {
                                        String reason = re.getText().toString();
                                        ro.put("quantity", quantity);
                                        ro.put("reason", reason);
                                    }
                                    if (quantity > 0 && quantity <= bi.Quantity) {
                                        String msg1 = String.format(getText(R.string
                                                        .msg_confirm_withdraw).toString(),
                                                quantity, bi.ItemName);
                                        if (BC.type == 2) {
                                            UI.showOKCancel(OrderOrderedActivity.this,
                                                    DLG_WITHDRAW, msg1,
                                                    getText(R.string.msg_confirm), ro);
                                        } else {
                                            UI.showOKCancel(OrderOrderedActivity.this,
                                                    DLG_WITHDRAW, msg1,
                                                    getText(R.string.msg_confirm), quantity);
                                        }
                                    }
                                    return true;
                                }
                            });
                            if (BC.type == 2) {
                                cd.show(bi.ItemName, R.layout.withdraw_text_entry, -1);
                            } else {
                                cd.show(bi.ItemName, R.layout.text_entry, -1);
                            }

                            break;
                        // case MENU_DISCOUNT:
                        // if (!bi.SeparateDiscount
                        // && bi.SaleTotal != bi.TotalPayable) {
                        // UI.showError(OrderOrderedActivity.this,
                        // R.string.msg_already_give_bill_discount);
                        // return;
                        // }
                        // Intent intent = new Intent(OrderOrderedActivity.this,
                        // ItemDiscountActivity.class);
                        // intent.putExtra("title",
                        // getText(R.string.msg_item_discount));
                        // intent.putExtra("billItem", bi);
                        // intent.putExtra("position", pos);
                        // startActivityForResult(intent, REQUEST_ITEM_DISCOUNT);
                        // break;

                        case MENU_COOK_DISHES: // 单品起菜
                            if (!BC.engineerMode){

                                new MyAsyncTask(OrderOrderedActivity.this, DeliverDish.class).execute(bi.SaleID);
                            }
                            break;
                        case MENU_COOK_ALLDISHES: // 整单起菜
                            if (!BC.engineerMode){

                                new MyAsyncTask(OrderOrderedActivity.this, BillDeliverDish.class).run(S.bill.BillCode);
                            }
                            break;
                    }
                }
            });
            return m.createMenu(bi.ItemName);
        }
        return null;
    }

    /**
     * 首次进入页面，提示图片点击隐藏，第二次进入不显示提示图片
     *
     * @param view
     */
    public void closeImage(View view) {
        findViewById(R.id.tip).setVisibility(View.GONE);
        saveSetting("isShow2", "true");
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


        if (!TextUtils.isEmpty(from) && from.equals("BillList")) {
            return;
        }

        BillItemInfo dish = (BillItemInfo) arg0.getItemAtPosition(arg2);
        // arg0.getChildAt(arg2).setBackgroundColor(Color.BLUE);

        // if (S.bill.getItem(dish.ItemCode, ESubmitType.NotSubmitted) != null)
        // {

        if (dish.SaleID == 0) {// 未下单菜品
            if (dish.ComposeType == 3 && dish.IfPackage == 2 && dish.IfCahnge == 1 || dish
                    .IfCahnge == 2) {//套餐明细替换
                changagePos = arg2;
                UI.showOKCancel(this, CHANGE_DISH_PACKEAGE, R.string
                        .menu_show_change_package_detail, R.string
                        .msg_confirm, dish);
            } else if (dish.ComposeType == 3 && dish.IfPackage == 2) {
                return;
            } else {
                customOrder(dish, sa);
            }
        } else {
            if (dish.IfPackage == 2) {//套餐明细
                return;
            }
            pos = arg2;
            Dialog d = createDialog(CONTEXT_MENU_ID, pos);
            if (d != null) {
                d.show();
            }
        }
    }

    protected void customOrder(final BillItemInfo dish, final ArrayAdapter<?> adapter) {
        CustomDialogView cd = new CustomDialogView(this);
        cd.setInitializer(new Initializer() {
            public void init(View view) {
                initOrderDish(OrderOrderedActivity.this, view, dish.ItemCode);
            }
        });
        cd.setOKProcessor(new OKProcessor() {
            public boolean process(Dialog ad) {
                return changeorder(ad, dish, adapter);
            }
        });
        cd.setCancelProcessor(new CustomDialogView.CancelProcessor() {
            @Override
            public void cancel(Dialog dialog) {
                dish.FixedDiscountCache = 0;
                dish.DiscountRateCache = 100;
                dish.hasChangeSingleDiscount = false;
            }
        });

        cd.show(dish.ItemName, R.layout.order_dish, -1);
    }

    protected boolean changeorder(Dialog v, BillItemInfo dish, ArrayAdapter<?> adapter) {
        EditText t = (EditText) v.findViewById(R.id.quantity);
        Double quantity = Double.parseDouble(t.getText().toString());
        String customFlavor = ((EditText) v.findViewById(R.id.custom_flavor)).getText().toString();
        if (BC.type == 2) {// 安吉轩判断做法是否有加工费
            addprice = 0;
            if (cookingStyles.size() > 0 && !TextUtils.isEmpty(styleString)) {// 无做法时不用获取加工费
                String[] s = styleString.split(",");
                for (String code : s) {
                    addprice += getStylePrice(Integer.parseInt(code));
                }
            }
        }
        EditText cname = (EditText) v.findViewById(R.id.changedname);
        EditText cprice = (EditText) v.findViewById(R.id.changedprice);
        if (t.getText().toString().equals("")) {
            return true;
        }
        if (!TextUtils.isEmpty(cname.getText())) {// 改名称
            dish.ItemName = cname.getText().toString();
        }
        if (!TextUtils.isEmpty(cprice.getText())) {// 改价格
            if (BC.type == 2) {// 安吉轩改价格后单独保存，因做法更改与价格更改同时存在时价格混乱
                dish.HasChangePrice = true;
                dish.ChangedPrice = Double.parseDouble(cprice.getText().toString());
            } else {
                dish.SalePrice = Double.parseDouble(cprice.getText().toString());
            }
        }
        if (BC.type == 2) {
            return changeorder(dish, quantity, getCookingStyle(), styleString, flavorString,
                    customFlavor, adapter);
        } else {
            return changeorder(dish, quantity, getCookingStyle(), getCookingStyleList(),
                    flavorString, customFlavor, adapter);
        }

    }

    /**
     * 选择做法，口味，数量后 刷新界面
     *
     * @param adapter
     * @return
     */
    protected boolean changeorder(BillItemInfo bi, double quantity, int cookingStyle, String
            CookingStyleList,
                                  String cookingFlavor, String customFlavor, ArrayAdapter<?>
                                          adapter) {
        bi.Quantity = quantity;
        if (BC.type == 2) {// 安吉轩单价等于做法加工费加单价
            if (bi.HasChangePrice) {// 改过价格
                bi.SalePrice = addprice + bi.ChangedPrice;
            } else {
                bi.SalePrice = addprice + bi.ItemPrice;
            }
        }
        if (bi.FixedDiscountCache > 0) {
            bi.FixedDiscount = bi.FixedDiscountCache;
            bi.FixedDiscountCache = 0;
            bi.DiscountRate = 100;
            bi.RateDiscount = 0;
        }

        if (bi.DiscountRateCache < 100) {
            bi.DiscountRate = bi.DiscountRateCache;
            bi.DiscountRateCache = 100;
            bi.FixedDiscountCache = 0;
        }
        bi.SaleTotal = bi.SalePrice * quantity;
        if (bi.DiscountRate < 100) {
            bi.TotalPayable = bi.SalePrice * quantity * bi.DiscountRate / 100;
            bi.RateDiscount = bi.SalePrice * quantity - bi.TotalPayable;
            bi.FixedDiscount = 0;
            bi.SeparateDiscount = true;
//            bi.CanDiscount = true;
        } else if (bi.FixedDiscount > 0) {
            bi.TotalPayable = bi.SalePrice * quantity - bi.FixedDiscount;
            bi.DiscountRate = 100;
            bi.RateDiscount = 0;
            bi.SeparateDiscount = true;
//            bi.CanDiscount = true;
        } else {
            bi.TotalPayable = bi.SalePrice * quantity;
            bi.DiscountRate = 100;
            bi.RateDiscount = 0;
            bi.FixedDiscount = 0;
            bi.SeparateDiscount = false;
//            bi.CanDiscount = false;
        }

//        bi.TotalPayable = bi.SaleTotal;
        bi.CookingStyle = cookingStyle;
        bi.CookingStyleList = CookingStyleList;
        bi.CookingFlavor = cookingFlavor;
        bi.CustomTaste = customFlavor;
        bi.KitchenType = kitchenType;
        //套餐处理明细
        if (bi.IfPackage == 1) {
            for (int i = 0; i < S.bill.BillItems.size(); i++) {
                BillItemInfo Item = S.bill.BillItems.get(i);
                if (Item.IfPackage == 2 && Item.PackageCode.equals(bi.ItemCode)) {//套餐明细增加数量
                    if (quantity > 0){
                        Item.Quantity = quantity;
                    }else {
                        Item.Quantity = quantity * Item.Quantity;
                    }
                }

            }
        }

        bi.hasChangeSingleDiscount = false;
        S.bill.calcTotal();
        S.modified = true;
        showData();
        return true;
    }


    //套餐菜品替换
    @Override
    public void listSelected(View view, int index) {
        super.listSelected(view, index);
        if (view.getId() == R.id.ordered_total_payable) {
            if (null != packageChange) {

                BillItemInfo newBillItem = U.toObject(packageChange.get(index), BillItemInfo.class);
                newBillItem.PackageCode = packageChange.get(index).getString("PageckCode");
                newBillItem.IfPackage = 2;
                newBillItem.ComposeType = 3;
                newBillItem.Quantity = S.bill.BillItems.get(changagePos).Quantity;
                //删除旧菜品。替换新菜品
                S.bill.BillItems.remove(changagePos);
                S.bill.BillItems.add(changagePos, newBillItem);

                sa.notifyDataSetChanged();
            }
        }

    }

    //结账
    public void PayBill(View view) {
        String billcode = S.bill.BillCode;
        Intent it = new Intent(this, CheckoutPaymentActivity.class);
        it.putExtra("code", S.bill.TableNo);
        it.putExtra("title", getString(R.string.checkout_consumption_checkout));
        it.putExtra("billcode", billcode);
        it.putExtra("prices", S.bill.TotalPayable);
        startActivity(it);
        finish();
    }
}
