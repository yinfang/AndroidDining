package com.clubank.util;

import android.support.v4.util.ArrayMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据量不大，最好在千级以内
 * 数据结构类型为Map类型
 *
 * @author fenyq
 */
public class MyRow extends HashMap<String, Object> implements Serializable {

    /*	public K keyAt(int index)
        public V valueAt(int index)*/
    private static final long serialVersionUID = 11245413897216L;

    public String getString(String name) {
        String ret = "";
        try {
            if (!containsKey(name)) {
                return "";
            } else {
                ret = get(name).toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int getInt(String name) {
        int ret = 0;
        try {
            if (!containsKey(name)) {
                return 0;
            } else {
                ret = Integer.parseInt(get(name).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public double getDouble(String name) {
        double ret = 0;
        try {
            if (!containsKey(name)) {
                return 0.00;
            } else {
                ret = Double.parseDouble(get(name).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean getBoolean(String name) {
        boolean ret = false;
        try {
            if (!containsKey(name)) {
                return false;
            } else {
                ret = Boolean.parseBoolean(get(name).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static MyRow fromMap(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        MyRow row = new MyRow();
        Iterator<?> it = map.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            row.put(key.toString(), map.get(key));
        }
        return row;
    }
//深度复制方法,需要对象及对象所有的对象属性都实现序列化
    public MyRow rowClone(){
        MyRow row = null;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            //将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            row = (MyRow) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return row;
    }

    public String keyAt(int pos){
        return  "";
    }

    public String valueAt(int pos){
        return  "";
    }
}
