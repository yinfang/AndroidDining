package com.clubank.domain;

/**
 * Session variable
 * 
 * @author Administrator
 * 
 */
public class S {
	public static String token;
	public static String userCode;
	public static String userName;
	public static String shopCode;
	public static String shopName;
	public static Bill bill = new Bill();
	public static boolean orderMode;// 是否点菜状态
	public static boolean modified;
	public static int areaPosition;
	public static boolean allowWithdrawDish = true;// 允许退菜
	/**
	 * 允许打折
	 */
	public static boolean AllowDiscount = true;	
	/**
	 * //允许记账
	 */
	public static boolean AllowCreditToCard = true;
}
