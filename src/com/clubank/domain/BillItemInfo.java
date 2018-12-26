package com.clubank.domain;

import java.io.Serializable;

public class BillItemInfo extends SoapData implements Serializable {
    private static final long serialVersionUID = 6517704343756180455L;
    public int SaleID;
    public String TableName;// 冗余信息
    public String ItemCode;
    public String ItemName;
    public String OpenTime;
    public double ItemPrice = 0;// 原来单价
    public double ChangedPrice = 0;// 保存修改后的单价
    public boolean HasChangePrice = false;// 是否修改了单价
    public double SalePrice = 0;
    public double SaleTotal = 0;
    public double PackageQuantity = 0;//套餐中菜品的初始数量
    public double Quantity = 0;
    public double TotalPayable = 0;
    public boolean Delivered = false;
    public double DiscountRate = 100;
    public double RateDiscount = 0;
    public double FixedDiscount = 0;
    public double DiscountRateCache = 100;//缓存的打折率，点击弹出框的确定按钮后，赋值给DiscountRate
    public double RateDiscountCache = 0;
    public double FixedDiscountCache = 0;//缓存的固定折扣，点击弹出框的确定按钮后，赋值给FixedDiscount
    public boolean hasChangeSingleDiscount = false;//在单个菜品打折界面是否点击了确认按钮

    public boolean SeparateDiscount = false;
    public boolean CanDiscount = false;
    public String TableNo;// for delayed dish
    public String BillingBy;// for delayed dish
    public transient byte[] SmallPicture;
    public int CookingStyle = 0;
    public String CookingStyleList;
    public String CookingFlavor;
    public boolean[] FlavorSelect;
    public String CustomTaste = "";
    public int Printtimes;
    public int Pricing;
    public boolean IsCallUp; // 是否被叫起
    public boolean IsUp;// 是否起菜（上菜）由本地维护
    public String PrintType;
    public String DiningRemarks;
    public int ComposeType;//3表示套餐 其它值不是套餐
    public int IfPackage = 0;//1表示套餐。2表示明细 值是app手动赋值的，只有下单后才可以通过服务器返回
    public int IfCahnge = 0;//套餐明细是否替换菜品 1和2可替换 0不可替换  2表示替换明细中的明细可以替换
    public String PackageCode;//总套餐Code
    public String DetailCode;//替换明细中的DetailCode
    public String ParentCode;//总套餐Code方便加菜用
    public String KitchenType;//入厨方式
    public String KitchenTypeList;
    public boolean IfStyle;//是否菜品做法必须
}