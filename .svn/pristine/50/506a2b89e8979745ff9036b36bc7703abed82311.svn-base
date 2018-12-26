package com.clubank.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.clubank.common.R;
import com.clubank.device.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MEListDialog implements ExpandableListView.OnGroupExpandListener,ExpandableListView.OnGroupCollapseListener{
    private Context context;
    private Dialog mDialog;
    private OnPositiveListener onPositiveListener;
    private OnNeutralListener onNeutralListener;
    private OnNegativeListener onNegativeListener;

    @Override
    public void onGroupCollapse(int groupPosition) {

    }

    @Override
    public void onGroupExpand(int groupPosition) {

    }
//	private MyRow row;
//	private ArrayList<String> dataKey;

    public interface OnPositiveListener {
        void onSelected(View view, MyData allFlavorData);
    }

    public interface OnNeutralListener {
        void onSelected(View view,MyData allFlavorData);
    }

    public interface OnNegativeListener{
        void onSelected(View view,MyData allFlavorData);
    }

    public MEListDialog(Context context) {
        this.context = context;
    }

    public void setOnSelectedListener(OnPositiveListener onPositiveListener) {
        this.onPositiveListener = onPositiveListener;
    }

    public void setOnNeutralListener(OnNeutralListener onNeutralListener) {
        this.onNeutralListener = onNeutralListener;
    }
    public void setOnNegativeListener(OnNegativeListener onNegativeListener){
        this.onNegativeListener = onNegativeListener;
    }

    public void show(final View view, int resIdTitle, int thirdBtn, final MyData allFlavorData) {
//        final MyRow oldRow = groupRow.rowClone();
//        final MyData oldData = allFlavorData.dataClone();
        final Builder d = new Builder(context);
        BaseActivity ba = (BaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View elistView = inflater.inflate(com.clubank.dining.R.layout.elist_dialog, null);
        elistView.setMinimumHeight(ba.screen.getHeight());
        ExpandableListView expandableListView = (ExpandableListView) elistView.findViewById(com
                .clubank.dining.R.id.elist);
        expandableListView.setMinimumHeight(ba.screen.getHeight());
        expandableListView.setGroupIndicator(null);
        EListAdapter adapter = new EListAdapter(allFlavorData, context, new EListAdapter.onChildCheckChangeListener() {


            @Override
            public void childCheckedChange(int groupPosition, int childPosition, boolean
                    isChecked) {
                ((MyData) allFlavorData.get(groupPosition).get("data")).get(childPosition).put("selected",isChecked);
            }
        });
        expandableListView.setAdapter(adapter);
        d.setView(elistView);
        d.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int clicked) {
                if (onPositiveListener != null) {
                    onPositiveListener.onSelected(view, allFlavorData);
                }
                mDialog.dismiss();
            }
        });
        d.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (onNegativeListener != null){
//                            onNegativeListener.onSelected(view,oldData);
                        }
                        mDialog.dismiss();
                    }
                });
//        if (thirdBtn > 0) {
//            d.setNeutralButton(thirdBtn, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // mDialog.dismiss();
//                    if (onNeutralListener != null) {
//                        onNeutralListener.onSelected(view, selected);
//                    }
//                }
//            });
//        }

        mDialog = d.create();
        d.show();

    }
}
