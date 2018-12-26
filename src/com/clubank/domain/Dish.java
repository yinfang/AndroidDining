package com.clubank.domain;

/**
 * 菜品
 * 
 * @author Administrator
 * 
 */
public class Dish {
	public String code;
	public String name;
	public String category;
	public String enName;
	public String description;
	public String enDescription;
	public double price;
	public byte[] smallPicture;
	public byte[] largePicture;
	public int hits;
	public String shortcut;
	public boolean canDiscount = true;
	public boolean soldOut;
	public int pricing; // 价格策略 "0" 标准, "1"价格可变, "2"名称价格可变 ,"3"名称可变
}
