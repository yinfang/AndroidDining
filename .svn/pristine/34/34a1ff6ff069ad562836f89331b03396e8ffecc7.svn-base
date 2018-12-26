package com.clubank.device;

import android.content.ContentValues;
import android.content.Context;

import com.clubank.dining.R;
import com.clubank.domain.C;
import com.clubank.util.DB;
import com.clubank.util.BizDBHelper;
import com.clubank.util.MyData;

public class UpdateDishTask extends ProgressTask {
	MyData data;
	boolean downloadPicture;
	private Context context;
/**
 * 
 * @param context
 * @param downloadPicture  是否更新菜品图片
 */
	public UpdateDishTask(Context context, boolean downloadPicture) {
		super(context);
		this.context = context;
		this.downloadPicture = downloadPicture;
	}

	@Override
	protected void preWork() {
	}

	protected void setData(MyData data) {
		this.data = data;
	}

	@Override
	protected int size() {
		return data.size();
	}

	@Override
	protected void doWork() {
		for (int i = 0; i < data.size(); i++) {
			try {
				String code = (String) data.get(i).get("Code");
				 BizDBHelper helper = new BizDBHelper(context, C.DB_NAME, null, C.DB_VERSION);
				    DB db = new DB(helper);
				@SuppressWarnings("static-access")
				ContentValues cv = db.getContent(data.get(i),
						new String[] { "Code" });
				cv.put("Updated", "1");

				long n = db.update("t_dish", cv, "code=?",
						new String[] { code });
				if (n == 0) {
					cv.put("Code", code);
					db.insert( "t_dish", cv);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			publishProgress(i);
		}
	}

	protected void afterWork() {
		super.afterWork();
		if (downloadPicture) {
			UpdateDishImageTask task = new UpdateDishImageTask(context);
			task.setTitle(R.string.lbl_update_dish_image);
			task.execute();
		}
	}

}
