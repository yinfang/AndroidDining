package com.clubank.device;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

public class DishCommentAdapter extends ArrayAdapter<MyRow> {
    public DishCommentAdapter(Context context, int resId, MyData data) {
	super(context, resId, data);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
	try {
		if(v==null){
			
	    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
		    Context.LAYOUT_INFLATER_SERVICE);
	   
	       v = vi.inflate(R.layout.item_comment, null);
		}

	    MyRow o = getItem(position);
	    if (o != null) {
		RadioButton notIdea = (RadioButton) v.findViewById(R.id.all);
		RadioButton pleased = (RadioButton) v.findViewById(R.id.pleased);
		RadioButton yawp = (RadioButton) v.findViewById(R.id.yawp);
		TextView commentSelectT = (TextView) v.findViewById(R.id.commentSelect);
		EditText otherCommentE = (EditText) v.findViewById(R.id.otherComment);
		pleased.setId(position + 1);
		yawp.setId(position + 4);
		commentSelectT.setId(position);
		notIdea.setId(position + 3);
		otherCommentE.setId(position + 2);
		TextView name = (TextView) v.findViewById(R.id.name);
		TextView quantity = (TextView) v.findViewById(R.id.quantity);
		name.setText(o.getString("ItemName"));
		String quantityV = o.getString("Quantity");
		quantity.setText(quantityV.substring(0, quantityV.indexOf(".")));
		String otherComment = o.getString("Comments");
		String commentSelect = o.getString("commentSelect");
		String notIdeaS = o.getString("notIdea");

		if (null != notIdeaS && notIdeaS.equals("ture")) {
		    commentSelectT.setText(getContext().getString(
			    R.string.lbl_select));
		    otherCommentE.setText("");
		    notIdea.setChecked(true);
		    yawp.setChecked(false);
		    pleased.setChecked(false);
		    commentSelectT.setClickable(false);
		    editextFocsAble(otherCommentE, false, false);
		    return v;
		} else if (o.getBoolean("Satisfied")) {
		    commentSelectT.setText(getContext().getString(
			    R.string.lbl_select));
		    otherCommentE.setText("");
		    pleased.setChecked(true);
		    yawp.setChecked(false);
		    notIdea.setChecked(false);
		    commentSelectT.setClickable(false);
		    editextFocsAble(otherCommentE, false, false);
		} else {
		    yawp.setChecked(true);
		    pleased.setChecked(false);
		    notIdea.setChecked(false);
		    commentSelectT.setClickable(true);
		    editextFocsAble(otherCommentE, false, false);
		
		    if (null != commentSelect) {
			if (commentSelect.equals(getContext().getString(
				R.string.lbl_other_reason))) {
			    editextFocsAble(otherCommentE, true, true);
			} else {
			    editextFocsAble(otherCommentE, false, false);
			}
			commentSelectT.setText(commentSelect);
		    }
		    if(!o.getBoolean("isSelect")){
			  if (null != otherComment) {
				otherCommentE.setText(otherComment);
			    }
		    }
			  
		    
		}

	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return v;
    }

    private void editextFocsAble(EditText edit, boolean focus, boolean keyboard) {
	edit.setEnabled(focus);
	edit.setFocusable(focus);
	edit.setFocusableInTouchMode(focus);
	if (focus) {
	    edit.requestFocus();
	} else {
	    edit.setText("");
	}

    }

}
