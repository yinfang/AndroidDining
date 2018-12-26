package com.clubank.device;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;

import com.clubank.common.R;
import com.clubank.domain.C;
import com.clubank.domain.ImageDispProp;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;

/**
 * 平铺图Adapter，显示小图，一行3个，正方形
 */
public class ImageGridAdapter extends BaseAdapter {

	private ImageDispProp prop;
	private int height;
	private GridView.LayoutParams lp;

	public ImageGridAdapter(BaseActivity context, MyData data, Bundle b) {
		super(context, R.layout.image_grid_item, data);
		prop = (ImageDispProp) b.getSerializable("prop");
		lp = new GridView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	protected void display(int position, View v, MyRow row) {
		String smallPicture = row.getString(prop.smallPicCol);
		setImage(v, R.id.grid_item_image, prop.baseImageUrl + smallPicture);
		if (prop.showName) {
			/* 平铺图上显示名称 */
			show(v, R.id.grid_item_txt);
			/* 格式化内容 */
			String value = U.getFormatted(prop.captionFormat, prop.captionCols,
					row);
			setEText(v, R.id.grid_item_txt, value);
		} else {
			hide(v, R.id.grid_item_txt);
		}
		v.setLayoutParams(lp);
	}

	public void setItemHeight(int height) {
		if (height == this.height) {
			return;
		}
		this.height = height;
		lp = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		C.mImageFetcher.setImageSize(height);
		notifyDataSetChanged();
	}

}
