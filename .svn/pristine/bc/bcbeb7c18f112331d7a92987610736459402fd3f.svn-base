package com.clubank.domain;

import java.util.Date;

public class Bill extends SoapData {
	private static final long serialVersionUID = 869466661714455714L;
	public String BillCode;
	public String CardNo;
	public String GuestName;
	public int TotalGuest = 1;
	public String TxDate;
	public double BillTotal;
	public double TotalPayable;
	public double DiscountRate = 100;
	public double RateDiscount;
	public double FixedDiscount;
	public String TableNo;
	public String TableName;
	public String BillShop;
	public String MemNo;
	public int BillStatus;
	public String BillingBy;
	public String CloseBy;
	public String OpenUser;
	public String OpenTime;
	public Date CloseDate;
	public String DiningRemarks; // 备注
	public boolean PrintDish;
	public double DishTotal;// 菜品数量
	public BillItemArray BillItems = new BillItemArray();

	public Bill() {
		BillShop = S.shopCode;
	}

	public void addItem(BillItemInfo bi) {
		//判断啊是否新点菜品，新菜品移动到列表最上边
		if (bi.SaleID > 0){
			BillItems.add(bi);
		}else {
			BillItems.add(0,bi);
		}
		calcTotal();
	}

	public void removeItem(int position) {
		BillItems.remove(position);
		calcTotal();
	}

	public void clearAll() {
		BillItems.clear();
		BillStatus = 0;
		DiscountRate = 100;
		RateDiscount = 0;
		FixedDiscount = 0;
		DiningRemarks = "";
	}

	public void changeQuantity(int pos, double quantity) {
		BillItems.get(pos).Quantity = quantity;
		BillItems.get(pos).SaleTotal = BillItems.get(pos).SalePrice * 1.0000 * quantity;
		BillItems.get(pos).TotalPayable = BillItems.get(pos).SaleTotal;
		calcTotal();
	}

	public void changeNamePrice(int pos, String name, double price) {
		BillItems.get(pos).ItemName = name;
		BillItems.get(pos).SalePrice = price;
		BillItems.get(pos).SaleTotal = BillItems.get(pos).SalePrice * 1.0000 * BillItems.get(pos).Quantity;
		BillItems.get(pos).TotalPayable = BillItems.get(pos).SaleTotal;
		calcTotal();
	}

	public void calcTotal() {
		BillTotal = 0;
		TotalPayable = 0;
		RateDiscount = 0;
		for (int i = 0; i < BillItems.size(); i++) {
			BillTotal += BillItems.get(i).SaleTotal;
			TotalPayable += BillItems.get(i).TotalPayable;
            //明细打折不改变整单的折扣率打折金额
//			RateDiscount += BillItems.get(i).RateDiscount;
		}
	}

	public void removeItem(String itemCode) {
		int pos = -1;
		for (int i = 0; i < BillItems.size(); i++) {
			BillItemInfo bi = BillItems.get(i);
			if (bi.ItemCode.equals(itemCode) && bi.SaleID == 0) {
				pos = i;
				break;
			}
		}
		if (pos >= 0) {
			removeItem(pos);
		}
	}

	public BillItemInfo getItem(String itemCode, ESubmitType submitType) {
		for (int i = 0; i < BillItems.size(); i++) {
			BillItemInfo bi1 = BillItems.get(i);
			if (bi1.ItemCode.equals(itemCode)) {
				if (submitType == ESubmitType.All || submitType == ESubmitType.Submitted && bi1.SaleID != 0
						|| submitType == ESubmitType.NotSubmitted && bi1.SaleID == 0) {
					return bi1;
				} else {
					continue;
				}
			}
		}
		return null;
	}

	public boolean setDelivered(int saleId, boolean delivered) {
		for (BillItemInfo bi : BillItems) {
			if (bi.SaleID == saleId) {
				bi.Delivered = delivered;
				return true;
			}
		}
		return false;
	}

}
