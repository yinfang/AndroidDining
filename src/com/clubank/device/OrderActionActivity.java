package com.clubank.device;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.ESubmitType;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.domain.SerializableMyData;
import com.clubank.op.CheckSoldOut;
import com.clubank.util.CustomDialogView;
import com.clubank.util.CustomDialogView.Initializer;
import com.clubank.util.CustomDialogView.OKProcessor;
import com.clubank.util.MEListDialog;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jishu0416 点菜基类 extends BaseActionActivity
 *         <p>
 *         初始化口味，做法，码选等
 */
public class OrderActionActivity extends BaseActionActivity {

    private final int DLG_CONFIRM_ABORT = 10035;
    public final int DLG_CONFIRM_HOME = 10036;
    protected MyData cookingStyles;
    public MyData cookingFlavors,//口味
            kitchenTypes;//入厨方式
    private int styleIndex;
    private String[] styleNames;
    private String[] flavorNames;
    private boolean[] flavorSelected, styleSelected;
    protected boolean popupQtyWindow;//点餐时弹出窗口输入数量和口味
    public String flavorString, styleString;
    public View parentview;
    private List<Integer> styleList;
    private String customFlavor;
    public double addprice;
    private BaseActivity ba;
    private boolean isChanged;
    public String kitchenType;
    public MyData allFlavorData;
    public boolean freeCoding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actions = new String[]{"OrderDishList", "OrderQuickOrder", "OrderOrdered"};
        cookingFlavors = db.getData("select * from T_CookingFlavor");
        kitchenTypes = db.getData("select * from T_KitchenType");
        popupQtyWindow = settings.getBoolean("popupQtyWindow", false);
        ba = new BaseActivity();
//        typeName = new ArrayList<>();
//        flaveType = new ArrayList<>();
//        groupRow = new MyRow();
    }

    @Override
    public void onResume() {
        super.onResume();
        findViewById(R.id.menu).setVisibility(View.GONE);
        String title = getEText(R.id.header_title);
        if (S.bill != null && S.orderMode) {
            String table = getString(R.string.lbl_no_table);
            if (S.bill.TableName != null) {
                table = S.bill.TableName;
            }
            title += "(" + table + ")";
            setHeaderTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        BU.goBack(this);
    }

    public void back(View view) {
        BU.goBack(this);
    }

    protected void confirmAbort() {
        UI.showOKCancel(this, DLG_CONFIRM_ABORT, R.string.msg_abandon_bill, R.string.lbl_confirm);
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == DLG_CONFIRM_ABORT) {
            BU.goBack(this);
        } else if (type == DLG_CONFIRM_HOME) {
            goHome();
        }
    }

    public void goHome(View view) {
        if (S.modified) {// 放弃下单时提示 确定退回到主界面
            UI.showOKCancel(this, DLG_CONFIRM_HOME, R.string.msg_abandon_bill, R.string
                    .lbl_confirm);
        } else {
            goHome();
        }
    }

    private void goHome() {
        S.bill.clearAll();
        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }

    // dialog
    protected void customOrder(final BaseActivity a, final MyRow dish, final ArrayAdapter<?>
            adapter) {
        CustomDialogView cd = new CustomDialogView(this);
        cd.setInitializer(new Initializer() {
            public void init(View view) {
                initOrderDish(a, view, dish.getString("Code"));
            }
        });
        cd.setOKProcessor(new OKProcessor() {
            public boolean process(Dialog ad) {

                MyData packageDetail = null;
                //套餐加入套餐明细到订单列表
                if (dish.getInt("ComposeType") == 3) {
                    String sql = "select ParentCode,ItemCode,ItemName,IfCahnge,Pricing,Updated," +
                            "ExtCode ,Quantity from t_PackageDish where ParentCode=? ";
                    String[] a = new String[]{dish.getString("Code")};
                    packageDetail = db.getData(sql, a);

                }

                //套餐
                if (dish.getInt("ComposeType") == 3) {
                    dish.put("IfPackage", 1);
                    order(ad, dish, adapter);
                    if (packageDetail != null) {
                        for (MyRow packageDish : packageDetail) {
                            packageDish.put("PackageCode", dish.getString("Code"));
                            packageDish.put("IfPackage", 2);
                            packageDish.put("ComposeType", 3);
                            order(ad, packageDish, adapter);
                        }
                    }

                } else {
                    order(ad, dish, adapter);
                }
                return true;
            }
        });
        cd.show((String) dish.get("Name"), R.layout.order_dish, -1);
    }

    /**
     * 初始化口味
     *
     * @param view
     * @param itemCode
     */
    protected void initOrderDish(BaseActivity a, View view, final String itemCode) {
        // 自定义口味
        if (settings.getBoolean("customFlavor", false)) {
            show(view, R.id.custom_row);
        }
        if (a.getClass() == OrderOrderedActivity.class) {
            show(view, R.id.row_single_discount);
        }
        flavorString = "";
        customFlavor = "";
        kitchenType = "";
        EditText qty = (EditText) view.findViewById(R.id.quantity);
        BillItemInfo bi = S.bill.getItem(itemCode, ESubmitType.NotSubmitted);
        if (bi != null && bi.CustomTaste != null && !bi.CustomTaste.equals("")) {
            EditText customEt = (EditText) view.findViewById(R.id.custom_flavor);
            customEt.setText(bi.CustomTaste);
        }
        if (bi != null) {
            qty.setText("" + bi.Quantity);
        }

        if (bi != null && a.getClass() == OrderOrderedActivity.class) {
            TextView tvSingleDiscount = (TextView) view.findViewById(R.id.tv_single_discount_click);
            if (tvSingleDiscount != null) {
                tvSingleDiscount.setTag(bi);
            }
        }

        String sql = "select id,itemcode,name,pricechange,remarks from T_CookingStyle where " +
                "itemcode=? order by sort";
        cookingStyles = db.getData(sql, new String[]{itemCode});

        if (cookingStyles.size() == 0) {
            View v = view.findViewById(R.id.row_cookingStyle);
            v.setVisibility(View.GONE);
        }
        if (BC.type == 2) {// 吉云轩版本做法可多选
            styleString = "";
            String biStyleNames = "";
            String[] s = null;
            if (bi != null && bi.CookingStyleList != null) {
                s = bi.CookingStyleList.split(",");
            }
            styleNames = new String[cookingStyles.size()];
            styleSelected = new boolean[cookingStyles.size()];
            int size = cookingStyles.size();
            for (int i = 0; i < size; i++) {
                String code = String.valueOf(cookingStyles.get(i).getInt("ID"));
                String name = cookingStyles.get(i).getString("Name");
                styleNames[i] = name;
                styleSelected[i] = false;
                if (s != null && Arrays.binarySearch(s, code) >= 0) {
                    if (biStyleNames.length() > 0) {
                        biStyleNames += ",";
                    }
                    biStyleNames += name;
                    styleSelected[i] = true;
                    if (styleString.length() > 0) {
                        styleString += ",";
                    }
                    styleString += cookingStyles.get(i).getInt("ID");
                }
            }
            if (!biStyleNames.equals("")) {
                ((TextView) view.findViewById(R.id.cooking_style)).setText(biStyleNames);
            }
        } else {
            styleNames = new String[cookingStyles.size() + 1];
            styleNames[0] = getString(R.string.lbl_not_selected);
            int size = cookingStyles.size();
            for (int i = 0; i < size; i++) {
                styleNames[i + 1] = cookingStyles.get(i).getString("Name");
                if (bi != null && bi.CookingStyle == cookingStyles.get(i).getInt("ID")) {
                    styleIndex = i + 1;
                    ((TextView) view.findViewById(R.id.cooking_style)).setText
                            (styleNames[styleIndex]);
                }
            }
        }

        if (bi != null) {
            if (BC.conversionCode.equalsIgnoreCase("6.x")) {
                if (bi.CookingStyleList != null && !bi.CookingStyleList.contains(",")
                        && !bi.CookingStyleList.equals("")) {
                    int size = cookingStyles.size();
                    for (int i = 0; i < size; i++) {
                        styleNames[i + 1] = cookingStyles.get(i).getString("Name");
                        if (bi != null && !bi.CookingStyleList.equals("")
                                && Integer.parseInt(bi.CookingStyleList) == cookingStyles.get(i)
                                .getInt("ID")) {
                            styleIndex = i + 1;
                            ((TextView) view.findViewById(R.id.cooking_style)).setText
                                    (styleNames[styleIndex]);
                        }
                    }
                } else if (bi.CookingStyleList != null && !bi.CookingStyleList.equals("")) {
                    String[] ss = bi.CookingStyleList.split(",");
                    int length = ss.length;
                    int size = cookingStyles.size();
                    for (int i = 0; i < length; i++) {

                        for (int j = 0; j < size; j++) {
                            styleNames[j + 1] = cookingStyles.get(j).getString("Name");
                            if (bi != null && Integer.parseInt(ss[i]) == cookingStyles.get(j)
                                    .getInt("ID")) {
                                styleIndex = j + 1;
                                TextView ts = (TextView) view.findViewById(R.id.cooking_style);
                                ((TextView) view.findViewById(R.id.cooking_style))
                                        .setText(ts.getText().toString() + "," +
                                                styleNames[styleIndex]);
                            }
                        }
                    }
                }
            }
        }
//        settings.getBoolean("flavorClassify", false)
        if (settings.getBoolean("flavorClassify", false)) {//口味分类
            if (settings.getBoolean("showflavorClassify", false)) {
                String biFlavorNames = "";
                String[] s = null;
                if (bi != null && bi.CookingFlavor != null) {
                    s = bi.CookingFlavor.split(",");
                }

                ArrayList<String> typeName = new ArrayList<>();
                ArrayList<String> flaveType = new ArrayList<>();
                MyRow groupRow = new MyRow();
                for (int i = 0; i < cookingFlavors.size(); i++) {
                    MyRow row = cookingFlavors.get(i);
                    if (row.get("FlavorType") == null) {
                        continue;
                    }
                    MyData data = (MyData) groupRow.get(row.get("FlavorType"));
                    if (data != null) {
                        data.add(row);
                    } else {
                        data = new MyData();
                        data.add(row);
                        groupRow.put(row.get("FlavorType").toString(), data);
                        typeName.add(row.get("TypeName").toString());
                        flaveType.add(row.get("FlavorType").toString());
                    }
                    String code = cookingFlavors.get(i).getString("Code");
                    String name = cookingFlavors.get(i).getString("Name");
                    if (s != null && Arrays.binarySearch(s, code) >= 0) {
                        if (biFlavorNames.length() > 0) {
                            biFlavorNames += ",";
                        }
                        biFlavorNames += name;
                        if (flavorString.length() > 0) {
                            flavorString += ",";
                        }
                        flavorString += cookingFlavors.get(i).getString("Code");
                        row.put("selected", true);
                    } else {
                        row.put("selected", false);
                    }
                }
                allFlavorData = new MyData();
                for (int i = 0; i < flaveType.size(); i++) {
                    MyRow row = new MyRow();
                    row.put("FlavorType", flaveType.get(i));
                    row.put("TypeName", typeName.get(i));
                    MyData data = (MyData) groupRow.get(flaveType.get(i));
                    row.put("data", data);
                    allFlavorData.add(row);
                }
                if (!biFlavorNames.equals("")) {
                    ((TextView) view.findViewById(R.id.cooking_flavor)).setText(biFlavorNames);
                }
            }
        } else {
            String biFlavorNames = "";
            String[] s = null;
            if (bi != null && bi.CookingFlavor != null) {
                s = bi.CookingFlavor.split(",");
            }
            flavorNames = new String[cookingFlavors.size()];
            flavorSelected = new boolean[cookingFlavors.size()];
            int size = cookingFlavors.size();
            for (int i = 0; i < size; i++) {
                Log.d("------", "initOrderDish: " + cookingFlavors.get(i).toString());
                String code = cookingFlavors.get(i).getString("Code");
                String name = cookingFlavors.get(i).getString("Name");
                flavorNames[i] = name;
                flavorSelected[i] = false;
                if (s != null && Arrays.binarySearch(s, code) >= 0) {
                    if (biFlavorNames.length() > 0) {
                        biFlavorNames += ",";
                    }
                    biFlavorNames += name;
                    flavorSelected[i] = true;
                    if (flavorString.length() > 0) {
                        flavorString += ",";
                    }
                    flavorString += cookingFlavors.get(i).getString("Code");
                }
            }
            if (!biFlavorNames.equals("")) {
                ((TextView) view.findViewById(R.id.cooking_flavor)).setText(biFlavorNames);
            }
        }

        if (settings.getBoolean("kitchentype", BC.showKitchenType)) {
            String kitchenTypeName = "";
            if (bi != null && bi.KitchenType != null) {
                kitchenType = bi.KitchenType;
                kitchenTypeName = getKitchenTypeName(bi.KitchenType);
                if (!kitchenTypeName.equals("")) {
                    ((TextView) view.findViewById(R.id.tv_cooking_type)).setText(kitchenTypeName);
                }
            }
            view.findViewById(R.id.row_cooking_type).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.row_cooking_type).setVisibility(View.GONE);
        }


        freeCoding = false;
        /*
         * 已选菜品界面 将原有长按改名称改价格,取消 功能改为单击事件时触发，取消原长按事件
		 * 选菜界面添加改价格和名称的功能，只针对吉云轩版本  2016.10.12
		 */
        if (a.getClass() == OrderOrderedActivity.class
                || (BC.type == 2 && bi != null && a.getClass() == OrderDishListActivity.class)) {
            View cname = view.findViewById(R.id.change_name_row);
            View cprice = view.findViewById(R.id.change_price_row);
            EditText name = (EditText) view.findViewById(R.id.changedname);
            name.setText(bi.ItemName);
            // EditText price = (EditText) view.findViewById(R.id.changedprice);
            // price.setText("" + bi.SalePrice);
            if (bi.Pricing == 1) {
                cname.setVisibility(View.GONE);
                cprice.setVisibility(View.VISIBLE);
                //自由编码数量可为负数
                freeCoding = true;
            } else if (bi.Pricing == 2) {
                cname.setVisibility(View.VISIBLE);
                cprice.setVisibility(View.VISIBLE);
                //自由编码数量可为负数
                freeCoding = true;
            } else if (bi.Pricing == 3) {
                cname.setVisibility(View.VISIBLE);
                cprice.setVisibility(View.GONE);
            }
        }


    }


    //获取入厨方式名称 根据kitchentype Code
    public String getKitchenTypeName(String kitchenType) {
        String s = "";
        String sql = "select name from T_KitchenType where Code=?";
        MyRow row = db.getByKey(sql, "" + kitchenType);
        if (row != null) {
            s = row.getString("Name");
        }
        return s == null ? "" : s;
    }

    /**
     * 获得做法名称（更具口味id）
     *
     * @param id
     * @return
     */
    public String getStyleName(int id) {
        String s = "";
        String sql = "select name from T_CookingStyle where id=?";
        MyRow row = db.getByKey(sql, "" + id);
        if (row != null) {
            s = row.getString("Name");
        }
        return s == null ? "" : s;
    }

    /**
     * 获得做法加工费
     *
     * @param id
     * @return
     */
    public double getStylePrice(int id) {
        double s = 0;
        String sql = "select PriceChange from T_CookingStyle where ID=?";
        MyRow row = db.getByKey(sql, "" + id);
        if (row != null) {
            s = row.getDouble("PriceChange");
        }
        return s;
    }

    /**
     * 获得口味名称（根据口味id）
     *
     * @param codes
     * @return
     */
    public String getFlavorNames(String codes) {
        String flavorNames = "";
        String[] s = codes.split(",");
        int size = cookingFlavors.size();
        for (int i = 0; i < size; i++) {
            String code = cookingFlavors.get(i).getString("Code");
            String name = cookingFlavors.get(i).getString("Name");
            if (s != null && Arrays.binarySearch(s, code) >= 0) {
                if (flavorNames.length() > 0) {
                    flavorNames += ",";
                }
                flavorNames += name;
            }
        }
        return flavorNames;
    }

    // 吉云轩点菜时增加改名称和价格功能
    protected boolean order(MyRow dish, String name, double changedPrice, double quantity, int
            cookingStyle,
                            String cookingStyleList, String cookingFlavor, String customFlavor,
                            ArrayAdapter<?> adapter) {
        BillItemInfo bi = new BillItemInfo();
        bi.ItemCode = dish.getString("Code");
        if (TextUtils.isEmpty(dish.getString("Code"))) {
            bi.ItemCode = dish.getString("ItemCode");

        }


        bi.PackageCode = dish.getString("ParentCode");
        if (TextUtils.isEmpty(dish.getString("ParentCode"))) {
            bi.PackageCode = dish.getString("PackageCode");

        }


        if (!TextUtils.isEmpty(name)) {
            bi.ItemName = name;
        } else {
            bi.ItemName = dish.getString("Name");
            if (TextUtils.isEmpty(dish.getString("Name"))) {
                bi.ItemName = dish.getString("ItemName");

            }
        }
        bi.Quantity = quantity;
        if (BC.IsHoliday) {
            bi.ItemPrice = dish.getDouble("HPrice");
        } else {
            bi.ItemPrice = dish.getDouble("Price");
        }

        if (isChanged) {// 价格已更改
            bi.HasChangePrice = true;
            bi.ChangedPrice = changedPrice;
            bi.SalePrice = bi.ChangedPrice + addprice;
        } else {
            bi.SalePrice = bi.ItemPrice + addprice;
        }
        bi.SaleTotal = bi.SalePrice * quantity;
        bi.TotalPayable = bi.SaleTotal;
        bi.CanDiscount = dish.getBoolean("CanDiscount");
        bi.SmallPicture = (byte[]) dish.get("SmallPicture");
        bi.CookingStyle = cookingStyle;
        bi.CookingStyleList = cookingStyleList;
        bi.Pricing = dish.getInt("Pricing");
        bi.CookingFlavor = cookingFlavor;

        bi.CustomTaste = customFlavor;
        bi.IfPackage = dish.getInt("IfPackage");//套餐类型
        bi.ComposeType = dish.getInt("ComposeType");//是否是套餐
        bi.IfCahnge = dish.getInt("IfCahnge");
        bi.DetailCode = dish.getString("DetailCode");
        bi.KitchenType = kitchenType;
        BU.addBillItem(bi);
        S.modified = true;
        adapter.notifyDataSetChanged();
        return true;
    }

    protected boolean order(MyRow dish, double quantity, int cookingStyle, String cookingStyleList,
                            String cookingFlavor, String customFlavor, ArrayAdapter<?> adapter) {
        BillItemInfo bi = new BillItemInfo();
        bi.ItemCode = dish.getString("Code");
        if (TextUtils.isEmpty(dish.getString("Code"))) {
            bi.ItemCode = dish.getString("ItemCode");

        }

        bi.ItemName = dish.getString("Name");
        if (TextUtils.isEmpty(dish.getString("Name"))) {
            bi.ItemName = dish.getString("ItemName");

        }

        bi.PackageCode = dish.getString("ParentCode");
        if (TextUtils.isEmpty(dish.getString("ParentCode"))) {
            bi.PackageCode = dish.getString("PackageCode");

        }


        bi.Quantity = quantity;
        if (BC.IsHoliday) {
            bi.ItemPrice = dish.getDouble("HPrice");
        } else {
            bi.ItemPrice = dish.getDouble("Price");
        }

        if (BC.type == 2) {// 安吉轩添加口味加工费
            bi.SalePrice = bi.ItemPrice + addprice;
        } else {
            bi.SalePrice = bi.ItemPrice;
        }
        bi.SaleTotal = bi.SalePrice * quantity;
        bi.TotalPayable = bi.SaleTotal;
        bi.CanDiscount = dish.getBoolean("CanDiscount");
        bi.SmallPicture = (byte[]) dish.get("SmallPicture");
        bi.CookingStyle = cookingStyle;
        bi.CookingStyleList = cookingStyleList;
        bi.Pricing = dish.getInt("Pricing");
        bi.CookingFlavor = cookingFlavor;
        //	bi.Pricing = Integer.valueOf((String) dish.get("Pricing"));
        bi.CustomTaste = customFlavor;
        bi.IfPackage = dish.getInt("IfPackage");//套餐类型
        bi.ComposeType = dish.getInt("ComposeType");//是否是套餐
        bi.IfCahnge = dish.getInt("IfCahnge");
        bi.DetailCode = dish.getString("DetailCode");
        bi.KitchenType = kitchenType;
        bi.IfStyle = dish.getBoolean("IfStyle");//做法是否必选
        BU.addBillItem(bi);
        S.modified = true;
        adapter.notifyDataSetChanged();
        return true;
    }

    /**
     * dialog确定
     */
    protected boolean order(final Dialog v, final MyRow dish, final ArrayAdapter<?> adapter) {
        final EditText t = (EditText) v.findViewById(R.id.quantity);
        if (t.getText().toString().equals("")) {
            return true;
        }
        if (BC.engineerMode) {
            actionSuccessSoldOut(t, v, dish, adapter);
            return true;
        }

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd "); //HH:mm:ss
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        final String ee = dff.format(new Date());
        final CheckSoldOut checkSoldOut = new CheckSoldOut(this);
        Observable.create(new ObservableOnSubscribe<Result>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Result> observableEmitter) throws
                    Exception {
                Result result = checkSoldOut.execute(ee, dish.get("Code"), t.getText().toString()
                        .trim());
                observableEmitter.onNext(result);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onNext(@NonNull Result result) {
                        if (result.code == 0) {
                            UI.showToast(OrderActionActivity.this, "此菜品已沽清");
                        } else if (result.code == 1) {
                            actionSuccessSoldOut(t, v, dish, adapter);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return true;
//        new MyAsyncTask(this, CheckSoldOut.class).run(ee, dish.get("Code"), t.getText().toString
//                ().trim());
//        for (int i = 0; i < dish.size(); i++) {
//            Log.d("DISHHHH", dish.keyAt(i) + " : " + dish.valueAt(i).toString());
//        }

//        Double quantity = Double.parseDouble(t.getText().toString());
//        String customFlavor = ((EditText) v.findViewById(R.id.custom_flavor)).getText()
// .toString();
//        if (BC.type == 2) {
//            addprice = 0;
//            String stylen = ((TextView) v.findViewById(R.id.cooking_style)).getText().toString();
//            if (stylen != null && !stylen.equals("(未选)") && cookingStyles.size() > 0) {//
//                // 无做法时不用获取加工费
//                if (!TextUtils.isEmpty(styleString)) {
//                    String[] s = styleString.split(",");
//                    for (String code : s) {
//                        addprice += getStylePrice(Integer.parseInt(code));
//                    }
//                }
//                // addprice = cookingStyles.get(styleIndex -
//                // 1).getDouble("PriceChange");
//            }
//            EditText name = (EditText) v.findViewById(R.id.changedname);
//            EditText price = (EditText) v.findViewById(R.id.changedprice);
//            double cprice = 0;
//            if (!TextUtils.isEmpty(price.getText().toString())) {
//                isChanged = true;
//                cprice = Double.parseDouble(price.getText().toString());
//            }
//            return order(dish, name.getText().toString(), cprice, quantity, getCookingStyle(),
//                    styleString,
//                    flavorString, customFlavor, adapter);
//        } else {
//            return order(dish, quantity, getCookingStyle(), getCookingStyleList(), flavorString,
//                    customFlavor, adapter);
//        }
    }


    public void actionSuccessSoldOut(TextView t, Dialog v, MyRow dish, ArrayAdapter<?> adapter) {
        Double quantity = Double.parseDouble(t.getText().toString());
        String customFlavor = ((EditText) v.findViewById(R.id.custom_flavor))
                .getText().toString();
        if (BC.type == 2) {
            addprice = 0;
            String stylen = ((TextView) v.findViewById(R.id.cooking_style))
                    .getText().toString();
            if (stylen != null && !stylen.equals("(未选)") && cookingStyles
                    .size() > 0) {//
                // 无做法时不用获取加工费
                if (!TextUtils.isEmpty(styleString)) {
                    String[] s = styleString.split(",");
                    for (String code : s) {
                        addprice += getStylePrice(Integer.parseInt(code));
                    }
                }
                // addprice = cookingStyles.get(styleIndex -
                // 1).getDouble("PriceChange");
            }
            EditText name = (EditText) v.findViewById(R.id.changedname);
            EditText price = (EditText) v.findViewById(R.id.changedprice);
            double cprice = 0;
            if (!TextUtils.isEmpty(price.getText().toString())) {
                isChanged = true;
                cprice = Double.parseDouble(price.getText().toString());
            }
            order(dish, name.getText().toString(), cprice, quantity,
                    getCookingStyle(),
                    styleString,
                    flavorString, customFlavor, adapter);
        } else {
            order(dish, quantity, getCookingStyle(),
                    getCookingStyleList(), flavorString,
                    customFlavor, adapter);
        }
    }


    /**
     * 获取做法编码
     *
     * @return
     */
    protected int getCookingStyle() {
        int cookingStyle = 0;
        if (cookingStyles.size() > 0 && styleIndex > 0) {
            cookingStyle = cookingStyles.get(styleIndex - 1).getInt("ID");
        }
        return cookingStyle;
    }

    /**
     * 获取做法ID列表(String字符串 以","分隔)
     *
     * @return
     */
    protected String getCookingStyleList() {
        String list = "";
        if (styleList == null || styleList.size() == 0) {
            return null;
        }
        int size = styleList.size();
        for (int i = 0; i < size; i++) {
            list += list.equals("") ? cookingStyles.get(styleList.get(i) - 1).getInt("ID")
                    : "," + cookingStyles.get(styleList.get(i) - 1).getInt("ID");
        }
        return list;
    }

    /**
     * 选择做法
     *
     * @param view
     */
    public void selectStyle(View view) {
        if (BC.conversionCode.equalsIgnoreCase("6.x")) {
            showMListDialog(view, styleNames, new boolean[styleNames.length]);
        } else {
            if (BC.type == 2) {// 吉云轩做法可多选 2016.10.11
                int count = 0;
                for (boolean c : styleSelected) {
                    if (c) {
                        count++;
                    }
                }
                if (styleString != null && !styleString.equals("") && count != styleString.split
                        (",").length) {
                    matchMStyleSelected();
                }
                showMListDialog(view, styleNames, styleSelected);
            } else {
                showListDialog(view, styleNames);
            }
        }
    }

    /**
     * 选择口味
     *
     * @param view
     */
    public void selectFlavor(View view) {
//        settings.getBoolean("flavorClassify", false)
        if (settings.getBoolean("flavorClassify", false)) {


            final MEListDialog dialog = new MEListDialog(this);
            dialog.setOnSelectedListener(new MEListDialog.OnPositiveListener() {
                @Override
                public void onSelected(View view, MyData allFlavorData) {
                    mElistSelected(view, allFlavorData);

                }
            });
//            dialog.setOnNegativeListener(new MEListDialog.OnNegativeListener() {
//                @Override
//                public void onSelected(View view, MyData allData) {
////                    recoverFlavorData(allData);
//                }
//            });
            if (allFlavorData.size() <= 0) {
                return;
            }
            dialog.show(view, 0, 0, allFlavorData);
        } else {
            int count = 0;
            for (boolean c : flavorSelected) {
                if (c) {
                    count++;
                }
            }
            boolean[] a = new boolean[10];
            if (flavorString != null && !flavorString.equals("") && count != flavorString.split
                    (",").length) {
                matchMSelected();
            }
            showMListDialog(view, flavorNames, flavorSelected);
        }
    }

//    private void recoverFlavorData(MyData allData) {
//        this.allFlavorData = allData;
//        String rows = new Gson().toJson(allFlavorData);
//        Log.d("----------", "recoverFlavorData: " + rows);
//    }

    /**
     * 获取已选做法编码和名称
     */
    private void matchMStyleSelected() {
        String[] styleCode = styleString.split(",");
        styleNames = new String[cookingStyles.size()];
        styleSelected = new boolean[cookingStyles.size()];
        int size = cookingStyles.size();
        for (int i = 0; i < size; i++) {
            String code = String.valueOf(cookingStyles.get(i).getInt("ID"));
            String name = cookingStyles.get(i).getString("Name");
            styleNames[i] = name;
            styleSelected[i] = false;
            if (styleCode != null && Arrays.binarySearch(styleCode, code) >= 0) {
                styleSelected[i] = true;
            }
        }
    }

    /**
     * 获取口味编码和名称
     */
    private void matchMSelected() {
        String[] flavorCode = flavorString.split(",");
        flavorNames = new String[cookingFlavors.size()];
        flavorSelected = new boolean[cookingFlavors.size()];
        int size = cookingFlavors.size();
        for (int i = 0; i < size; i++) {
            String code = cookingFlavors.get(i).getString("Code");
            String name = cookingFlavors.get(i).getString("Name");
            flavorNames[i] = name;
            flavorSelected[i] = false;
            if (flavorCode != null && Arrays.binarySearch(flavorCode, code) >= 0) {
                flavorSelected[i] = true;
            }
        }
    }

    @Override
    public void listSelected(View view, int index) {
        int id = view.getId();
        if (id == R.id.cooking_style) {
            styleIndex = index;
            ((TextView) view).setText(styleNames[index]);

        }
    }

    @Override
    public void mlistSelected(View view, boolean[] b) {

        if (view.getId() == R.id.cooking_style) {
            /*
             * if (styleList == null) { styleList = new ArrayList<Integer>(); }
             * styleList.clear(); styleSelected = b; for (int i = 0; i <
             * b.length; i++) { if (b[i]) { styleList.add(i); } } String ss =
             * ""; for (int i = 0; i < b.length; i++) { if (b[i]) { ss +=
             * ss.equals("") ? styleNames[i] : "," + styleNames[i]; } }
             */
            String ss = "";
            for (int i = 0; i < b.length; i++) {
                if (b[i]) {
                    ss += cookingStyles.get(i).get("Name") + ",";
                }
            }
            if (ss.endsWith(",")) {
                ss = ss.substring(0, ss.length() - 1);
            }
            ((TextView) view).setText(ss);
            if (BC.type == 2) {
                if (styleSelected != null) {
                    styleString = "";
                    int length = styleSelected.length;
                    for (int i = 0; i < length; i++) {
                        if (styleSelected[i]) {
                            if (styleString.length() > 0) {
                                styleString += ",";
                            }
                            styleString += cookingStyles.get(i).getInt("ID");
                        }
                    }
                }
            }
        } else {
            flavorSelected = b;
            int id = view.getId();
            if (id == R.id.cooking_flavor) {
                String s = "";
                int length = b.length;
                for (int i = 0; i < length; i++) {
                    if (b[i]) {
                        s += cookingFlavors.get(i).get("Name") + ",";
                    }
                }
                if (s.endsWith(",")) {
                    s = s.substring(0, s.length() - 1);
                }
                ((TextView) view).setText(s);
            }

            if (flavorSelected != null) {
                flavorString = "";
                int length = flavorSelected.length;
                for (int i = 0; i < length; i++) {
                    if (flavorSelected[i]) {
                        if (flavorString.length() > 0) {
                            flavorString += ",";
                        }
                        flavorString += cookingFlavors.get(i).getString("Code");
                    }
                }
            }
        }
    }

    public void mElistSelected(View view, MyData allData) {
        int id = view.getId();

        if (id == R.id.cooking_flavor) {
            String s = "";
            flavorString = "";
            int length = allData.size();
            for (int i = 0; i < length; i++) {
                MyData data = (MyData) allData.get(i).get("data");
                for (int j = 0; j < data.size(); j++) {
                    MyRow row = data.get(j);
                    if (row.getBoolean("selected")) {
                        s += row.getString("Name") + ",";
                        if (flavorString.length() > 0) {
                            flavorString += ",";
                        }
                        flavorString += row.getString("Code");
                    }
                }
            }
            if (s.endsWith(",")) {
                s = s.substring(0, s.length() - 1);
            }
            ((TextView) view).setText(s);
        }
    }


    /**
     * 数量+
     *
     * @param v
     */
    public void add(View v) {
        View parent = (View) v.getParent();
        EditText et = (EditText) parent.findViewById(R.id.quantity);
        String s = et.getText().toString();
        if (s.contains(".")) {
            s = s.substring(0, s.indexOf("."));
        }
        et.setText((Integer.parseInt(s) + 1) + "");
    }

    /**
     * 数量—
     *
     * @param v
     */
    public void del(View v) {
        View parent = (View) v.getParent();
        EditText et = (EditText) parent.findViewById(R.id.quantity);
        String s = et.getText().toString();
        if (s.contains(".")) {
            s = s.substring(0, s.indexOf("."));
        }
        if (freeCoding) {
            int pos = Integer.parseInt(s) - 1;
            et.setText(pos + "");
        } else {
            int pos = (Integer.parseInt(s) - 1) < 0 ? 0 : (Integer.parseInt(s) - 1);
            et.setText(pos + "");
        }

    }

    /**
     * 码选
     *
     * @param view
     */
    public void selectma(View view) {
        parentview = (View) view.getParent();
        styleList = new ArrayList<Integer>();
        Intent i = new Intent(this, SelectMa.class);
        Bundle b = new Bundle();
        SerializableMyData data = new SerializableMyData();
        data.setData(cookingFlavors);
        b.putSerializable("cookingFlavors", data);
        MyData flavorsTrans = new MyData();
        if (settings.getBoolean("flavorClassify", false)) {
            for (int j = 0; j < allFlavorData.size(); j++) {
                String flavor = allFlavorData.get(j).getString("FlavorType");
                MyData secData = (MyData) allFlavorData.get(j).get("data");
                for (int k = 0; k < secData.size(); k++) {
                    MyRow r = secData.get(k);
                    if (r.getBoolean("selected")) {
                        MyRow row = new MyRow();
                        row.put("Code", r.getString("Code"));
                        row.put("Name", r.getString("Name"));
                        flavorsTrans.add(row);
                    }
                }
            }
        } else {
            int length = flavorSelected.length;
            for (int j = 0; j < length; j++) {
                if (flavorSelected[j]) {
                    MyRow row = new MyRow();
                    row.put("Code", cookingFlavors.get(j).getString("Code"));
                    row.put("Name", cookingFlavors.get(j).getString("Name"));
                    flavorsTrans.add(row);
                }
            }
        }
        SerializableMyData data2 = new SerializableMyData();
        data2.setData(flavorsTrans);
        b.putSerializable("flavorsTrans", data2);
        i.putExtra("flavorBundle", b);
        startActivityForResult(i, 12345);
    }

    /**
     * 选择入厨方式
     *
     * @param view
     */
    public void selectcookie(View view) {
        parentview = (View) view.getParent();
        if (kitchenTypes != null && kitchenTypes.size() > 0) {
            Intent i = new Intent(this, SelectCookieTypeActivity.class);
            String json = new Gson().toJson(kitchenTypes);
            i.putExtra("cookie", json);
            startActivityForResult(i, 23456);
        }
    }

    public void singleDiscount(View view) {
        BillItemInfo billItemInfo = (BillItemInfo) view.getTag();
        if (billItemInfo != null) {
            Intent intent = new Intent(this, SingleDiscountActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("singleBill", billItemInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }

}