package com.clubank.domain;

import java.io.Serializable;

public class DishComments extends SoapData implements Serializable {
	private static final long serialVersionUID = 6517704343756180455L;
	public String BillCode;
	public int SaleId;
	public boolean Satisfied;
	public String Comments;
}
