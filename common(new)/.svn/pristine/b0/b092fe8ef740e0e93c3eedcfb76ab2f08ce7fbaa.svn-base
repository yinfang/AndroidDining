package com.clubank.device;

import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 友盟分享回调
 * Created by fengyq on 2016/3/3.
 */
public class UMPostListener implements UMShareListener {

    private BaseActivity a;

    public UMPostListener(BaseActivity a) {
        this.a = a;

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(a, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Toast.makeText(a, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Toast.makeText(a, "分享取消", Toast.LENGTH_SHORT).show();
    }
}
