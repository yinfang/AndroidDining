package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.ChargeOrder;
import com.clubank.op.VerifyConsumeCard;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.UI;

/**
 * OrderOrderedActivity --->  记账
 * 
 * @author jishu0416
 *
 */
public class ChargeBillActivity extends BasebizActivity {

	private final int DLG_CHARGE = 12;
	private final int DLG_CHARGE_SUCCESS = 13;
	private String cardNo;
	private int isError = 0;
	private int SEARCH_QUERY_CHECKIN = 66;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_bill);
		// String title = getString(R.string.menu_Charge) + "("
		// + S.bill.TableName + ")";
		// setHeaderTitle(title);

		setEText(R.id.TableName, S.bill.TableName);// 消费卡号
		setEText(R.id.cardNo, S.bill.CardNo);// 消费卡号
		if (S.bill.OpenTime != null && (S.bill.OpenTime).length() > 16) {
			String billname = S.bill.BillCode + " (" + S.bill.OpenTime.substring(11, 16) + ")";
			setEText(R.id.BillCode, billname);// 消费卡号
		}
	}

	public void SearchCarNo(View view) {  //查询
		Intent intent = new Intent(this, CheckinListTwo.class);
//		intent.putExtra("name", getEText(R.id.qo_guest_name));
//		intent.putExtra("memno", getEText(R.id.qo_mem_no));
		startActivityForResult(intent, SEARCH_QUERY_CHECKIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SEARCH_QUERY_CHECKIN && resultCode == RESULT_OK) {
//			String name = data.getStringExtra("name");
//			String memno = data.getStringExtra("memno");
			String cardno = data.getStringExtra("cardno");
			if (null != cardno) {
//				setEText(R.id.qo_guest_name, name);
//				setEText(R.id.qo_mem_no, memno);
				setEText(R.id.cardNo, cardno);
			}

		}
	}

	public void submitBill(View view) {
		EditText cardNoText = (EditText) this.findViewById(R.id.cardNo);
		cardNo = cardNoText.getText().toString();
		if ("".equals(cardNo)) {
			UI.showError(ChargeBillActivity.this, R.string.msg_no_card);
			return;
		}
		new MyAsyncTask(this, VerifyConsumeCard.class).execute(cardNo);
	}

	@Override
	public void processDialogOK(int type, Object tag) {
		if (type == DLG_CHARGE) {
			if (TextUtils.isEmpty(S.bill.BillCode) || TextUtils.isEmpty(S.bill.CardNo)) {
				UI.showToast(this, getString(R.string.error_hit));
				return;
			}
			new MyAsyncTask(this, ChargeOrder.class).execute(S.bill.BillCode, S.bill.CardNo, S.token);
		} else if (type == DLG_CHARGE_SUCCESS) {
			setResult(RESULT_OK);
			// BU.goBack(this);
			finish();
		} else if (type == 3007) {
			biz.openLoginActivity();
		}
		super.processDialogOK(type, tag);
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == ChargeOrder.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				S.bill.BillStatus = 1; // charged
				UI.showInfo(this, R.string.msg_charge_order_success, DLG_CHARGE_SUCCESS);

			} else if (result.code == BC.RESULT_OLD_BILL) {
				UI.showError(this, R.string.msg_old_bill);
			} else if (result.code == BC.RESULT_NO_CHECKIN_EXIST) {
				UI.showError(this, R.string.msg_no_checkin_exist);
			} else if (result.code == BC.RESULT_BILL_CHECKED_OUT) {
				UI.showError(this, R.string.msg_bill_checked_out);
			} else if (result.code == BC.RESULT_UNCHARGEABLE_BILL) {
				UI.showError(this, R.string.msg_unchargeable_bill);
			} else if (result.code == BC.RESULT_CONSUME_CARD_EXPIRED) {
				UI.showError(this, R.string.msg_consume_card_expired);
			} else if (result.code == BC.RESULT_CONSUME_CARD_LEVEL) {
				UI.showError(this, R.string.msg_consume_card_level);
			} else if (result.code == -3) {
				UI.showError(this, R.string.lbl_charge_failed);
			} else if (result.code == BC.LOGIN_OUT_TIME) {
				if (BC.type != 2) {// 吉云轩版本不提示登陆过期 2016.10.10
					UI.showError(this, getResources().getString(R.string.relogin), 3007, "");
				}
			} else {// 为了避免失败所以掉三次次接口
				isError++;
				if (isError == 3) {
					isError = 0;
					// 将失败代码打印方便检查
					UI.showError(this, R.string.lbl_charge_failed + result.obj.toString());
					return;
				}

				new MyAsyncTask(this, ChargeOrder.class).execute(S.bill.BillCode, S.bill.CardNo, S.token);
			}
		} else if (op == VerifyConsumeCard.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				S.bill.CardNo = cardNo;
				UI.showOKCancel(this, DLG_CHARGE, R.string.msg_confirm_charge, R.string.msg_confirm, null);
			} else if (result.code == BC.RESULT_INVALID_CONSUME_CARD) {
				UI.showError(this, String.format(getText(R.string.msg_invalid_card_no).toString(), cardNo));
			} else {
				UI.showError(this, String.format(getText(R.string.msg_invalid_cardno).toString(), cardNo));
			}
		}
	}

	@Override
	public void back() {

		super.back();
	}
}
