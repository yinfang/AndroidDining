package com.clubank.device;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemArray;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.ESubmitType;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.CheckSoldOut;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author jishu0416
 *         菜品列表
 */
public class OrderDishListActivity extends OrderActionActivity implements
        OnItemClickListener {

    private int CMENU_DISH = 1;
    private IconContextMenu m;
    private ListView listView;
    private DishAdapter da;
    private int pos;
    private MyData dishes = new MyData();
    private MyData categories;
    private String flavor;
    MyRow dish;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keepInHistory = true;
        setContentView(R.layout.dish);
        listView = (ListView) findViewById(R.id.lvw_dish);
        da = new DishAdapter(this, dishes);
        listView.setAdapter(da);

        AdapterView.OnItemLongClickListener ilc = new ItemLongClickListener();
        listView.setOnItemLongClickListener(ilc);
        listView.setOnItemClickListener(this);

        if (!S.orderMode) {
            findViewById(R.id.actionbar).setVisibility(View.GONE);
        }
        LinearLayout l = (LinearLayout) findViewById(R.id.category);
        l.removeAllViews();
        String sql = "select Code,Name,Sort from T_ItemCategory where code in(select distinct " +
                "category from t_dish) order by sort";
        categories = db.getData(sql);
        LayoutInflater inflater = LayoutInflater.from(this);
        int size = categories.size();
        for (int i = 0; i < size; i++) {
            String name = categories.get(i).getString("Name");
            View v = inflater.inflate(R.layout.left_item, null);
            TextView tv = (TextView) v.findViewById(R.id.left_item);
            tv.setText(name);
            l.addView(v);
        }
        if (categories.size() > 0) {// display the first category
            showDish(0);
        }

    }

    @Override
    protected void onStart() {
        if (S.orderMode && dishes != null) {
            String isShow3 = settings.getString("isShow3", "false");
            if (isShow3.equals("false")) {
                findViewById(R.id.tip).setVisibility(View.VISIBLE);
            }
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        da.notifyDataSetChanged();
    }

    public void selectCategory(View view) {
        LinearLayout l = (LinearLayout) findViewById(R.id.category);
        int index = l.indexOfChild(view); // 当一点击时得到下标当前View的下标
        showDish(index);
    }

    private void showDish(int index) {
        LinearLayout l = (LinearLayout) findViewById(R.id.category);
        int Count = l.getChildCount();
        for (int i = 0; i < Count; i++) {
            View v = l.getChildAt(i);
            View item = v.findViewById(R.id.left_item);
            // View divider = v.findViewById(R.id.divider);
            if (i == index) {
                item.setBackgroundColor(getResources().getColor(
                        R.color.yellow_title));
                TextView textView = (TextView) item
                        .findViewById(R.id.left_item);
                textView.setTextColor(getResources().getColor(R.color.white));
                // divider.setVisibility(View.INVISIBLE);
            } else {
                item.setBackgroundColor(getResources().getColor(R.color.white));
                TextView textView = (TextView) item
                        .findViewById(R.id.left_item);
                textView.setTextColor(getResources().getColor(
                        R.color.yellow_title));
                // divider.setVisibility(View.VISIBLE);
            }
        }
        String code = categories.get(index).getString("Code");
        String sql = "select Code,Name,Price,HPrice,CanDiscount,Soldout,SmallPicture,Category," +
                "Pricing,ComposeType,IfStyle from t_dish where category=? ";
        String[] a = new String[]{code};
        dishes = db.getData(sql, a, new String[]{"SmallPicture"});
        da.clear();
        if (dishes.size() > 0) {
            for (MyRow row : dishes) {
                da.add(row);
            }
        }
        da.notifyDataSetChanged();
    }

    class ItemLongClickListener implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // Dish dish = (Dish) arg0.getItemAtPosition(arg2);
            pos = arg2;
            Dialog d = createDialog(CMENU_DISH, arg2);
            if (d != null) {
                d.show();
            }
            return true;
        }
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        dish = (MyRow) arg0.getItemAtPosition(pos);

        if (BC.engineerMode) {
            successSoldOut();
        } else {
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd "); //HH:mm:ss
            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            String ee = dff.format(new Date());
            new MyAsyncTask(OrderDishListActivity.this, CheckSoldOut.class).execute(ee, dish.get
					("Code"), 1);   //检查估清
        }
    }


    @Override
    public void onPostExecute(Class<?> op, Result result) {
        // TODO Auto-generated method stub
        super.onPostExecute(op, result);
        if (op == CheckSoldOut.class) {
            if (result.code == 0) {
                UI.showToast(OrderDishListActivity.this, "此菜品已沽清");
            } else if (result.code == 1) {
                successSoldOut();
            }
        }
    }

    private void successSoldOut() {
        MyData packageDetail = null;
        double qty = 0;
        //看已下订单中是否有此订单
        if (S.bill != null) {
            BillItemArray selected = S.bill.BillItems;
            int size = selected.size();
            for (int j = 0; j < size; j++) {
                BillItemInfo bi = selected.get(j);
                if (dish.get("Code").equals(bi.ItemCode)) {
                    if (bi.IfPackage != 2) {//套餐明细
                        qty += bi.Quantity;
                    }
                }
            }
        }

        //套餐加入套餐明细到订单列表
        if (dish.getInt("ComposeType") == 3) {
            String sql = "select ParentCode,ItemCode,ItemName,IfCahnge,Pricing,Updated,ExtCode,Quantity " +
					"from t_PackageDish where ParentCode=? ";
            String[] a = new String[]{dish.getString("Code")};
            packageDetail = db.getData(sql, a);

        }

        if (S.orderMode
                && (!("true".equals(dish.get("SoldOut"))))
                && S.bill.getItem(dish.getString("Code"), ESubmitType.All) == null || qty < 1.0) {
				/*if (S.orderMode
						&& (!"true".equals(dish.get("SoldOut")))
						&& S.bill.getItem(dish.getString("Code"), ESubmitType.All) == null) {*/
            this.pos = pos;
            if (popupQtyWindow) {
                customOrder(OrderDishListActivity.this, dish, da);
            } else {
                addprice = 0;
                //套餐
                if (dish.getInt("ComposeType") == 3) {
                    dish.put("IfPackage", 1);
                    order(dish, 1, 0, "", "", "", da);
                    if (packageDetail != null) {
                        for (MyRow packageDish : packageDetail) {
                            packageDish.put("PackageCode", dish.getString("Code"));
                            packageDish.put("IfPackage", 2);
                            packageDish.put("ComposeType", 3);
                            int quantity = packageDish.getInt("Quantity");
                            order(packageDish, quantity == 0 ? 1 : quantity, 0, "", "", "", da);
                        }
                    }

                } else {
                    order(dish, 1, 0, "", "", "", da);
                }
            }
        }
    }

    private Dialog createDialog(int id, Object tag) {
        if (id == CMENU_DISH) {
            final MyRow dish = dishes.get(pos);
            m = new IconContextMenu(this, tag);
            if (S.orderMode
                    && !dish.get("SoldOut").equals("true")
                    && S.bill.getItem(dish.getString("Code"),
                    ESubmitType.Submitted) == null

                    ) {
                m.add(R.string.menu_changeProperty, R.drawable.adddish,
                        BC.MENU_ORDER);
            }
            byte[] b = (byte[]) dish.get("SmallPicture");
            Drawable d = getResources().getDrawable(R.drawable.icon);
            if (b != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                d = new BitmapDrawable(getResources(), bmp);
            }
            m.add(R.string.menu_show_big_image, d, BC.MENU_SHOW_BIG_IMAGE);

            //7.0以后版本才有套餐功能
            int a = dish.getInt("ComposeType");
            if (dish.getInt("ComposeType") == 3) {//是否是套餐
                m.add(R.string.menu_show_package_detail, getResources().getDrawable(R.drawable
						.icon), BC.QUER_PACKAGE_DETAIL);
            }

            m.setOnClickListener(new IconContextMenuOnClickListener() {
                @Override
                public void onClick(int menuId) {
                    Intent i;
                    switch (menuId) {
                        case BC.MENU_ORDER:
                            customOrder(OrderDishListActivity.this, dish, da);
                            break;
                        case BC.MENU_SHOW_BIG_IMAGE:
                            i = new Intent(OrderDishListActivity.this,
                                    DishPagerActivity.class);
                            i.putExtra("category", (String) dish.get("Category"));
                            i.putExtra("pos", pos);
                            i.putExtra("title", getText(R.string.lbl_big_image));
                            startActivity(i);
                            break;

                        //查看明细详情
                        case BC.QUER_PACKAGE_DETAIL:
                            i = new Intent(OrderDishListActivity.this,
                                    PackageDetailActivity.class);
                            i.putExtra("Code", dish.getString("Code"));
                            i.putExtra("title", getText(R.string.menu_show_package_detail));
                            startActivity(i);
                            break;
                    }
                }
            });


            return m.createMenu((String) dish.get("Name"));
        }
        return null;
    }

    public void closeImage(View view) {
        findViewById(R.id.tip).setVisibility(View.GONE);
        saveSetting("isShow3", "true");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345) {
            if (data != null) {
                flavor = data.getStringExtra("flavor");
                String flavorCode = data.getStringExtra("flavorCode");
                TextView tv = (TextView) parentview
                        .findViewById(R.id.cooking_flavor);
                tv.setText(flavor);
                if (flavorString.length() > 0) {
                    flavorString += ",";
                }
                flavorString = flavorCode;
            }
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
}
