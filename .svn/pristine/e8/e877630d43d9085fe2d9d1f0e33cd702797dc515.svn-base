package com.clubank.device;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemArray;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.S;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

@SuppressLint("ViewConstructor")
public class DishAdapter extends BaseAdapter {
	public  int type;//区分套餐明细和菜品列表
	LayoutInflater vi;

	public DishAdapter(BaseActivity context, MyData dishes) {
		super(context, R.layout.lvi_dish, dishes);
	}
	
	@Override
	protected void display(int position, View v, MyRow o) {
		super.display(position, v, o);

	TextView tt = (TextView) v.findViewById(R.id.dish_name);
		TextView cc = (TextView) v.findViewById(R.id.dish_code);
		TextView bt = (TextView) v.findViewById(R.id.dish_price);
		TextView ds = (TextView) v.findViewById(R.id.dish_selected);
		TextView dq = (TextView) v.findViewById(R.id.dish_quantity);
		ImageView im = (ImageView) v.findViewById(R.id.imageView1);

		if (o != null) {
			if (tt != null) {
				if(TextUtils.isEmpty((String) o.get("Name"))){
					tt.setText((String) o.get("ItemName"));

				}else {
					tt.setText((String) o.get("Name"));
				}
			}
			if (bt != null) {
				if (BC.IsHoliday){
					bt.setText(BC.nf_a.format(o.getDouble("HPrice")));
				}else {
					bt.setText(BC.nf_a.format(o.getDouble("Price")));
				}

			}
			cc.setText((String) o.get("Code"));
		}
		double qty = 0;
		if (S.bill != null&&type==0) {
			BillItemArray selected = S.bill.BillItems;
			for (int j = 0; j < selected.size(); j++) {
				BillItemInfo bi = selected.get(j);
				if (o.get("Code").equals(bi.ItemCode)) {
					if(bi.IfPackage!=2){//套餐明细
						qty += bi.Quantity;
					}

				}
			}
		}
		if (S.orderMode && qty > 0&&type==0) {
			dq.setText("" + qty);
			ds.setVisibility(View.VISIBLE);
			dq.setVisibility(View.VISIBLE);
			
		} else {
			ds.setVisibility(View.GONE);
			dq.setVisibility(View.GONE);
		}
		if ("true".equals(o.get("SoldOut"))) {// sold out
			tt.setTextColor(getContext().getResources().getColor(R.color.red));
		} else {
			tt.setTextColor(getContext().getResources().getColor(R.color.black));
		}

		//套餐明细不加载图片
		if(type==0){

		byte[] b = (byte[]) o.get("SmallPicture");
		if (b == null) {
			b = BU.getSmallPicture(getContext(), o.getString("Code"));
		}

		if(b!=null&&b.equals(im.getTag(R.id.imageView1))){

		}else {
			if (b != null) {
				//避免更新列表标记图片从新加载导致闪烁
				setImage(v, R.id.imageView1, b, true);
				o.put("SmallPicture", b);
			} else {
				setImage(v, R.id.imageView1, R.drawable.zhuye_13, true);
			}
			im.setTag(R.id.imageView1, b);
		}

		}

	}
}