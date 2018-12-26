package com.clubank.domain;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class BillItemArray extends Vector<BillItemInfo> implements KvmSerializable {
	private static final long serialVersionUID = -1166006770093411055L;

	public Object getProperty(int index) {
		return super.get(index);
	}

	public int getPropertyCount() {
		return size();
	}
	
	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int index, Hashtable properties,
			PropertyInfo info) {
		info.name = "BillItemInfo";
		info.type = BillItemInfo.class;
	}

	public void setProperty(int index, Object value) {
		add((BillItemInfo) value);
	}

}
