package com.clubank.device;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * Created by long on 17-6-9.
 *
 */

public class DebugAdapter extends BaseAdapter {
    private MyData data;




    public DebugAdapter(MyData data){
        this.data = data;
    }

    public void addData(MyData datas){
        for (int i = 0; i < datas.size(); i++) {
            data.add(datas.get(i));
        }
        notifyDataSetChanged();
    }

    public void removeData(MyData datas){
        if (datas == null){
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            if (data.contains(datas.get(i))){
                data.remove(datas.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.debug_list_item,null);
            holder = new ViewHolder();
            holder.interfaceTv = (TextView) convertView.findViewById(R.id.interface_tv);
            holder.responseTv = (TextView) convertView.findViewById(R.id.response);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyRow row = data.get(position);

//        holder.interfaceTv.setText(row.keyAt(0));
//
//        String s = row.getString(row.keyAt(0));

//        setEColor(s,holder.responseTv,parent.getContext());
//        if (s.length()> 50){
//            holder.responseTv.setText(s.substring(0,49));
//        }else {
//            holder.responseTv.setText(s);
//        }

        return convertView;
    }

    class ViewHolder{
        TextView interfaceTv,responseTv;
    }


    private void setEColor(String s, TextView textView, Context context){
        if (s.startsWith("<?xml version=\"1.0\" encoding")){
            textView.setTextColor(context.getResources().getColor(R.color.green));
        }else {
            textView.setTextColor(context.getResources().getColor(R.color.red));
        }
    }
}
