package com.clubank.device;

import android.view.Menu;
import android.view.MenuItem;


import com.clubank.common.R;
import com.clubank.domain.C;
import com.clubank.util.UI;

public class SwitchDebugActivity extends BaseActivity{
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int i = Menu.FIRST;
		i++;
		menu.add(Menu.NONE, i, 1, getString(R.string.engineer_mode)).setIcon(
				android.R.drawable.ic_dialog_info);
		i++;
		menu.add(Menu.NONE, i, 2, getString(R.string.normal_mode)).setIcon(
				android.R.drawable.ic_dialog_info);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			UI.showOKCancel(this, C.ENGINEER_MODE,
					R.string.confirm_engineer_mode, R.string.confirm);
			break;
		case Menu.FIRST + 2:
			hide(R.id.debug);
			biz.setServerType(C.SERVER_INTERNET);
			saveSetting("serverType", C.SERVER_INTERNET);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void processDialogOK(int type, Object tag) {
		super.processDialogOK(type, tag);
		if (type == C.ENGINEER_MODE) {
			show(R.id.debug);
			biz.setServerType(C.SERVER_INTRANET);
			saveSetting("serverType", C.SERVER_INTRANET);
		}
	}


}
