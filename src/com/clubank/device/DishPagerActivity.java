package com.clubank.device;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.S;
import com.clubank.util.GlideUtil;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

import java.util.ArrayList;
import java.util.List;

public class DishPagerActivity extends BasebizActivity {

    ViewPager pager;
    MyData dishes;
    List<View> views;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_pager);
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setPageMargin(UI.getPixel(this, 80));
        Bundle bundle = getIntent().getExtras();
        int pos = bundle.getInt("pos");
        String category = bundle.getString("category");

        // inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();

        // for (int i = 0; i < dishes.size(); i++) {
        // View v = inflater.inflate(R.layout.vf_dish, null);
        // views.add(v);
        // }
        String sql = "select Code,Name,Price,HPrice,CanDiscount,Soldout,LargePicture,Category,Pricing,ComposeType from t_dish where category=? ";
        dishes = db.getData(sql, new String[]{category},
                new String[]{"LargePicture"});

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < dishes.size(); i++) {
        	
            View v = vi.inflate(R.layout.vf_dish, null);
           byte[] BigItemImage = (byte[])dishes.get(i).get("LargePicture") ;
           			
//       int[] images = new int[]{R.drawable.about,R.drawable.aa,R.drawable.zhuye_28,R.drawable.zhuye2_07,R.drawable.zhuye2_03};
          ImageView imageView1 = (ImageView)v.findViewById(R.id.imageView1);
          if (null != BigItemImage) {
        	  GlideUtil.getInstance().setImage(this,imageView1,BigItemImage, false);
			} else {
//				GlideUtil.getInstance().setImage(this, imageView1, R.drawable.zhuye_13, false);
			}
          
          views.add(v);
          
        }
        TextView vtotal = (TextView) findViewById(R.id.total);
        vtotal.setText("" + views.size());

        DishPagerAdapter adapter = new DishPagerAdapter(this, views);
        pager.setAdapter(adapter);

        pager.setCurrentItem(pos);
        // setImage(pos);
        pager.setOnPageChangeListener(new MyPageChangeListener(views));
        showPageNumber(pos);

    }

    public void order(View view) {
        int pos = Integer.parseInt("" + view.getTag());
        MyRow dish = dishes.get(pos);
        BillItemInfo bi = new BillItemInfo();
        bi.ItemCode = (String) dish.get("Code");
        bi.PackageCode=dish.getString("ParentCode");
        if(TextUtils.isEmpty(dish.getString("ParentCode"))){
            bi.PackageCode=dish.getString("PackageCode");

        }


        bi.ItemName = dish.getString("Name");
        if(TextUtils.isEmpty(dish.getString("Name"))){
            bi.ItemName=dish.getString("ItemName");

        }


        bi.Quantity = 1;
        if (BC.IsHoliday){
            bi.SalePrice = Double.valueOf((String) dish.get("HPrice"));
        }else {
            bi.SalePrice = Double.valueOf((String) dish.get("Price"));
        }
        bi.ItemPrice = bi.SalePrice;
        bi.SaleTotal = bi.SalePrice * bi.Quantity;
        bi.TotalPayable = bi.SaleTotal;
        bi.CanDiscount = Boolean.valueOf((String) dish.get("CanDiscount"));
        bi.SmallPicture = (byte[]) dish.get("SmallPicture");
        bi.IfPackage=dish.getInt("IfPackage");//套餐类型
        bi.ComposeType=dish.getInt("ComposeType");//是否是套餐

        bi.DetailCode=dish.getString("DetailCode");


        MyData packageDetail=null;

        //获取套餐明细
        if(dish.getInt("ComposeType")==3){
            bi.IfPackage=1;
            String sql = "select ParentCode,ItemCode,ItemName,IfCahnge,Pricing,Updated,ExtCode from t_PackageDish where ParentCode=? ";
            String[] a = new String[] { bi.ItemCode };
            packageDetail = db.getData(sql,a);

        }
        BU.addBillItem(bi);
        //套餐加入套餐明细到订单列表
        if(packageDetail!=null){
            for (MyRow packageDish:packageDetail){
                BillItemInfo packageInfo= U.toObject(packageDish, BillItemInfo.class);
                packageInfo.Quantity= bi.Quantity;
                packageInfo.IfPackage=2;
                packageInfo.ComposeType=3;
                packageInfo.PackageCode=packageInfo.ParentCode;
                BU.addBillItem(packageInfo);
            }


        }


        LinearLayout ll = (LinearLayout) view.getParent().getParent();
        TextView ds = (TextView) ll.findViewById(R.id.dish_selected);
        TextView dq = (TextView) ll.findViewById(R.id.dish_quantity);
        Button bb = (Button) ll.findViewById(R.id.button1);
        dq.setText("" + bi.Quantity);
        ds.setVisibility(View.VISIBLE);
        dq.setVisibility(View.VISIBLE);
        bb.setVisibility(View.GONE);
    }

    class MyPageChangeListener extends SimpleOnPageChangeListener {
        List<View> views;

        MyPageChangeListener(List<View> views) {
            this.views = views;
        }

        @Override
        public void onPageSelected(int pos) {
            super.onPageSelected(pos);
            showPageNumber(pos);
        }
    }

    private void showPageNumber(int pos) {
        TextView vpos = (TextView) findViewById(R.id.pos);
        vpos.setText("" + (pos + 1));
        View v = views.get(pos);
        TextView tt = (TextView) v.findViewById(R.id.textView1);
        TextView bt = (TextView) v.findViewById(R.id.dish_price);
        TextView de = (TextView) v.findViewById(R.id.textView2);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView1);

        MyRow o = dishes.get(pos);
        tt.setText((String) o.get("Name"));
        if (BC.IsHoliday){
            bt.setText(BC.nf_a.format(o.getDouble("HPrice")));
        }else {
            bt.setText(BC.nf_a.format(o.getDouble("Price")));
        }
        if (o.get("Description") != null) {
            de.setText((String) o.get("Description"));
            de.setTextColor(Color.WHITE);
            de.setTextSize(18);
        }
        if ("true".equals(o.get("SoldOut"))) {//
            tt.setTextColor(Color.RED);
        } else {
            tt.setTextColor(Color.WHITE);
        }
        byte[] b = (byte[]) o.get("LargePicture");
        if (b != null && b.equals(iv.getTag(R.id.imageView1))) {

        } else {

            if (b != null) {
                setImage(iv, b, R.drawable.zhuye_13);
            } else {
                // setImage(iv,R.drawable.zhuye_13,R.drawable.zhuye_13);
            }
            iv.setTag(R.id.imageView1, b);
        }


        TextView ds = (TextView) v.findViewById(R.id.dish_selected);
        TextView dq = (TextView) v.findViewById(R.id.dish_quantity);
        Button bb = (Button) v.findViewById(R.id.button1);

        List<BillItemInfo> selected = S.bill.BillItems;
        bb.setTag(pos);
        double qty = 0;
        for (int j = 0; j < selected.size(); j++) {
            BillItemInfo bi = selected.get(j);
            if (bi.ItemCode.equals(o.get("Code"))) {
                qty += bi.Quantity;
            }
        }
        if (S.orderMode && qty > 0) {
            dq.setText("" + qty);
            ds.setVisibility(View.VISIBLE);
            dq.setVisibility(View.VISIBLE);
        } else {
            ds.setVisibility(View.GONE);
            dq.setVisibility(View.GONE);
        }
        if (qty == 0 && (!"true".equals(o.get("SoldOut"))) && S.orderMode) {
            bb.setVisibility(View.VISIBLE);
        } else {
            bb.setVisibility(View.GONE);
        }

    }

}
