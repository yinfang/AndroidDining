package com.clubank.device;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.BC;
import com.clubank.domain.S;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

/**
 * 查看菜品大图
 * 
 * @author jishu0416
 *
 */
public class DishJianPagerActivity extends BasebizActivity {

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
		/*
		 * String sql =
		 * "select Code,Name,Price,CanDiscount,SoldOut,Description,LargePicture from t_dish where category=?"
		 * ; dishes =db.getData(sql, new String[] { category }, new String[] {
		 * "LargePicture" });
		 */
		MyData data = U.getData(bundle, "distList");
		dishes = new MyData();
		for (MyRow myRow : data) {
			if (category.equals(myRow.getString("Category"))) {
				dishes.add(myRow);
			}
		}
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < dishes.size(); i++) {
			View v = vi.inflate(R.layout.vf_dish, null);
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

	/**
	 * 点菜 R.layout.vf_dish
	 * 
	 * @param view
	 */
	public void order(View view) {
		int pos = Integer.parseInt("" + view.getTag());
		MyRow dish = dishes.get(pos);
		BillItemInfo bi = new BillItemInfo();
		bi.ItemCode = (String) dish.get("Code");
		bi.ItemName = (String) dish.get("Name");
		bi.Quantity = 1;
		if (BC.IsHoliday){
			bi.SalePrice = Double.valueOf((String) dish.get("HPrice"));
		}else {
			bi.SalePrice = Double.valueOf((String) dish.get("Price"));
		}
		bi.SaleTotal = bi.SalePrice * bi.Quantity;
		bi.TotalPayable = bi.SaleTotal;
		bi.CanDiscount = Boolean.valueOf((String) dish.get("CanDiscount"));
		bi.SmallPicture = (byte[]) dish.get("SmallPicture");
		BU.addBillItem(bi);

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

	/**
	 * 显示菜品图片轮播图
	 * 
	 * @param pos
	 */
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
		}
		if ("true".equals(o.get("SoldOut"))) {//
			tt.setTextColor(Color.RED);
		} else {
			tt.setTextColor(Color.WHITE);
		}
		byte[] b = (byte[]) o.get("LargePicture");
		if (b != null) {
			setImage(R.id.imageView1, b,true);
		} else {
			setImage(R.id.imageView1, R.drawable.zhuye_13);
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
