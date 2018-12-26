package com.clubank.device;

import org.kobjects.base64.Base64;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.op.GetDishImage;
import com.clubank.util.DB;
import com.clubank.util.BizDBHelper;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
/**
 * 更新菜品图片线程
 * @author jishu0416
 *
 */
public class UpdateDishImageTask extends ProgressTask {
	MyData data;
	BizDBHelper helper = new BizDBHelper(context, C.DB_NAME, null, C.DB_VERSION);
	DB db = null;

	public UpdateDishImageTask(Context context) {
		super(context);
		db = new DB(helper);
	}

	@Override
	protected void preWork() {
		data = db
				.getData("select code,imagedate from t_dish where extcode is not null order by code ");
	}

	@Override
	protected int size() {
		return data.size();
	}

	protected void afterWork() {
		super.afterWork();
	}

	@Override
	protected void doWork() {
		GetDishImage conn = new GetDishImage(context);
		for (int i = 0; i < data.size(); i++) {
			try {
				MyRow di = data.get(i);
				String code = (String) di.get("Code");
				String time = (String) di.get("ImageDate");
				if (time == null) {
					time = "1980-01-01 22:22:22";
				}

				Result result = conn.execute(code, time);
				MyRow row = (MyRow) result.obj;
				String smallPicture = (String) row.get("SmallPicture");
				String largePicture = (String) row.get("LargePicture");
				String imageDate = (String) row.get("ImageDate");

				if (smallPicture != null && smallPicture.length() > 0) {
					byte[] b1 = Base64.decode(smallPicture);
					byte[] b2 = Base64.decode(largePicture);
					String sql = "update t_dish set smallpicture=?,largepicture=? ,imageDate=? where code=?";
					db.exec(sql, new Object[] { b1, b2, imageDate, code });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			publishProgress(i);
		}
	}
}
