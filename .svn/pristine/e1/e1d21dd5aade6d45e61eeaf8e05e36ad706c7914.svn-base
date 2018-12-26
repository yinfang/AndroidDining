package com.clubank.device;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.GetCookingFlavor;
import com.clubank.op.GetCookingStyle;
import com.clubank.op.GetDiningArea;
import com.clubank.op.GetDish;
import com.clubank.op.GetDiningPackageItem;
import com.clubank.op.GetItemCategory;
import com.clubank.op.GetKitchenType;
import com.clubank.op.GetRecommendDish;
import com.clubank.op.GetTable;
import com.clubank.util.DB;
import com.clubank.util.JsonUtil;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 更新数据
 *
 * @author jishu0416
 */
public class UpdateDataActivity extends BaseActivity {

    private CheckBox c1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_data);
        findViewById(R.id.header_menu_scan).setVisibility(View.GONE);
        TextView curr_shop = (TextView) this.findViewById(R.id.curr_shop);
        curr_shop.setText(S.shopCode + " (" + S.shopName + ") ");
        // Button b = (Button) findViewById(R.id.btn_update_data);
        // b.setOnClickListener(new Button.OnClickListener() {
        c1 = (CheckBox) findViewById(R.id.checkBox1);
    }


    public void updateData(View view) {
        new MyAsyncTask(this, GetDiningArea.class).execute(S.shopCode);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        /*
		 * if (result.code == RT.SERVER_ERROR) { UI.showError(this, op.getName()
		 * + result.obj); }
		 */

        if (result.code != BC.RESULT_SUCCESS) {
            return;
        }
        try {

            if (op == GetDiningArea.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from t_DiningArea");
                for (int i = 0; i < data.size(); i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    db.insert("t_DiningArea", cv);
                }
                new MyAsyncTask(this, GetTable.class).execute("");
            } else if (op == GetTable.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from t_DiningTable");
                for (int i = 0; i < data.size(); i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    db.insert("t_DiningTable", cv);
                }
                new MyAsyncTask(this, GetItemCategory.class).execute(S.shopCode);
            } else if (op == GetItemCategory.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from t_ItemCategory");
                for (int i = 0; i < data.size(); i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    db.insert("t_ItemCategory", cv);
                }
                new MyAsyncTask(this, GetDish.class).execute(S.shopCode, "");
            } else if (op == GetDish.class) {
                MyData data = (MyData) result.obj;
                db.exec("update t_Dish set updated=0");
                UpdateDishTask task = new UpdateDishTask(this, c1.isChecked());
                task.setTitle(R.string.lbl_update_dish);
                task.setData(data);
                task.execute();
                db.exec("delete from t_Dish where updated=0");
                Criteria c = new Criteria();
                c.Distance = -1;
                c.OrderKey = "";
                if (BC.type == 2) {
                    c.PageIndex = 1;
                    c.PageSize = 2000;
                } else {
                    c.PageIndex = 1;
                    c.PageSize = 3000;
                }

                new MyAsyncTask(this, GetCookingStyle.class).execute(S.shopCode, c);
                // UI.showInfo(this, getText(R.string.msg_update_finished));
            } else if (op == GetCookingStyle.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from T_CookingStyle");
                int n = data.size();
				/*if (BC.type == 2) {
					List<ContentValues> concs = new ArrayList<>();
					for (int i = 0; i < n; i++) {
						ContentValues cv = DB.getContent(data.get(i));
						concs.add(cv);
					}
					db.diningInsert("T_CookingStyle", concs);
				} else {
					for (int i = 0; i < n; i++) {
						ContentValues cv = DB.getContent(data.get(i));
						db.insert("T_CookingStyle", cv);
					}
				}*/
                List<ContentValues> concs = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    concs.add(cv);
                }
                db.diningInsert("T_CookingStyle", concs);
                new MyAsyncTask(this, GetCookingFlavor.class).execute(S.shopCode);
            } else if (op == GetCookingFlavor.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from T_CookingFlavor");
                for (int i = 0; i < data.size(); i++) {
                    //根据后台返回数据是否包含以下的key来决定是否在设置界面显示口味分类的选项
                    if (data.get(i).containsKey("FlavorType")&& data.get(i).containsKey("TypeName")) {
                        saveSetting("showflavorClassify",true);
                    }else {
                        saveSetting("showflavorClassify",false);
                        saveSetting("flavorClassify",false);
                    }
                    ContentValues cv = DB.getContent(data.get(i));
//					ContentValues cv = getContent(data.get(i));
//					if (!cv.containsKey("Description")){
//						cv.put("Description",data.get(i).get("Name").toString());
//					}
//					if (!cv.containsKey("FlavorType")){
//						Random random = new Random();
//						int a = random.nextInt(4);
//						cv.put("FlavorType",a+"");
//					}
//					if (!cv.containsKey("TypeName")){
//						cv.put("TypeName",cv.getAsString("FlavorType"));
//					}

                    db.insert("T_CookingFlavor", cv);
                }
                Criteria c2 = new Criteria();
                c2.Distance = -1;
                c2.OrderKey = "";
                if (BC.type == 2) {
                    c2.PageIndex = 1;
                    c2.PageSize = 2000;
                } else {
                    c2.PageIndex = 1;
                    c2.PageSize = 2000;
                }

                if (settings.getBoolean("kitchentype", BC.showKitchenType)) {
                    new MyAsyncTask(this, GetKitchenType.class).execute(S.shopCode, c2);
                } else {
                    new MyAsyncTask(this, GetDiningPackageItem.class).execute(S.shopCode);
                }

            } else if (op == GetKitchenType.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from T_KitchenType");
                int n = data.size();
                List<ContentValues> concs = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    concs.add(cv);
                }
                db.diningInsert("T_KitchenType", concs);

                new MyAsyncTask(this, GetDiningPackageItem.class).execute(S.shopCode);
            } else if (op == GetDiningPackageItem.class) {
                MyData data = (MyData) result.obj;
                db.exec("delete from t_PackageDish");
                for (int i = 0; i < data.size(); i++) {
                    ContentValues cv = DB.getContent(data.get(i));
                    db.insert("t_PackageDish", cv);
                }
                //更新菜品推荐的数据
                String dateString =   C.df_yMd.format(new Date());
//                new MyAsyncTask(this, GetRecommendDish.class).run(S.shopCode, "",
//                        dateString);
            }else if (op == GetRecommendDish.class){
                MyData data = (MyData) result.obj;
                db.exec("update T_RecommendDish set updated=0");
                UpdateRecommendTask task = new UpdateRecommendTask(this, c1.isChecked());
                task.setTitle(R.string.lbl_update_dish);
                task.setData(data);
                task.execute();
                db.exec("delete from T_RecommendDish where updated=0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
