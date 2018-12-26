package com.clubank.device;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * bitmap加载监听
 * 比较适合cacheInMemory（false）和cacheOnDisc（false）
 * 但是为了提高用户体验均加入缓存所以要避免oom请调用cleanBitmapList方法；
 * @author fenyq
 *
 */
public class ReleaseBitmap implements ImageLoadingListener {

	private List<Bitmap> mBitmaps;
	public ReleaseBitmap(){
		mBitmaps=new ArrayList<Bitmap>();
	}
	
	/**
	 * 我们只需要在Activity或Fragment的onDestory（）方法中再调用cleanBitmapList（）方法就防止内存持续上升的问题。
	 */
	public void cleanBitmapList(){
		if(mBitmaps.size()>0){
			for (int i = 0; i < mBitmaps.size(); i++) {
				Bitmap b=mBitmaps.get(i);
				if(b!=null&&!b.isRecycled()){
					b.recycle();
					
				}
			}
			
		}
	}
	
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		mBitmaps.add(loadedImage);
		
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}

	

}
