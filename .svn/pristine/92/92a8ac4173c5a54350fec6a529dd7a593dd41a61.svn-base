package com.clubank.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubank.dining.R;

import java.util.ArrayList;

/**
 * Created by long on 17-6-1.
 */

public class EListAdapter extends BaseExpandableListAdapter{

    //    MyRow row;
//    ArrayList<String> typeName, flaveType;
    Context context;
    MyData allFlavorData;
    private onChildCheckChangeListener listener;

    public EListAdapter(MyData allFlavorData, Context context, onChildCheckChangeListener
            listener) {
//        this.row = row;
//        this.typeName = typeName;
//        this.flaveType = flaveType;
        this.context = context;
        this.listener = listener;
        this.allFlavorData = allFlavorData;
    }

    @Override
    public int getGroupCount() {
        return allFlavorData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = ((MyData) allFlavorData.get(groupPosition).get("data")).size();
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
//        return  groupPosition;
        return allFlavorData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

//        return childPosition;
        return  ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
        return Long.parseLong(groupPosition+""+childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.elist_group,null);
            groupHolder = new GroupHolder();
            groupHolder.title = (TextView) convertView.findViewById(R.id.elist_group_tv);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.title.setText(allFlavorData.get(groupPosition).getString("TypeName"));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean
            isLastChild, View convertView, ViewGroup parent) {
//        ChildHolder childHolder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.elist_child,null);
//            childHolder = new ChildHolder();
//            childHolder.name = (TextView) convertView.findViewById(R.id.elist_child_tv);
//            childHolder.cbox = (CheckBox) convertView.findViewById(R.id.elist_child_cbox);
//            childHolder.childll = (LinearLayout) convertView.findViewById(R.id.elist_child_ll);
//            convertView.setTag(childHolder);
//        } else {
//            childHolder = (ChildHolder) convertView.getTag();
//        }
//        ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getString("Name");
//        childHolder.name.setText( ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getString("Name"));
//        boolean select = ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getBoolean("selected");
//        childHolder.cbox.setChecked(select);
//        childHolder.cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (listener != null) {
//                    listener.childCheckedChange(groupPosition, childPosition, isChecked);
//                }
//            }
//        });
//        final ChildHolder finalChildHolder = childHolder;
//        childHolder.childll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finalChildHolder.cbox.setChecked(!finalChildHolder.cbox.isChecked());
//            }
//        });

        View view = LayoutInflater.from(context).inflate(R.layout.elist_child,null);
        final ChildHolder childHolder1= new ChildHolder();
        childHolder1.name = (TextView) view.findViewById(R.id.elist_child_tv);
        childHolder1.cbox = (CheckBox) view.findViewById(R.id.elist_child_cbox);
        childHolder1.childll = (LinearLayout) view.findViewById(R.id.elist_child_ll);
        ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getString("Name");
        childHolder1.name.setText( ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getString("Name"));
        boolean select1 = ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).getBoolean("selected");
        childHolder1.cbox.setChecked(select1);
        childHolder1.cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.childCheckedChange(groupPosition, childPosition, isChecked);
                }
            }
        });
        childHolder1.childll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childHolder1.cbox.setChecked(!childHolder1.cbox.isChecked());
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder {
        public TextView title;
    }

    class ChildHolder {
        public TextView name;
        public CheckBox cbox;
        public LinearLayout childll;
    }

    public interface onChildCheckChangeListener {
        void childCheckedChange(int groupPosition, int childPosition, boolean isChecked);
    }

//    @Override
//    public long getCombinedGroupId(long groupId) {
//        return groupId;
//    }
//
//    @Override
//    public long getCombinedChildId(long groupId, long childId) {
//        return Long.parseLong(groupId+""+childId);
//    }


    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

}
