package com.clubank.device;

import android.os.Bundle;
import android.view.View;

import com.clubank.dining.R;
import com.clubank.domain.FeedbackInfo;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.util.UI;
/**
 * 反馈 （主页menu）
 * @author jishu0416
 *
 */
public class FeedbackActivity extends BasebizActivity {

	private static final int DLG_SUCCESS = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
	}
/**
 * 提交
 * @param view
 */
	public void submit(View view) {
		FeedbackInfo info = new FeedbackInfo();
		info.Name = getEText(R.id.Name);
		info.Content = getEText(R.id.Content);
		info.QQ = getEText(R.id.QQ);
		info.MobileNo = getEText(R.id.MobileNo);
		info.Email = getEText(R.id.Email);
		info.CreateBy = S.userName;

		if ("".equals(info.Content) || "".equals(info.Name)
				&& "".equals(info.QQ) && "".equals(info.MobileNo)
				&& "".equals(info.Email)) {
			UI.showError(this, R.string.msg_data_required);
			return;
		}
		UI.showInfo(this, R.string.msg_feedback_success, DLG_SUCCESS);
		return;
		// if (!info.MobileNo.equals("") && !UI.validateMobile(info.MobileNo)) {
		// UI.showError(this, R.string.msg_invalid_mobile);
		// return;
		// }
		// if (!info.Email.equals("") && !UI.validateEmail(info.Email)) {
		// UI.showError(this, R.string.msg_invalid_email);
		// return;
		// }
		// new MyAsyncTask(this, C.OP_FEEDBACK, true).execute(info);
		// send an email instead
	}

	@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		/*if (op == Feedback.c) {
			if (result.code == C.RESULT_SUCCESS) {
				UI.showInfo(this, R.string.msg_feedback_success, DLG_SUCCESS);
			} else {
				UI.showInfo(this, R.string.msg_operation_failed);
			}
		}*/
	}

	@Override
	public void processDialogOK(int type, Object tag) {
		super.processDialogOK(type, tag);
		if (type == DLG_SUCCESS) {
			finish();
		}
	}
}
