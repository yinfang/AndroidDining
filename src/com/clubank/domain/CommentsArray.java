package com.clubank.domain;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CommentsArray extends Vector<DishComments> implements KvmSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4880259519671152346L;

	public Object getProperty(int index) {
		return super.get(index);
	}

	public int getPropertyCount() {
		return size();
	}

	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int index, Hashtable properties,
			PropertyInfo info) {
		info.name = "DishComments";
		info.type = DishComments.class;
	}

	public void setProperty(int index, Object value) {
		add((DishComments) value);
	}

}
