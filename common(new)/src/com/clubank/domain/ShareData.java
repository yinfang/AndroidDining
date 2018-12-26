package com.clubank.domain;

import android.graphics.Bitmap;

import com.clubank.util.MyRow;

public class ShareData {

	public String title = "";// 标题
	public String content = "";// 内容
	public String url = "";// 链接地址
	public String imageUrl = "";// 图片地址
	public Boolean enableEdit;// 是否允许编辑
	public Bitmap bitmap;// 用于分享的本地图片地址
	public MyRow keys;// app id
	public String shareType = "";// 分享类型：Image：纯图片、Text：纯文字、Icon：图文分享

	/***
	 * 根据name获取对应的app id
	 * 
	 * @param name
	 * @return
	 */
	public String getKey(String name) {
		return keys.getString(name);
	}
}
