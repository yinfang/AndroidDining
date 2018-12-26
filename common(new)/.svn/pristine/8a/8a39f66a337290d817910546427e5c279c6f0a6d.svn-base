package com.clubank.util;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;

import com.clubank.common.R;
import com.clubank.device.BaseActivity;
import com.clubank.device.UMPostListener;
import com.clubank.domain.C;
import com.clubank.domain.ShareData;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.Map;

/**
 * 友盟分享
 * Created by fengyq on 2015/12/2.
 */
public class UmShare {
    private BaseActivity ba;
    private ShareData sd;
    public SHARE_MEDIA[] displaylist;
    public static ShareAction action;
    private UMImage umi;
    private UMPostListener umPostListener;

    public UmShare(BaseActivity activity) {
        this.ba = activity;
        this.action = new ShareAction(ba);
    }

    public void setShareData(ShareData shareData) {
        sd = shareData;
        configPlatforms();
    }

    /**
     * 默认提示toast
     *
     * @param isShowToast
     */
    public void isShowToast(Boolean isShowToast) {
        Config.IsToastTip = isShowToast;
    }

    /**
     * 设置自定义进度条
     */
    public void setDialog(Dialog dialog) {
        Config.dialog = dialog;
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加平台
        addPlatform();
        WaitingDialog dialog = new WaitingDialog(ba);
        Config.dialog = dialog;
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            UI.showToast(ba, "授权成功!");
            ba.saveSetting("oauth", true);
            shareSina(platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            UI.showToast(ba, "授权出错!");
            ba.saveSetting("oauth", false);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            UI.showToast(ba, "授权失败!");
            ba.saveSetting("oauth", false);
        }
    };

    /**
     * @return
     * @功能描述 : 添加平台分享
     */
    private void addPlatform() {
        //微信平台 appkey appsecret
        PlatformConfig.setWeixin(sd.getKey(C.SHARE_KEY_WEIXIN), sd.getKey(C.SHARE_KEY_WEIXIN_SECRET));
        //新浪微博
        PlatformConfig.setSinaWeibo(sd.getKey(C.SHARE_KEY_SINAWEIBO), sd.getKey(C.SHARE_KEY_SINAWEIBO_SECRET));
        //在微博开放平台设置的授权回调URL必须与代码中设置一致
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        // QQ和Qzone
        PlatformConfig.setQQZone(sd.getKey(C.SHARE_KEY_QQ), sd.getKey(C.SHARE_KEY_QQ_ID));
    }


    /**
     * 分享类型：Image：纯图片、Text：纯文字、Icon：图文分享
     */
    public void shareShow() {
        if (sd.bitmap != null) {
            umi = new UMImage(ba, sd.bitmap);
        } else {
            if (TextUtils.isEmpty(sd.imageUrl)) {
                umi = new UMImage(ba, BitmapFactory.decodeResource(ba.getResources(), R.drawable.emptylogo));
            } else {
                umi = new UMImage(ba, sd.imageUrl);
            }
        }
        umPostListener = new UMPostListener(ba);
        action.setDisplayList(displaylist).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.SINA) {
                    if (ba.settings.getBoolean("oauth", false)) {//新浪已授权
                        shareSina(share_media);
                    } else {
                        UMShareAPI mShareAPI = UMShareAPI.get(ba);
                        mShareAPI.doOauthVerify(ba, SHARE_MEDIA.SINA, umAuthListener);
                    }
                } else {
                    action.setPlatform(share_media).withText(sd.content).withTitle(sd.title).withTargetUrl(sd.url).withMedia(umi)
                            .setCallback(umPostListener)
                            .share();
                }
            }
        }).open();
    }

    public void shareSina(SHARE_MEDIA share_media) {
        String tt = sd.title, cont = Html.fromHtml(sd.content).toString();
        if (cont.length() > 50) {
            cont = cont.substring(0, 50) + "……";
        }
        if (tt.length() > 50) {
            tt = sd.title.substring(0, 50);
        }
        action.setPlatform(share_media)
                .withText(tt + "\n" + cont)
                .withTitle("")
                .withTargetUrl(sd.url)
                .withMedia(umi)
                .setCallback(umPostListener)
                .share();
    }
    /*图片(url)
    UMImage image = new UMImage(ShareActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");

    图片(本地资源引用)
    UMImage image = new UMImage(ShareActivity.this,
            BitmapFactory.decodeResource(getResources(), R.drawable.image));

    图片(本地绝对路径)
    UMImage image = new UMImage(ShareActivity.this,
            BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

    URL音频及图片
    UMusic music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
    music.setTitle("sdasdasd");
    music.setThumb(new UMImage(ShareActivity.this,"http://www.umeng.com/images/pic/social/chart_1.png"));

    url视频
    UMVideo video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");
*/

}
