package com.clubank.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.clubank.dining.R;
import com.clubank.domain.Bill;
import com.clubank.domain.BillItemArray;
import com.clubank.domain.BillItemInfo;
import com.clubank.domain.C;
import com.clubank.domain.ESubmitType;
import com.clubank.domain.S;
import com.clubank.util.BizDBHelper;
import com.clubank.util.DB;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * Business utilities
 * 
 * @author Administrator
 * 
 */
public class BU {
	public static void fillBillItems(MyData items, BillItemArray list) {
		if (items == null) {
			list.clear();
			return;
		}
		list.clear();
		int n = items.size();
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				MyRow row = items.get(i);
				BillItemInfo rec = new BillItemInfo();
				rec.ItemPrice = row.getDouble("SalePrice");
				rec.SaleID = row.getInt("SaleID");
				rec.TableName = row.getString("TableName");
				rec.ItemCode = row.getString("ItemCode");
				rec.ItemName = row.getString("ItemName");
				rec.SalePrice = row.getDouble("SalePrice");
				rec.Quantity = row.getDouble("Quantity");
				rec.TotalPayable = row.getDouble("TotalPayable");
				rec.SaleTotal = row.getDouble("SaleTotal");
				rec.Delivered = row.getBoolean("Delivered");
				rec.DiscountRate = row.getDouble("DiscountRate");
				rec.RateDiscount = row.getDouble("RateDiscount");
				rec.FixedDiscount = row.getDouble("FixedDiscount");
				rec.SeparateDiscount = row.getBoolean("SeparateDiscount");
				rec.CanDiscount = row.getBoolean("CanDiscount");
				rec.CookingStyle = row.getInt("CookingStyle");
				rec.CookingStyleList = row.getString("CookingStyleList");
				rec.CookingFlavor = row.getString("CookingFlavor");
				rec.CustomTaste = row.getString("CustomTaste");
				rec.Printtimes = row.getInt("Printtimes");
				rec.OpenTime = row.getString("OpenTime");// saledate
				rec.Pricing = row.getInt("Pricing");
				rec.IsCallUp = row.getBoolean("IsCallUp");
				rec.IsUp = row.getBoolean("IsUp");
				rec.PrintType = row.getString("PrintType");
				rec.DiningRemarks = row.getString("DiningRemarks");
				rec.IfPackage = row.getInt("IfPackage");
				rec.PackageCode = row.getString("PackageCode");
				rec.ComposeType = row.getInt("ComposeType");
				rec.KitchenType = row.getString("KitchenType");
				rec.IfStyle = row.getBoolean("IfStyle");
				list.add(rec);
			}
		}
	}

	public static void fillBillItems(MyData items) {
		fillBillItems(items, S.bill.BillItems);
	}

	/**
	 * add an item to bill
	 * 
	 * @param bi
	 */
	public static void addBillItem(BillItemInfo bi) {
		BillItemInfo itemInfo=	S.bill.getItem(bi.ItemCode, ESubmitType.All);
//		if (bi.SaleID == 0){
//			S.bill.BillItems.
//		}

		if(null!=itemInfo&&itemInfo.PackageCode.equals(bi.PackageCode)){//举个栗子：比如咖喱饭是一个套餐中的一个菜。也可以单点。也可以在套餐中包含。而套餐中包含的套餐明细也是单独的一道菜 so 会出现相同itemcode
			S.bill.removeItem(bi.ItemCode);//删除本来有的
			if(bi.ComposeType==3){//删除明细
				for (int i=0;i<S.bill.BillItems.size();i++){//订单中如果有替换
					BillItemInfo Item=S.bill.BillItems.get(i);
						if(bi.ItemCode.equals(Item.PackageCode)){//明细
							S.bill.BillItems.remove(i);
						}
				}

			}

			S.bill.addItem(bi);

		}else{
			S.bill.addItem(bi);

		}

	}

	public static void fillBill(MyRow row) {
		S.bill.CardNo = row.getString("CardNo");
		S.bill.GuestName = row.getString("GuestName");
		S.bill.TotalGuest = row.getInt("TotalGuest");
		S.bill.TableNo = row.getString("TableNo");
		S.bill.TableName = row.getString("TableName");
		S.bill.MemNo = row.getString("MemNo");
		S.bill.TotalPayable = row.getDouble("TotalPayable");
		S.bill.BillTotal = row.getDouble("BillTotal");
		S.bill.DiscountRate = row.getDouble("DiscountRate");
		S.bill.FixedDiscount = row.getDouble("FixedDiscount");
		S.bill.RateDiscount = row.getDouble("RateDiscount");
		S.bill.OpenUser = row.getString("OpenUser");
		S.bill.BillCode = row.getString("BillCode");
		S.bill.OpenTime = row.getString("OpenTime");
		S.bill.TxDate = row.getString("TxDate");
		S.bill.BillStatus = row.getInt("BillStatus");
		S.bill.PrintDish = row.getBoolean("PrintDish");
		S.bill.DiningRemarks = row.getString("DiningRemarks");
		S.bill.GuestName = row.getString("GuestName");
		S.bill.DishTotal = row.getDouble("DishTotal");
		// S.bill.BillShop = row.getString("BillShop");// we put it with
		// S.shopCode in QuickOpenActivity.java
	}

	public static byte[] getSmallPicture(Context context, String itemcode) {
		BizDBHelper helper = new BizDBHelper(context, C.DB_NAME, null, C.DB_VERSION);
		DB db = new DB(helper);
		String sql = "select SmallPicture from t_dish where code=?";
		MyData d = db.getData(sql, new String[] { itemcode }, new String[] { "SmallPicture" });
		if (d.size() > 0) {
			byte[] b = (byte[]) d.get(0).get("SmallPicture");
			return b;
		}
		return null;
	}

	public static void goBack(Activity a) {
		// S.orderMode = false;
		// if (a instanceof OrderDishListActivity
		// || a instanceof OrderOrderedActivity) {
		// S.bill.clearAll();
		// }
		a.finish();
	}

	public static void goTableCenter(Context context) {
		S.orderMode = false;
		S.bill = new Bill();
		Intent intent = new Intent(context, TableActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("from", "");
		intent.putExtra("title", R.string.menu_Table);
		context.startActivity(intent);
	}

}
