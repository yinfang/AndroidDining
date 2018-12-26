package com.clubank.domain;

import java.io.Serializable;

public class ImageDispProp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3501737399818197034L;
	public String smallPicCol;// 小图字段名
	public String largePicCol;// 大图字段名
	public String captionFormat;// 显示内容格式， 如 "%s (￥%n)",内容显示在浏览大图的底部。
	public String captionCols;// 显示名称字段多个用逗号分隔，数量与captionFormat里定义的变量要保持一致,例如
								// "Name,Price"
	/**
	 * 在平铺图下部直接显示名称,可通过下面参数调整文字显示效果 <br/>
	 * default_image_grid_item_text_height <br/>
	 * default_image_grid_item_text_bg <br/>
	 * default_image_grid_item_text_color <br/>
	 * default_image_grid_item_text_size
	 */
	public boolean showName;
	public String baseImageUrl;
	public boolean closeOnClickImage;

}
