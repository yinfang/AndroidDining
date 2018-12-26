package com.clubank.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.S;

import java.util.ArrayList;
import java.util.List;

/**
 * 叫起或起菜的对话框
 * 
 * @author fengyq
 * 
 */
@SuppressLint({ "ViewConstructor", "CutPasteId" })
public class CallupPopup extends PopupWindow {
	private boolean IsCall; // 叫起？还是起菜？
	public static CheckBox allCall;
	private ListAdapter adapter;
	final List<BillItemInfo> data;

	public CallupPopup(Activity context, boolean iscall, OnClickListener onclick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View thisView = inflater.inflate(R.layout.callup_popup, null);
		// 设置SelectPicPopupWindow的View
		this.setContentView(thisView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度

		this.setWidth(width * 4 / 5);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(height * 3 / 4);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		/*
		 * this.setOutsideTouchable(true); this.setFocusable(true);
		 */

		IsCall = iscall;
		TextView tv = (TextView) thisView.findViewById(R.id.alltext);
		if (IsCall) {
			tv.setText(R.string.msg_allplaning_dish);
		} else {
			tv.setText(R.string.lbl_deliverAllDishes);
		}
		ListView list = (ListView) thisView.findViewById(R.id.list);

		data = new ArrayList<BillItemInfo>();
		int n = S.bill.BillItems.size();
		if (IsCall) { // 叫起
			if (n > 0) {
				for (int i = 0; i < n; i++) {
					if (S.bill.BillItems.get(i).SaleID == 0) {
						if(S.bill.BillItems.get(i).IfPackage!=2){//套餐明细不需要叫起
							data.add(S.bill.BillItems.get(i));
						}

					}
				}
			}
		} else { // 起菜
			if (n > 0) {
				for (int i = 0; i < n; i++) {
					if (S.bill.BillItems.get(i).SaleID != 0 && S.bill.BillItems.get(i).IsCallUp) {
						if(S.bill.BillItems.get(i).IfPackage!=2) {//套餐明细不需要起菜
							data.add(S.bill.BillItems.get(i));
						}
					}
				}
			}
		}
		adapter = new ListAdapter(context, R.layout.flavoritem, data, iscall);
		list.setAdapter(adapter);
		allCall = (CheckBox) thisView.findViewById(R.id.allcall);
		allCall.setChecked(false);
		Button commit = (Button) thisView.findViewById(R.id.commit);
		commit.setOnClickListener(onclick);
		TextView alltv = (TextView) thisView.findViewById(R.id.alltext);
		if (!IsCall) {
			alltv.setText(R.string.lbl_deliverAllDishes);
		} else {
			alltv.setText(R.string.msg_allplaning_dish);
		}
		// 整单叫起
		allCall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
				if (data.size() <= 0) {
					return;
				}

				if (ischeck) {
					int v = data.size();
					int a = S.bill.BillItems.size();
					if (IsCall) {
						if (v > 0) {
							// 循环所有菜，使点钟的菜变换
							for (int y = 0; y < v; y++) {
								data.get(y).IsCallUp = true;// 用isCallup来判断界面显示
							}
						}
						if (a > 0) {
							for (int i = 0; i < a; i++) {
								if (S.bill.BillItems.get(i).ItemCode.equals(data.get(0).ItemCode)
										&& S.bill.BillItems.get(i).SaleID == data.get(0).SaleID) {
									S.bill.BillItems.get(i).IsCallUp = true;
								}
							}

						}

					} else {
						if (v > 0) {
							// 循环所有菜，使点钟的菜变换
							for (int y = 0; y < v; y++) {
								data.get(y).IsUp = true;// 用IsUp来判断界面显示
							}
						}

						if (a > 0) {
							for (int i = 0; i < a; i++) {
								if (S.bill.BillItems.get(i).ItemCode.equals(data.get(0).ItemCode)
										&& S.bill.BillItems.get(i).SaleID == data.get(0).SaleID) {
									S.bill.BillItems.get(i).IsUp = true;
								}
							}
						}
					}

					/*
					 * View v = list.getChildAt(i); CheckBox thisbox =
					 * (CheckBox) v .findViewById(R.id.checkbox);
					 * thisbox.setChecked(true);
					 */
					allCall.setChecked(true);
					adapter.notifyDataSetChanged();

				} else {
					/*
					 * // 循环所有菜，使点钟的菜变换 for (int y = 0; y <
					 * S.bill.BillItems.size(); y++) { data.get(y).IsCallUp =
					 * false;//用isCallup来判断界面显示 }
					 * 
					 * allCall.setChecked(false);
					 * adapter.notifyDataSetChanged();
					 */
				}

			}
		});
	}

	class ListAdapter extends ArrayAdapter<BillItemInfo> {

		public ListAdapter(Context context, int resource, List<BillItemInfo> data, boolean iscall) {
			super(context, R.layout.flavoritem, data);
			IsCall = iscall;

		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			ViewHolder holder = null;

			if (v == null) {
				// 获得ViewHolder对象
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.flavoritem, null);
				holder.tv = (TextView) v.findViewById(R.id.textview);
				holder.box = (CheckBox) v.findViewById(R.id.checkbox);
				// 为view设置标签
				v.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) v.getTag();
			}
			final BillItemInfo thisitem = data.get(position);

			/* box.setChecked(false); */
			if (IsCall) { // 叫起
				if (thisitem.IsCallUp) {
					holder.box.setChecked(true);
				} else {
					holder.box.setChecked(false);
				}
				holder.tv.setText(thisitem.ItemName.trim());

				holder.box.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						chlickBox(position, !thisitem.IsCallUp);
					}
				});

			} else { // 起菜
				if (thisitem.IsUp) {
					holder.box.setChecked(true);
				} else {
					holder.box.setChecked(false);
				}
				holder.tv.setText(thisitem.ItemName.trim());

				holder.box.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						chlickBox(position, !thisitem.IsUp);
					}
				});
				/*
				 * if (thisitem.IsUp) { holder.box.setChecked(true); }
				 * holder.tv.setText(thisitem.ItemName.trim()); holder.box
				 * .setOnCheckedChangeListener(new OnCheckedChangeListener() {
				 * 
				 * @Override public void onCheckedChanged(CompoundButton v,
				 * boolean ischecked) { if (ischecked) { // 如果选中就要判断是不是已经全部被选中
				 * data.get(position).IsUp = ischecked; boolean notall = false;
				 * for (int i = 0; i < data.size(); i++) { if
				 * (!data.get(i).IsUp) { notall = true; break; } } if (notall) {
				 * // 不是全部选中 CallupPopup.allCall.setChecked(false); } else {
				 * CallupPopup.allCall.setChecked(true); } } else { //
				 * 如果取消一个就要将整单改为false CallupPopup.allCall.setChecked(false); }
				 * for (int i = 0; i < S.bill.BillItems.size(); i++) { if
				 * (S.bill.BillItems.get(i).ItemCode .equals(thisitem.ItemCode)
				 * && S.bill.BillItems.get(i).SaleID == thisitem.SaleID) {
				 * S.bill.BillItems.get(i).IsCallUp = !ischecked;
				 * S.bill.BillItems.get(i).IsUp = ischecked; } } } });
				 */
			}
			return v;
		}
	}

	public static class ViewHolder {
		TextView tv;
		CheckBox box;
	}

	// 叫起的checkbox点击事件
	public void chlickBox(int pos, boolean isChecke) {

		boolean notall = false;// false表示全选，ture表示不全选
		if (isChecke) {
			int d = data.size();
			if (IsCall) {
				if (d > 0) {
					// 如果选中就要判断是不是已经全部被选中
					data.get(pos).IsCallUp = isChecke;
					for (int i = 0; i < d; i++) {
						if (!data.get(i).IsCallUp) {
							notall = true;
							break;
						}
					}
				}
			} else {
				if (d > 0) {
					data.get(pos).IsUp = isChecke;
					for (int i = 0; i < d; i++) {
						if (!data.get(i).IsUp) {
							notall = true;
							break;
						}
					}
				}
			}

			if (notall && CallupPopup.allCall.isChecked()) {

				CallupPopup.allCall.setChecked(false);

			} else {
				if (!notall && !CallupPopup.allCall.isChecked()) {// 避免多次设置
					CallupPopup.allCall.setChecked(true);
				}

			}

		} else { // 如果取消就要将整单改为false
			CallupPopup.allCall.setChecked(false);

		}

		BillItemInfo info = data.get(pos);
		int f = S.bill.BillItems.size();
		// 订单数据同步
		if (IsCall) {
			if (f > 0) {
				for (int i = 0; i < f; i++) {
					if (S.bill.BillItems.get(i).ItemCode.equals(info.ItemCode)
							&& S.bill.BillItems.get(i).SaleID == info.SaleID) {
						S.bill.BillItems.get(i).IsCallUp = isChecke;
					}
				}
			}
		} else {// 起菜
			if (f > 0) {
				for (int i = 0; i < f; i++) {
					if (S.bill.BillItems.get(i).ItemCode.equals(info.ItemCode)
							&& S.bill.BillItems.get(i).SaleID == info.SaleID) {
						S.bill.BillItems.get(i).IsCallUp = !isChecke;
						S.bill.BillItems.get(i).IsUp = isChecke;
					}
				}
			}
		}

		info.IsCallUp = isChecke;
		data.set(pos, info);
		adapter.notifyDataSetChanged();
	}
}
