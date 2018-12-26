package com.clubank.device;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.CommentsArray;
import com.clubank.domain.DishComments;
import com.clubank.domain.Result;
import com.clubank.op.GetBillByCode;
import com.clubank.op.GetDishCommentsReason;
import com.clubank.op.SaveDishComments;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

public class DishCommentActivity extends BasebizActivity {

	public DishCommentAdapter adapter;
	private MyData dishData;
	private String[] yawps;
	private final int DLAG_DISH_COMMENT = 100;
	private final int DLAG_DISH_COMMENT_OK = 101;
	private CheckBox allPleased;
	private CheckBox allYawp;
	private boolean itemCheck;
	private CommentsArray commentsArray;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);
		initView();
		refreshData();
	}

	private void initView() {
		allPleased = (CheckBox) findViewById(R.id.all_pleased);
		allYawp = (CheckBox) findViewById(R.id.all_yawp);
		allPleased.setChecked(true);
		// 全选（满意）事件监听
		allPleased
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							fillListView(true);
							itemCheck = false;
							allYawp.setChecked(false);
						} else if (!allYawp.isChecked()) {
							if (!itemCheck) {
								allComment();
							}
						}
					}
				});
		// 全选（不满意）事件监听
		allYawp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					fillListView(false);
					itemCheck = false;
					allPleased.setChecked(false);
				} else if (!allPleased.isChecked()) {
					if (!itemCheck) {
						allComment();
					}

				}
			}
		});
	}

	private void getList() {
		adapter = new DishCommentAdapter(this, R.layout.item_comment, dishData);
		ListView	listView = (ListView) findViewById(R.id.dish_list);
		listView.setAdapter(adapter);
	}

	public void commentSelect(View view) {
		showListDialog(view, yawps);
	}

	@Override
	public void listSelected(View view, int index) {
		super.listSelected(view, index);

		int id = view.getId();
	int dishSize=dishData.size();
		for (int i = 0; i < dishSize; i++) {
			if (i == id) {
				MyRow row = dishData.get(id);
				row.put("commentSelect", yawps[index]);
				dishData.set(i, row);
			}
		}
		adapter.notifyDataSetChanged();
	}

	public void otherReason(View view) {

		try {

			final EditText editText = (EditText) view;
			editText.requestFocus();
			editText.setEnabled(true);
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == KeyEvent.KEYCODE_ENDCALL) {
						int postion = (Integer) editText.getId() - 2;
						MyRow row = dishData.get(postion);
						row.put("Comments", editText.getText().toString());
						dishData.set(postion, row);
						adapter.notifyDataSetChanged();
						editText.setEnabled(false);
						editText.setFocusable(false);
						editText.setFocusableInTouchMode(false);
					}

					return false;
				}

			});
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// 单条菜品（满意）复选框事件
	public void itemPleased(View view) {
		setAllChecked();
		changeCommentChoice(view.getId() - 1, true);
	}

	// 单条菜品（不满意）复选框事件
	public void itemYawp(View view) {
		setAllChecked();
		changeCommentChoice(view.getId() - 4, false);
	}

	private void changeCommentChoice(int id, boolean isPleased) {
		MyRow items = dishData.get(id);
		if (null != items.getString("notIdea")) {
			if (items.getString("notIdea").equals("ture")) {
				items.put("Satisfied", isPleased);
				items.put("notIdea", "false");
				return;
			}
		}
		if (items.getBoolean("Satisfied")) {
			if (isPleased) {
				items.put("notIdea", "ture");
			} else {
				items.put("Satisfied", isPleased);
			}
			// 不满意
		} else {

			if (isPleased) {
				items.put("Satisfied", isPleased);
			} else {
				items.put("notIdea", "ture");
			}
		}
		dishData.set(id, items);
		adapter.notifyDataSetChanged();
	}

	private void setAllChecked() {
		itemCheck = true;
		allPleased.setChecked(false);
		allYawp.setChecked(false);
	}

	public void refreshData() {
		// 获取失败原因数据
		new MyAsyncTask(this, GetDishCommentsReason.class).execute();
	}

	private void fillListView(boolean ispleased) {
		int dishSize=dishData.size();
		for (int i = 0; i < dishSize; i++) {
			MyRow items = dishData.get(i);
			items.put("Satisfied", ispleased);
			items.put("notIdea", "false");
			dishData.set(i, items);
		}
		adapter.notifyDataSetChanged();
	}

	private void allComment() {
		int dishSize=dishData.size();
		for (int i = 0; i < dishSize; i++) {

			MyRow items = dishData.get(i);
			items.put("notIdea", "ture");
			dishData.set(i, items);
		}
		adapter.notifyDataSetChanged();
	}

	public void submit(View view) {
		int notIdea = 0;
		commentsArray = new CommentsArray();
		int dishSize=dishData.size();
		for (int i = 0; i < dishSize; i++) {
			MyRow row = dishData.get(i);
			DishComments dishComments = new DishComments();
			dishComments.BillCode = getIntent().getStringExtra("BillCode");
			String notIdeaV = row.getString("notIdea");
			if (null != notIdeaV && notIdeaV.equals("ture")) {
				notIdea++;
				if (notIdea == dishData.size()) {
					UI.showError(this, getString(R.string.msg_notMsgSubmit));
					return;
				}
			} else {
				dishComments.SaleId = row.getInt("SaleID");
				if (allPleased.isChecked()) {
					dishComments.Satisfied = true;
					commentsArray.add(dishComments);
					continue;
				} else if (allYawp.isChecked()) {
					dishComments.Satisfied = false;
				} else {
					dishComments.Satisfied = row.getBoolean("Satisfied");
				}
				if (!row.getBoolean("Satisfied")) {
					dishComments.Satisfied = false;
					String yawpReason = row.getString("commentSelect");
					if (null != yawpReason
							&& yawpReason
									.equals(getString(R.string.lbl_other_reason))) {
						dishComments.Comments = row.getString("Comments");
					} else if (null != yawpReason) {
						dishComments.Comments = row.getString("commentSelect");
					}
				} else {
					dishComments.Satisfied = true;
					dishComments.Comments = "";
				}

				commentsArray.add(dishComments);

			}

		}

		UI.showOKCancel(this, DLAG_DISH_COMMENT, R.string.msg_submit_confirm,
				R.string.lbl_dishes_comment);
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == GetBillByCode.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				MyRow row = (MyRow) result.obj;
				dishData = (MyData) row.get("BillItems");
				if (null != dishData && dishData.size() > 0) {
					int i = 0;
					for (MyRow dishrow : dishData) {
					int lenght=yawps.length;
						for (int j = 0; j < lenght; j++) {
							if (null != dishrow.getString("Comments")
									&& !dishrow.getString("Comments")
											.equals("")) {
								if (dishrow.getString("Comments").equals(
										yawps[j])) {
									dishrow.put("commentSelect",
											dishrow.getString("Comments"));
									dishrow.put("isSelect", true);
									break;
								} else {
									dishrow.put("isSelect", false);
									dishrow.put(
											"commentSelect",
											getString(R.string.lbl_other_reason));
								}
							}
						}
						if (!dishrow.getBoolean("Satisfied") && i != 1) {
							setAllChecked();
							i++;
						}
					}
					getList();
				} else {
					UI.showInfo(this, getString(R.string.msg_notComment),
							DLAG_DISH_COMMENT_OK);
				}

			}

		} else if (op == GetDishCommentsReason.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				String row = result.obj.toString();
				// anyType{string=味道太重; string=材料不新鲜; }
				String str = row.replace("anyType{", "").replace("}", "")
						.replace("string=", "").replaceAll(" ", "");
				str = str + getString(R.string.lbl_other_reason);
				yawps = str.split(";");
				new MyAsyncTask(this, GetBillByCode.class).execute(getIntent()
						.getStringExtra("BillCode"));
			}
		} else if (op == SaveDishComments.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				UI.showInfo(this, getString(R.string.lbl_SubmitOk),
						DLAG_DISH_COMMENT_OK);
			}
		}
	}

	@Override
	public void processDialogOK(int type, Object tag) {
		super.processDialogOK(type, tag);
		if (type == DLAG_DISH_COMMENT) {
			new MyAsyncTask(this, SaveDishComments.class)
					.execute(commentsArray);
		} else if (type == DLAG_DISH_COMMENT_OK) {
			finish();
		}
	}

	// 屏幕旋转时调用此方法

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

}
