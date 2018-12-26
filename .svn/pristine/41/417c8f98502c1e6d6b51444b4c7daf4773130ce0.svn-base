package com.clubank.device;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.S;
import com.clubank.util.GlideUtil;

import java.util.List;

public class BillItemAdapter extends ArrayAdapter<BillItemInfo> {
    private OrderOrderedActivity a;

    public BillItemAdapter(Context context, int resId, List<BillItemInfo> data) {
        super(context, resId, data);
        a = (OrderOrderedActivity) context;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.lvi_ordered, null);
        }
        LinearLayout dish = (LinearLayout) v.findViewById(R.id.dish_Layout);
        LinearLayout packageDetail = (LinearLayout) v.findViewById(R.id.package_Layout);
        BillItemInfo o = getItem(position);

        if (o != null) {
            if (o.IfPackage == 2) {//套餐明细
                dish.setVisibility(View.GONE);
                packageDetail.setVisibility(View.VISIBLE);
                TextView t1 = (TextView) v.findViewById(R.id.package_itemname);
                TextView t2 = (TextView) v.findViewById(R.id.package_quantity);
                TextView t3 = (TextView) v.findViewById(R.id.package_price);
                TextView t4 = (TextView) v.findViewById(R.id.package_item_total_payable);
                t1.setText(o.ItemName.trim());
                t2.setText(o.Quantity + "");
                t3.setText(BC.nf_a.format(o.SalePrice));
                t4.setText(BC.nf_a.format(o.TotalPayable));
                if (o.IfCahnge == 1 || o.IfCahnge == 2) {
                    v.findViewById(R.id.ifchang).setVisibility(View.VISIBLE);
                } else {
                    v.findViewById(R.id.ifchang).setVisibility(View.GONE);

                }
            } else {
                dish.setVisibility(View.VISIBLE);
                packageDetail.setVisibility(View.GONE);

                TextView t1 = (TextView) v.findViewById(R.id.ordered_itemname);
                TextView t2 = (TextView) v.findViewById(R.id.ordered_quantity);
                TextView t3 = (TextView) v.findViewById(R.id.ordered_price);
                TextView t4 = (TextView) v.findViewById(R.id.ordered_item_total_payable);
                ImageView t5 = (ImageView) v.findViewById(R.id.item_status);

                TextView t7 = (TextView) v.findViewById(R.id.sale_date);
                TextView t8 = (TextView) v.findViewById(R.id.dl_saleId);
                TextView t10 = (TextView) v.findViewById(R.id.style_flavor);
// 			    TextView t11 = (TextView) v.findViewById(R.id.kitchen_type);
                View t9 = v.findViewById(R.id.chkPrint);
                if (o.SaleID > 0) {
                    t8.setText("" + o.SaleID);
                    //是否新点菜品
                    v.findViewById(R.id.new_food_sign).setVisibility(View.GONE);
                    v.findViewById(R.id.new_food_discount).setVisibility(View.GONE);
                } else {
                    v.findViewById(R.id.new_food_sign).setVisibility(View.VISIBLE);
                    if (o.FixedDiscount > 0 || o.DiscountRate < 100){//新点菜品是否进行了本地的单菜打折
                        v.findViewById(R.id.new_food_discount).setVisibility(View.VISIBLE);
                    }else {
                        v.findViewById(R.id.new_food_discount).setVisibility(View.GONE);
                    }
                }

                t1.setText(o.ItemName.trim());
                t2.setText(o.Quantity + "");
                t3.setText(BC.nf_a.format(o.SalePrice));
                t4.setText(BC.nf_a.format(o.TotalPayable));
                if (o.SaleID != 0 && !a.printMode) {
                    if (o.Delivered) {
                        t5.setImageResource(R.drawable.bs_delivered);
                    } else if (o.Printtimes > 0) {
                        t5.setImageResource(R.drawable.bs_printed);
                    } else {
                        t5.setImageResource(R.drawable.bs_submitted);
                    }
                    t5.setVisibility(View.VISIBLE);
                } else {
                    t5.setVisibility(View.GONE);
                    t8.setVisibility(View.INVISIBLE); // T0D0 supersom
                }
                if (o.SmallPicture == null) {
                    o.SmallPicture = BU.getSmallPicture(getContext(), o.ItemCode);
                }
                byte[] b = o.SmallPicture;
                ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
                if (b != null) {
                    GlideUtil.getInstance().setImage(a, iv, b, true);
                } else {
                    GlideUtil.getInstance().setImage(a, iv, R.drawable.zhuye_13, true);
                }
                if (o.OpenTime != null) {
                    t7.setText(o.OpenTime.substring(11, 16));
                } else {
                    t7.setText("");
                }
                if (a.printMode && o.SaleID > 0) {
                    t9.setVisibility(View.VISIBLE);
                } else {
                    t9.setVisibility(View.GONE);
                }
                String s = "";
                if (BC.type == 2) {// 吉云轩做法多选，显示 多项
                    if (o.CookingStyleList != null && o.CookingStyleList.contains(",")) {
                        String[] styles = o.CookingStyleList.split(",");
                        int c = styles.length;
                        if (c > 0) {
                            for (int i = 0; i < c; i++) {
                                s += s.equals("") ? a.getStyleName(Integer.parseInt(styles[i]))
                                        : "," + a.getStyleName(Integer.parseInt(styles[i]));
                            }
                        }
                    } else if (o.CookingStyleList != null && !o.CookingStyleList.equals("")) {
                        s += a.getStyleName(Integer.parseInt(o.CookingStyleList));
                    }
                } else {
                    if (o.CookingStyle > 0) {
                        s += a.getStyleName(o.CookingStyle);
                    }
                }

                if (BC.conversionCode.equalsIgnoreCase("6.x")) {
                    if (o.CookingStyleList != null && o.CookingStyleList.contains(",")) {
                        String[] styles = o.CookingStyleList.split(",");
                        int x = styles.length;
                        if (x > 0) {
                            for (int i = 0; i < x; i++) {
                                s += s.equals("") ? a.getStyleName(Integer.parseInt(styles[i]))
                                        : "," + a.getStyleName(Integer.parseInt(styles[i]));
                            }
                        }
                    } else if (o.CookingStyleList != null && !o.CookingStyleList.equals("")) {
                        s += a.getStyleName(Integer.parseInt(o.CookingStyleList));
                    }
                }
                if (o.CookingFlavor != null && !o.CookingFlavor.trim().equals("")) {
                    s += " " + a.getFlavorNames(o.CookingFlavor);
                }
                if (o.CustomTaste != null && !o.CustomTaste.trim().equals("")) {
                    s += " " + o.CustomTaste;
                }
                if (s.length() > 0) {
                    t10.setText(s);
                } else {
                    t10.setText("");
                }
//                if (o.KitchenType != null && !TextUtils.isEmpty(o.KitchenType)){
//                    t11.setText(a.getKitchenTypeName(o.KitchenType));
//                }

                // 叫起
                TextView iscall = (TextView) v.findViewById(R.id.callup);
                TextView isup = (TextView) v.findViewById(R.id.uppick);
                iscall.setVisibility(View.GONE);
                isup.setVisibility(View.GONE);
                if (o.PrintType == null || o.PrintType.equals("")) {
                    if (S.bill.BillItems.get(position).IsCallUp) {
                        iscall.setVisibility(View.VISIBLE);
                    } else {
                        iscall.setVisibility(View.GONE);
                    }
                    if (S.bill.BillItems.get(position).IsUp) {
                        isup.setVisibility(View.VISIBLE);
                    } else {
                        isup.setVisibility(View.GONE);
                    }
                } else {
                    if (o.PrintType.equals(a.getString(R.string.msg_planing_dish))) {
                        iscall.setVisibility(View.VISIBLE);
                        isup.setVisibility(View.GONE);
                    } else if (o.PrintType.equals(a.getString(R.string.lbl_deliverDishes))) {
                        iscall.setVisibility(View.GONE);
                        isup.setVisibility(View.VISIBLE);
                    }
                }

                v.setTag(iscall);
            }
        }
        return v;
    }
}
