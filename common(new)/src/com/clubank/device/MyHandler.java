package com.clubank.device;

import android.os.Handler;
import android.os.Message;

/**
 * 基础Handler 供子类调用，因为Handler现在必须是静态的变量。
 * 
 * @author chenyh
 * 
 */
public class MyHandler extends Handler {
	private int type;
	private BaseActivity a;

	public MyHandler(int type, BaseActivity a) {
		this.type = type;
		this.a = a;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		a.updateUI(type, msg);
	}

}
