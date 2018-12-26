package com.clubank.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MyData extends ArrayList<MyRow> {

	private static final long serialVersionUID = 15984232684L;


	/* (non-Javadoc)
	 * @see java.util.ArrayList#get(int)
	 * 防止数组越界
	 */
	public MyRow get(int index) {
		MyRow row;
		try {

			if (index >= size()) {// 防止数组越界
				row = new MyRow();
				return row;
			} else {
				return super.get(index);
			}
		} catch (Exception e) {
			row = new MyRow();
			return row;

		}

	}
	//深度复制方法,需要对象及对象所有的对象属性都实现序列化
	public MyData dataClone(){
		MyData data = null;
		try {
			// 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			//将流序列化成对象
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			data = (MyData) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}
	

}
