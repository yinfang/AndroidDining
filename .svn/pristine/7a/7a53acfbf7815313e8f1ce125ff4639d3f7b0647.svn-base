package com.clubank.device;

import android.os.Bundle;
import android.widget.ListView;

import com.clubank.dining.R;
import com.clubank.util.MyData;

/**
 * 查看套餐明细
 */
public class PackageDetailActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.package_layout);
		String dishCode=getIntent().getStringExtra("Code");
		String sql = "select ParentCode,ItemCode,ItemName,IfCahnge,Pricing,Updated,ExtCode from t_PackageDish where ParentCode=? ";
		String[] a = new String[] { dishCode };
		MyData packageDeail = db.getData(sql,a);
		ListView	listView = (ListView) findViewById(R.id.package_dish);
		DishAdapter	da = new DishAdapter(this, packageDeail);
		da.type=1;
		listView.setAdapter(da);
	}


}
