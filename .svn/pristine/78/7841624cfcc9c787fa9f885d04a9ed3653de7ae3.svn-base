package com.clubank.device;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Bill;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.ChangeTable;
import com.clubank.op.GetBillByCode;
import com.clubank.op.SaveBillInfo;
import com.clubank.op.GetBill;
import com.clubank.op.PrintBill;
import com.clubank.util.CustomDialogView;
import com.clubank.util.IconContextMenu;
import com.clubank.util.IconContextMenuOnClickListener;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.clubank.util.CustomDialogView.Initializer;
import com.clubank.util.CustomDialogView.OKProcessor;

/**
 * @author jishu0416 账单查询，账单一览
 */
public class BillListActivity extends BasebizActivity implements OnItemLongClickListener,
        OnItemClickListener {

    private final int CONTEXT_MENU_ID = 1;
    private final int MENU_COMMENT_BILL = 3;
    private final int MENU_CHANGE_BILL = 4;
    private final int MENU_CHANGE_INFO = 5;
    private int REQUEST_CHANGE_TABLE = 7;
    private int MENU_CHANGE_TABLE = 10;
    private String areaCode;
    private String date;
    private String tableCode;
    private boolean isMyself;
    private Bill2Adapter sa;
    private int pos;
    private MyData tables;
    private String queryStr;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_list);

        sa = new Bill2Adapter(this, new MyData());
        ListView listView = (ListView) findViewById(R.id.lvw_charge_list);
        listView.setAdapter(sa);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        Bundle b = getIntent().getExtras();
        areaCode = b.getString("areaCode");
        tableCode = b.getString("tableCode");
        date = b.getString("date");
        queryStr = b.getString("queryStr");
        isMyself = b.getBoolean("isMyself");
        findViewById(R.id.header_menu).setVisibility(View.VISIBLE);
        menus = new String[]{"menu_refresh"};
        setEText(R.id.condition, b.getString("condition"));
        loadTable();
        refreshData();
    }

    @Override
    public void menuSelected(View view, int index) {
        if (index == 0) {// refresh
            refreshData();
        }
    }

    private void loadTable() {
        String sql = "select Code,Name,areaCode from T_Diningtable where areacode=?";
        tables = db.getData(sql, new String[]{areaCode});
		/*
		 * tableNames = new String[tables.size() + 1]; tableNames[0] =
		 * getString(R.string.lbl_not_selected); for (int i = 0; i <
		 * tables.size(); i++) { tableNames[i + 1] = (String)
		 * tables.get(i).get("Name"); }
		 */
    }

    public void refreshData() {
        String user = null;
        if (isMyself) {
            user = S.userCode;
        }
        new MyAsyncTask(this, GetBill.class).execute(areaCode, date, tableCode, user, queryStr);
    }

    @Override
    public void onResume() {
        super.onResume();
        findViewById(R.id.menu).setVisibility(View.GONE);
        refreshData();
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == 33) {
            refreshData();
        }
    }

    private void fillListView(MyData bills) {
        if (bills.size() > 0) {
            sa.clear();
            for (MyRow row : bills) {
                sa.add(row);
            }
            sa.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_TABLE && resultCode == RESULT_OK) {
            String newTableNo = data.getExtras().getString("newTableNo");
            String billName = data.getExtras().getString("billName");
			/*
			 * for (int i = 0; i < tables.size(); i++) { if
			 * (tables.get(i).getString("Code").equals(newTableNo)) {
			 * tablePosition = i; setEText(R.id.TableName,
			 * tables.get(i).getString("Name")); break; } } String billCode =
			 * tablePending.get(pendingIndex).getString( "BillCode");
			 */
            new MyAsyncTask(this, ChangeTable.class).execute(billName, newTableNo);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
        this.pos = pos;
        MyRow row = sa.getItem(pos);
        // not comment
        if (row.getInt("BillStatus") != 0) {
            // 结账
            Dialog d = createDialog(CONTEXT_MENU_ID, pos);
            if (d != null) {
                d.show();

            }
        }

        return false;
    }

    private Dialog createDialog(int id, Object tag) {

        final MyRow row = sa.getItem(pos);
        final String billCode = row.getString("BillCode");
        if (id == CONTEXT_MENU_ID) {
            IconContextMenu m = new IconContextMenu(this, tag);
            m.add(R.string.lbl_dishes_comment, R.drawable.menu_orderquickorder, MENU_COMMENT_BILL);
            m.add(R.string.menu_change_table, R.drawable.menu_change_table, MENU_CHANGE_TABLE);
            m.setOnClickListener(new IconContextMenuOnClickListener() {
                @Override
                public void onClick(int menuId) {
                    if (menuId == MENU_COMMENT_BILL) {
                        Intent intent = new Intent(BillListActivity.this, DishCommentActivity
								.class);
                        intent.putExtra("title", row.getString("TableName") + "(" + row.getString
								("BillCode") + ")");
                        intent.putExtra("BillCode", billCode);
                        startActivity(intent);
                    } else if (menuId == MENU_CHANGE_BILL) {
                        changeRemark(row);
                    } else if (menuId == MENU_CHANGE_TABLE) {
                        Intent intent = new Intent(BillListActivity.this, ChangeTableActivity
								.class);
						/*
						 * intent.putExtra("oldCode",
						 * tables.get(tablePosition).getString("Code"));
						 * intent.putExtra("oldName",
						 * tables.get(tablePosition).getString("Name"));
						 */
                        intent.putExtra("oldCode", tables.get(pos).getString("Code"));
                        intent.putExtra("oldName", row.getString("TableName"));
                        intent.putExtra("title", getText(R.string.menu_change_table));
                        intent.putExtra("billName", row.getString("BillCode"));
                        intent.putExtra("guestName", row.getString("TotalGuest"));
                        startActivityForResult(intent, REQUEST_CHANGE_TABLE);
                    }
                }
            });
            return m.createMenu(row.getString("TableName") + "(" + row.getString("BillCode") + ")");
        }
        return null;
    }

    private void changeRemark(final MyRow row) {
        CustomDialogView cd = new CustomDialogView(BillListActivity.this);
        cd.setInitializer(new Initializer() {
            public void init(View view) {
                changeRamark(view, row);
            }
        });
        cd.setOKProcessor(new OKProcessor() {
            public boolean process(Dialog ad) {
                return change(ad, row);
            }
        });
        cd.show(row.getString("TableName") + "(" + row.getString("BillCode") + ")", R.layout
				.changeremark, -1);
    }

    private void changeRamark(View view, MyRow row) {
        EditText et = (EditText) view.findViewById(R.id.change_remark);
        et.setText(row.getString("DiningRemarks"));
        EditText card = (EditText) view.findViewById(R.id.cardno);
        card.setText(row.getString("CardNo"));
    }

    private boolean change(Dialog ad, MyRow row) {
        EditText et = (EditText) ad.findViewById(R.id.change_remark);
        EditText card = (EditText) ad.findViewById(R.id.cardno);
        // TODO 改单头

        new MyAsyncTask(BillListActivity.this, SaveBillInfo.class).run(et.getText().toString(),
                row.getString("BillCode"));
        return true;
    }

    public void printBill(View view) {
        String billCode = (String) view.getTag();
        new MyAsyncTask(this, PrintBill.class).execute(billCode, S.userCode);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MyRow row = (MyRow) parent.getItemAtPosition(position);
        S.bill = new Bill();
        new MyAsyncTask(this, GetBillByCode.class).execute(row.getString("BillCode"));

    }

    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == GetBill.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyData bills = (MyData) result.obj;
                setEText(R.id.bill_count, bills.size() + "");
                fillListView(bills);
            }
        } else if (op == PrintBill.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.sent_print_cmd);
            }
        } else if (op == SaveBillInfo.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                refreshData();
            }
        } else if (op == ChangeTable.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                UI.showInfo(this, R.string.msg_operation_success, 33);
            }
        } else if (op == GetBillByCode.class) {
            if (result.code == BC.RESULT_SUCCESS) {
                MyRow row = (MyRow) result.obj;
                MyData items = (MyData) row.get("BillItems");
                if (items != null && items.size() > 0) {
                    BU.fillBillItems(items);
                }
                BU.fillBill(row);
                S.orderMode = true;
                S.modified = false;
                Intent intent = new Intent(this, OrderOrderedActivity.class);
                intent.putExtra("from", "BillList");
                intent.putExtra("BillStatus", row.getInt("BillStatus"));
                startActivity(intent);
            }
        }
    }

}
