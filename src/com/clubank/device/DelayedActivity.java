package com.clubank.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetDelayedDish;
import com.clubank.op.RemindDish;
import com.clubank.op.SetDelivered;
import com.clubank.op.WithdrawDish;
import com.clubank.util.CustomDialogView;
import com.clubank.util.CustomDialogView.Initializer;
import com.clubank.util.CustomDialogView.OKProcessor;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

import java.util.Date;

/**
 * @author jishu0416 菜品延迟
 */
public class DelayedActivity extends BasebizActivity {

	private MyData areas;
	private String[] areaNames;
	private IconContextMenu m;
	private int pos;
	private DelayedAdapter sa;
	private int DLG_WITHDRAW = 1;
	private   int CMENU_DISH = 1;
	private final int MENU_REMIND_DISH = 5;
	private final int MENU_SET_DELIVERED = 6;
	private  final int MENU_WITHDRAW_DISH = 7;
	private MyData list = new MyData();
	private String[] tableNames = new String[0];
	private MyData tables;
	private String tableNo = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delayed);
		String sql = "select AreaCode,AreaName from T_DiningArea where shopcode=?";
		areas = db.getData(sql, new String[] { S.shopCode });
		areaNames = new String[areas.size()];
		int size= areas.size();
		for (int i = 0; i < size; i++) {
			areaNames[i] = (String) areas.get(i).get("AreaName");
		}
		findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
		menus = new String[] { "menu_area", "menu_refresh" };
		loadTable();
		sa = new DelayedAdapter(this, R.layout.lvi_delayed, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(sa);
		listView.setOnItemClickListener((itemLongClickHandler));
		if(S.areaPosition<areas.size()){
		setEText(R.id.areaName, areas.get(S.areaPosition).getString("AreaName"));
		}
		refreshData();
	}

	public void delayed(View view) {
		if (areas.size() > 0) {
			refreshData();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		findViewById(R.id.menu).setVisibility(View.GONE);
	}

	@Override
	public void menuSelected(View view, int index) {
		if (index == 0) {
			showListDialog(view, areaNames);
		} else if (index == 1) {// refresh
			refreshData();
		}
	}

	public void refreshData() {
		new MyAsyncTask(this, GetDelayedDish.class).execute(S.shopCode, areas.get(S.areaPosition).getString("AreaCode"),
				tableNo);
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == GetDelayedDish.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				MyData data = (MyData) result.obj;
				sa.clear();
				for (MyRow row : data) {
					sa.add(row);
				}
				sa.notifyDataSetChanged();
			}
		} else if (op == WithdrawDish.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				UI.showInfo(this, R.string.msg_withdraw_dish_success);
				refreshData();
			}
		} else if (op == RemindDish.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				UI.showInfo(this, R.string.msg_remind_dish_success);
			}
		} else if (op == SetDelivered.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				refreshData();
			}
		}
	}

	private OnItemClickListener itemLongClickHandler = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			pos = position;
			Dialog d = createDialog(CMENU_DISH, position);
			if (d != null) {
				d.show();
			}

		}
	};

	private class DelayedAdapter extends ArrayAdapter<MyRow> {
		public DelayedAdapter(Context context, int resId, MyData data) {
			super(context, resId, data);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lvi_delayed, null);
			}

			MyRow o = getItem(position);
			if (o != null) {
				TextView t1 = (TextView) v.findViewById(R.id.dl_dish_name);
				TextView t2 = (TextView) v.findViewById(R.id.table_no);
				TextView t4 = (TextView) v.findViewById(R.id.sale_date);
				TextView t5 = (TextView) v.findViewById(R.id.delayed);
				ImageView t6 = (ImageView) v.findViewById(R.id.imageView1);
				ImageView t7 = (ImageView) v.findViewById(R.id.item_status);
				TextView t8 = (TextView) v.findViewById(R.id.dl_saleId);
				TextView t9 = (TextView) v.findViewById(R.id.bill_code);
				t8.setText(o.getString("SaleID"));
				t9.setText(o.getString("BillCode"));

				t1.setText(o.getString("ItemName"));
				t2.setText(o.getString("TableName"));
				t4.setText(o.getString("OpenTime").substring(11, 16));
				long t = 0;
				try {
					t = BC.df_yMdHms.parse(o.getString("OpenTime")).getTime();
				} catch (Exception e) {
				}
				int delayed = (int) ((new Date().getTime() - t) / 1000 / 60);
				t5.setText("" + delayed);
				byte[] b = (byte[]) o.get("SmallPicture");
				if (b == null) {
					b = BU.getSmallPicture(DelayedActivity.this, o.getString("ItemCode"));
				}
				if (b != null) {
					setImage(t6, b,true);
					o.put("SmallPicture", b);
				} else {

					setImage(t6,R.drawable.zhuye_13,true);
				}
				if (o.getInt("Printtimes") > 0) {
					t7.setImageResource(R.drawable.bs_printed);
				} else {
					t7.setImageResource(R.drawable.bs_submitted);
				}
			}
			return v;
		}
	}

	private Dialog createDialog(int id, Object tag) {
		if (id == CMENU_DISH) {
			final MyRow bi = list.get(pos);
			m = new IconContextMenu(this, tag);
			m.add(R.string.menu_remind_dish, R.drawable.reminddish, MENU_REMIND_DISH);
			m.add(R.string.menu_set_delivered, R.drawable.delivered, MENU_SET_DELIVERED);
			m.add(R.string.menu_withdraw, R.drawable.withdrawdish, MENU_WITHDRAW_DISH);
			m.setOnClickListener(new IconContextMenuOnClickListener() {
				@Override
				public void onClick(int menuId) {

					switch (menuId) {
					case MENU_REMIND_DISH:
						new MyAsyncTask(DelayedActivity.this, RemindDish.class).execute(bi.getInt("SaleID"));
						break;
					case MENU_SET_DELIVERED:
						bi.put("Delivered", true);
						new MyAsyncTask(DelayedActivity.this, SetDelivered.class).execute(bi.getInt("SaleID"), true);
						sa.notifyDataSetChanged();
						break;
					case MENU_WITHDRAW_DISH:
						// TODO CUSTOMDIALOG
						if (BC.type == 2) {
							showCustomDialog(DLG_WITHDRAW, bi.getString("ItemName"), R.layout.withdraw_text_entry, -1);
						} else {
							showCustomDialog(DLG_WITHDRAW, bi.getString("ItemName"), R.layout.text_entry, -1);
						}
						break;
					}
				}
			});
			return m.createMenu(bi.getString("ItemName"));
		}
		return null;
	}

	public void showCustomDialog(final int type, CharSequence title, int layout, int resIcon) {

		CustomDialogView cd = new CustomDialogView(this);
		cd.setInitializer(new Initializer() {
			public void init(View view) {
				initDialog(type, view);
			}
		});
		cd.setOKProcessor(new OKProcessor() {
			public boolean process(Dialog ad) {
				return finishDialog(type, ad);
			}
		});
		cd.show(title, layout, resIcon);

	}

	@Override
	public void processDialogOK(int type, Object tag) {
		super.processDialogOK(type, tag);
		if (type == DLG_WITHDRAW) {
			double quantity;
			String reason;
			if (BC.type == 2) {// 安吉轩加退菜原因
				MyRow ro = (MyRow) tag;
				quantity = (Double) ro.getDouble("quantity");
				reason = ro.getString("reason");
			} else {
				quantity = (Double) tag;
			}

			MyRow bi = list.get(pos);
			int saleId = bi.getInt("SaleID");
			if (BC.type == 2) {
				new MyAsyncTask(this, WithdrawDish.class).execute(saleId, quantity, reason);
			} else {
//				int qua = (int) quantity;
				new MyAsyncTask(this, WithdrawDish.class).execute(saleId, quantity, "Cancelled");
			}
			refreshData();
		}
	}

	public void loadTable() {
		String areaCode = (String) areas.get(S.areaPosition).get("AreaCode");
		String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
		tables = db.getData(sql, new String[] { areaCode });
		int size= tables.size();
		tableNames = new String[size+ 1];
		tableNames[0] = getString(R.string.msg_all);
		for (int i = 0; i <size; i++) {
			tableNames[i + 1] = (String) tables.get(i).get("Name");
		}
	}

	public void selectTable(View view) {
		showListDialog(view, tableNames);
	}

	@Override
	public void listSelected(View view, int index) {
		int id = view.getId();
		if (id == R.id.TableName) {
			if (index > 0) {
				if(tables.size()>0){
					setEText(R.id.TableName, tables.get(index - 1).getString("Name"));
					tableNo = tables.get(index - 1).getString("Code");
				}
				
			} else {
				tableNo = "";
				setEText(R.id.TableName, getString(R.string.lbl_all));
			}

		} else if (id == R.id.header_menu) {
			S.areaPosition = index;
		
			setEText(R.id.areaName, areas.get(index).getString("AreaName"));
			tableNo = "";
			setEText(R.id.TableName, getString(R.string.lbl_all));
			loadTable();
			refreshData();
		}
	}

	protected void initDialog(int type, View view) {
		if (type == DLG_WITHDRAW) {
			final MyRow bi = list.get(pos);
			EditText input = (EditText) view.findViewById(R.id.textEntryValue);
			input.setText(bi.getString("Quantity"));
		}
	}

	protected boolean finishDialog(int type, Dialog dialog) {
		if (type == DLG_WITHDRAW) {
			TextView tv = (TextView) dialog.findViewById(R.id.te_quantity);
			tv.setText(getText(R.string.msg_withdraw_quantity));
			final MyRow bi = list.get(pos);
			EditText te = (EditText) dialog.findViewById(R.id.textEntryValue);
			Double quantity = Double.parseDouble(te.getText().toString());
			MyRow ro = new MyRow();
			if (type == 2) {
				EditText re = (EditText) dialog.findViewById(R.id.withdraw_reason);
				String reason = re.getText().toString();
				ro.put("quantity", quantity);
				ro.put("reason", reason);
			}
			if (quantity > 0 && quantity <= bi.getDouble("Quantity")) {
				String msg1 = String.format(getText(R.string.msg_confirm_withdraw).toString(), quantity,
						bi.getString("ItemName"));
				if (BC.type == 2) {
					UI.showOKCancel(DelayedActivity.this, DLG_WITHDRAW, msg1, getText(R.string.msg_confirm), ro);
				} else {
					UI.showOKCancel(DelayedActivity.this, DLG_WITHDRAW, msg1, getText(R.string.msg_confirm), quantity);
				}

			}
		}
		return true;
	}

}
