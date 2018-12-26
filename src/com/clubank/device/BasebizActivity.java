package com.clubank.device;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetBillItemByID;
import com.clubank.op.SetDelivered;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;
import com.zxing.android.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author jishu0416 头部扫描触发事件及扫描结果的处理，底部menu加载和响应事件，清除缓存文件等
 *
 */
public class BasebizActivity extends BaseActivity {
	private  int REQUEST_SCAN_DISH = 10201;
	private  int DLG_CONFIRM_SCAN_DELIVER = 10231;
	private int saleId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d("----------", "onCreate: "+this.getClass().getSimpleName());
	}

	@Override
	protected void clearSetting() {
		super.clearSetting();
		Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
		editor.remove("userCode");
		editor.remove("password");
		editor.remove("rememberPassword");
		editor.remove("popupQtyWindow");
		editor.remove("quickInputLockNumber");
		editor.commit();
		S.userCode = null;
		S.userName = null;
		S.token = null;
		S.shopName = null;
		S.shopCode = null;
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == GetBillItemByID.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				MyRow o = (MyRow) result.obj;
				if (o == null) {
					String msg = getString(R.string.msg_invalid_scanned_code);
					msg = String.format(msg, saleId);
					UI.showError(this, msg);
					return;
				}
				String msg = getString(R.string.lbl_confirm_scan_deliver);
				msg = String.format(msg, o.getString("ItemName"), o.getString("TableName"));
				UI.showOKCancel(this, DLG_CONFIRM_SCAN_DELIVER, msg, getText(R.string.lbl_confirm), o.getInt("SaleID"));
			}
		} else if (op == SetDelivered.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				MyRow row = (MyRow) result.obj;
				boolean scanFlag = row.getBoolean("scanFlag");
				if (scanFlag) {
					Toast.makeText(this, R.string.set_deliver_success, Toast.LENGTH_SHORT).show();
				}
			}
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_SCAN_DISH) {
			if (resultCode == RESULT_OK) {
				Bundle b = data.getExtras();
				String c = b.getString("content");
				int n = c.length();
				if (!Character.isDigit(c.charAt(n - 1))) {
					c = c.substring(0, n - 1);
				}
				if (!U.isInteger(c)) {
					UI.showError(this, R.string.msg_invalid_scanned_code);
					return;
				}
				saleId = Integer.valueOf(c);
				if (!BC.engineerMode){
					new MyAsyncTask(this, GetBillItemByID.class).execute(saleId);
				}
			} else {
				Toast.makeText(this, R.string.scan_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void processDialogOK(int type, final Object tag) {
		super.processDialogOK(type, tag);
		if (type == DLG_CONFIRM_SCAN_DELIVER) {
			int saleId = (Integer) tag;
			if (!BC.engineerMode){
				new MyAsyncTask(this, SetDelivered.class).execute(saleId, true, true);
			}
		}
	}

	protected void openScanner() {
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivityForResult(intent, REQUEST_SCAN_DISH);
	}

	public void openScanner(View view) {
		openScanner();
	}

	@SuppressWarnings("deprecation")
	public void showMenu(final View view) {
		if (menus == null || menus.length == 0) {
			return;
		}
		View v = getLayoutInflater().inflate(R.layout.popwindow, null);
		ListView listView1 = (ListView) v.findViewById(R.id.PopWindow_lv);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		final PopupWindow popupWindow = new PopupWindow(v, width / 2, height);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		int n = menus.length;
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				int imageId = UI.getId(this, menus[i], "drawable");
				CharSequence name = UI.getText(this, menus[i]);
				item.put("image", imageId);
				item.put("name", name);
				data.add(item);
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.pop_item_light, new String[] { "image", "name" },
				new int[] { R.id.image, R.id.name });
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				menuSelected(view, arg2);
				popupWindow.dismiss();
			}
		});

		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			// (View) view.getParent().getParent()得到父容器，然后将popupWindow放在这个位置。
			popupWindow.showAsDropDown((View) view.getParent().getParent(), width / 2, 0);
		}
	}

	public void menuSelected(View view, int index) {
	}

}
