package com.clubank.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.clubank.dining.R;

public class MyWaitingDialog extends Dialog {

	public MyWaitingDialog(Context context) {
		super(context, R.style.WaitingDialog);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.width = UI.getPixel(context, 50);
		lp.height = UI.getPixel(context, 50);
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.my_progressbar, null);
		if (Build.VERSION.SDK_INT >= 15) {//4.0
			
		}
		addContentView(v, lp);
	}
}
