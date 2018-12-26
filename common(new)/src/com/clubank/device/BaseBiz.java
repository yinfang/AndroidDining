package com.clubank.device;

public abstract class BaseBiz implements IBiz {

	public int loginType;
	protected BaseActivity ba;

	public BaseBiz(BaseActivity ba) {
		this.ba = ba;
	}

	public int getLoginType() {
		return loginType;
	}

}
