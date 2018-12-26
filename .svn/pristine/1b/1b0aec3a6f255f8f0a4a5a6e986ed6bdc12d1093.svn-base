package com.clubank.device;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.op.GetDiningPrinter;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
/**
 * 厨打监控
 * @author jishu0416
 *
 */
public class PrinterActivity extends BasebizActivity {
	private PrinterAdapter da;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printer);
		MyData list = new MyData();
		da = new PrinterAdapter(this, R.layout.lvi_printer, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(da);
		findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
		menus = new String[] { "menu_refresh" };
		refreshData();
	}

	@Override
	public void menuSelected(View view, int index) {
		if (index == 0) {// refresh
			refreshData();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		findViewById(R.id.menu).setVisibility(View.GONE);
	}
	
	public void refreshData() {
		new MyAsyncTask(this, GetDiningPrinter.class).execute();
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == GetDiningPrinter.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				MyData list = (MyData) result.obj;
				da.clear();
				for (int i = 0; i < list.size(); i++) {
					da.add(list.get(i));
				}
				da.notifyDataSetChanged();
			}
		}
	}

	private class PrinterAdapter extends ArrayAdapter<MyRow> {

		public PrinterAdapter(Context context, int resId, MyData data) {
			super(context, resId, data);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lvi_printer, null);
			}

			MyRow o = getItem(position);
			if (o != null) {
				TextView t1 = (TextView) v.findViewById(R.id.id);
				TextView t2 = (TextView) v.findViewById(R.id.name);
				TextView t3 = (TextView) v.findViewById(R.id.ip);
				TextView t4 = (TextView) v.findViewById(R.id.kitchen);
				TextView t5 = (TextView) v.findViewById(R.id.statusDesc);
				int status = Integer.valueOf((String) o.get("Status"));
				String s = (String) o.getString("StatusDesc");

				if (status != 0) {
					t2.setTextColor(Color.RED);
					t5.setTextColor(Color.RED);
				} else {
					t2.setTextColor(Color.WHITE);
					t5.setTextColor(Color.GREEN);
					s = getContext().getString(R.string.lbl_normal);
				}

				String n = o.getString("Name");
				if (n.length() > 28) {
					n = n.substring(0, 27) + "...";
				}
				String k = o.getString("Kitchen");
				t1.setText("" + o.get("ID"));
				t2.setText(n);
				t3.setText((String) o.get("IP"));
				t4.setText(k);
				t5.setText(s);
			}
			return v;
		}
	}

}
