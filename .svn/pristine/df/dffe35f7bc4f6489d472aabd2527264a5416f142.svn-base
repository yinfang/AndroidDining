package com.clubank.device;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.clubank.domain.ESubmitType;
import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetRecommendDish;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * @author jishu0416 推荐菜品
 */
public class DishJianListActivity extends BasebizActivity implements
        OnItemClickListener {

    private final int CMENU_DISH = 1;
    private IconContextMenu m;
    private ListView listView;
    private DishAdapter da;
    private int pos;
    private MyData dishes = new MyData();
    private MyData categories;
    private String flavor;
    private MyData distList;

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

        for (int i = 0; i < categories.size(); i++) {
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

    @SuppressLint("SimpleDateFormat")
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

//        String code = categories.get(index).getString("Code");
//        String sql =
//                "select Code,Name,Price,CanDiscount,Soldout,Category,Pricing from t_dish where " +
//						"category=? ";
//        String[] a = new String[]{code};
//        dishes = db.getData(sql, a);
//        da.clear();
//        if (dishes.size() > 0) {
//            for (MyRow row : dishes) {
//                da.add(row);
//            }
//        }

        da.clear();
//        String code = categories.get(index).getString("Code");
//        String sql = "select Code,Name,Price,CanDiscount,Soldout,SmallPicture,Category,Pricing,ComposeType from t_dish where category=? ";
//        String[] a = new String[] { code };
//        dishes = db.getData(sql, a,new String[]{"SmallPicture"});
//        da.clear();
//        if (dishes.size() > 0) {
//            for (MyRow row : dishes) {
//                da.add(row);
//            }
//        }
//        da.notifyDataSetChanged();
        if (distList != null && distList.size() != 0) {
            selectToShow(index);
        } else {
            if (BC.engineerMode){
                String code = categories.get(index).getString("Code");
                String sql = "select Code,Name,Price,HPrice,CanDiscount,Soldout,SmallPicture,Category,Pricing,ComposeType from t_dish where category=? ";
                String[] a = new String[] { code };
                dishes = db.getData(sql, a,new String[]{"SmallPicture"});
                da.clear();
                if (dishes.size() > 0) {
                    for (MyRow row : dishes) {
                        da.add(row);
                    }
                }
                da.notifyDataSetChanged();
            }else {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(now);
                new MyAsyncTask(this, GetRecommendDish.class).run(S.shopCode, "",
                        dateString);
            }
        }
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetRecommendDish.class) {
            if (result.code == RT.SUCCESS) {
                distList = (MyData) result.obj;
                selectToShow(0);
            }
        }
    }

    private void selectToShow(int index) {
        da.clear();
        String code = categories.get(index).getString("Code");
        if (distList.size() != 0) {
            for (MyRow myRow : distList) {
                if (myRow.getString("Category").equals(code)) {
                    da.add(myRow);
                }
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
        final MyRow dish = (MyRow) arg0.getItemAtPosition(pos);
        if (S.orderMode
                && (!"true".equals(dish.get("SoldOut")))
                && S.bill.getItem(dish.getString("Code"), ESubmitType.All) == null) {
            this.pos = pos;
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
                        BC.MENU_ORDER);// 改数量、口味
            }
            byte[] b = (byte[]) dish.get("SmallPicture");
            Drawable d = getResources().getDrawable(R.drawable.icon);
            if (b != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                d = new BitmapDrawable(getResources(), bmp);
            }
            m.add(R.string.menu_show_big_image, d, BC.MENU_SHOW_BIG_IMAGE);// 查看大图
            m.setOnClickListener(new IconContextMenuOnClickListener() {
                @Override
                public void onClick(int menuId) {
                    switch (menuId) {
                        case BC.MENU_ORDER:
                            // customOrder(dish, da);
                            break;
                        case BC.MENU_SHOW_BIG_IMAGE:
						/*
						 * Intent i = new Intent(DishJianListActivity.this,
						 * DishPagerActivity.class);
						 */
                            Intent i = new Intent(DishJianListActivity.this,
                                    DishPagerActivity.class);
                            //i.putExtra("distList", distList);
                            i.putExtra("category", (String) dish.get("Category"));
                            i.putExtra("pos", pos);
                            i.putExtra("title", getText(R.string.lbl_big_image));
                            startActivity(i);
                            break;
                    }
                }
            });

            return m.createMenu((String) dish.get("Name"));
        }
        return null;
    }

    /**
     * 首次进入菜品推荐页面，显示提示图片，点击隐藏，下次进入不显示
     *
     * @param view
     */
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
				/*
				 * TextView tv = (TextView) parentview
				 * .findViewById(R.id.cooking_flavor); tv.setText(flavor); if
				 * (flavorString.length() > 0) { flavorString += ","; }
				 * flavorString = flavorCode;
				 */
            }
        }
    }
}
