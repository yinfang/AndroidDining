package com.clubank.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.route.RoutePlanSearch;

import com.bumptech.glide.Glide;
import com.clubank.common.R;
import com.clubank.domain.C;
import com.clubank.domain.ContactInfo;
import com.clubank.domain.Criteria;
import com.clubank.domain.ImageDispProp;
import com.clubank.domain.ListObject;
import com.clubank.domain.MapPoint;
import com.clubank.domain.Point;
import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.map.MyOnGetGeoCoderResultListener;
import com.clubank.map.MyOnMarkerClickListener;
import com.clubank.util.ACache;
import com.clubank.util.CustomDialog;
import com.clubank.util.CustomDialog.Initializer;
import com.clubank.util.CustomDialog.OKProcessor;
import com.clubank.util.DB;
import com.clubank.util.DownloadFile;
import com.clubank.util.GlideCacheUtil;
import com.clubank.util.GlideUtil;
import com.clubank.util.ImageUtil;
import com.clubank.util.ListDialog;
import com.clubank.util.MListDialog;
import com.clubank.util.MListDialog.OnNeutralListener;
import com.clubank.util.MapUtil;
import com.clubank.util.MyDBHelper;
import com.clubank.util.MyData;
import com.clubank.util.MyDateDialog;
import com.clubank.util.MyRow;
import com.clubank.util.MyTimeDialog;
import com.clubank.util.U;
import com.clubank.util.UI;
import com.clubank.util.UmShare;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

//import com.baidu.mobstat.StatService;

@SuppressLint({"InflateParams", "Registered", "NewApi"})
public class BaseActivity extends FragmentActivity {
    public SharedPreferences settings;
    public MyApplication app;
    protected Context sContext;
    private static boolean initialized;

    /**
     * 检查登陆的类别以便回调时区分
     */
    protected DB db;
    protected DisplayMetrics dm = new DisplayMetrics();
    /**
     * 屏幕较短边的像素
     */
    protected int smallerScreenSize;
    protected String[] menus;
    protected int[] menuImages;
    protected IBiz biz;

    /**
     * 触发处理图片的控件
     */
    protected View getPictureView;
    /**
     * 要填充的图片控件
     */
    public Display screen;
    protected ImageView iv;
    private OnClickListener clickListener;
    private MapView mMapView;// 百度地图对象
    private RoutePlanSearch mSearch;// 导航路径
    private boolean isShowPictrue;// 是否展示裁剪后的照片
    private Calendar ca = Calendar.getInstance();
    private GlideUtil glideUtil = GlideUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContext = this;
        if (C.PageChangeWay == 0) {//淡入淡出
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        } else if (C.PageChangeWay == 1) {//由右向左滑入
            overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
        }
//        if (U.isDebug(this)){
            Log.d("------", "onCreate: "+this.getClass().getSimpleName());
//        }

        try {
            Class<?> clazz = Class.forName("com.clubank.device.MyBiz");
            Constructor<?> c = clazz.getConstructor(BaseActivity.class);
            biz = (IBiz) c.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyDBHelper helper = biz.initDBHelper(this);
        if (null != helper) {
            db = new DB(helper);
        }
        app = (MyApplication) getApplication();
        app.addActivity(this);
        settings = getSharedPreferences(C.APP_ID, MODE_PRIVATE);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screen = wm.getDefaultDisplay();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        smallerScreenSize = dm.widthPixels;
        if (dm.heightPixels < dm.widthPixels) {
            smallerScreenSize = dm.heightPixels;
        }
        clickListener = new MyOnClickListener();

        setTheme(R.style.MyHoloTheme);
        if (!initialized) {
            //    U.initImageCache(this); 废弃使用Glide图片框架
            U.initLocSDK(this);
            U.initPush(this);
            U.initUmeng(this);
            initialized = true;
        }
//        Frontia.init(getApplicationContext(),
//                PushUtils.getMetaValue(this, "api_key"));
//     if(C.TYPESH==2){
//        	
//     		callAsynchronousTask();
//        }
    }

    protected void onStart() {
        super.onStart();
        if (mMapView == null) {
            /*
             * 如果是地图类界面，确保layout中的地图控件id为bmapView 最好在项目的layout文件中 include common
			 * 中的 my_map
			 */
            int mapId = UI.getId(this, "bmapView", "id");
            if (mapId > 0) {
                mMapView = (MapView) findViewById(mapId);
            }
        }
    }

    private void setOnClickListener(String res) {
        int resId = UI.getId(this, res, "id");
        if (resId > 0) {
            View v = findViewById(resId);
            if (v != null) {
                v.setOnClickListener(clickListener);
            }
        }
    }

    public void openIntent(Class<?> clazz, CharSequence title) {
        openIntent(clazz, title, null);
    }

    public void openIntent(Class<?> clazz, CharSequence title, Bundle extras) {
        openIntent(clazz, title, extras, 0);
    }

    public void openIntent(Class<?> clazz, CharSequence title, Bundle extras,
                           int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("title", title.toString().replaceAll("\n", " "));
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }

    public void openIntent(Class<?> clazz, int resId) {
        openIntent(clazz, getText(resId), null);
    }

    public void openIntent(Class<?> clazz, int resId, int requestCode) {
        openIntent(clazz, getText(resId), null, requestCode);
    }

    public void openIntent(Class<?> clazz, int resId, Bundle extras,
                           int requestCode) {
        openIntent(clazz, getText(resId), extras, requestCode);
    }

    /**
     * ;[pp 打开新Activity
     *
     * @param clazz Activity
     */
    public void openIntent(Class<?> clazz, int resId, Bundle b) {
        openIntent(clazz, getText(resId), b);
    }

//    public void callAsynchronousTask() {
//		final Handler handler = new Handler();
//		Timer timer = new Timer();
//		
//		TimerTask doAsynchronousTask = new TimerTask() {
//			@Override
//			public void run() {
//				handler.post(new Runnable() {
//					public void run() {
//						try {
//							biz.UploadFile(BaseActivity.this, "");
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//						}
//					}
//				});
//			}
//		};
//		timer.schedule(doAsynchronousTask, 0, 50000); // execute in every 50000
//	}

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面
        // (仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        if (menus != null && findViewById(R.id.menu) != null) {
            show(R.id.menu);
        }

        // use string instead of R.id.menu to avoid NoSuchField Exception,etc.
        // in case a layout not include common header
        setOnClickListener("menu");
        setOnClickListener("ic_home");
        setOnClickListener("header_back");
        Bundle bundle = getIntent().getExtras();
        String title = null;
        if (bundle != null) {
            title = bundle.getString("title");
        }
        /*int id = UI.getId(this, "debug", "id");
        if (id > 0) {
            View v = findViewById(id);
            if (v != null) {
                if (settings.getInt("serverType", C.SERVER_INTERNET) == C.SERVER_INTRANET) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
            }
        }*/

        if (title != null) {
            setHeaderTitle(title);
        }
        restoreVars();
//        StatService.onResume(this);
        if (mMapView != null) {
            mMapView.onResume();
        }

    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
//        StatService.onPause(this);
        JPushInterface.onPause(this);
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        app.removeActivity(this);
        Glide.get(this).clearMemory();//清理内存缓存  可以在UI主线程中进行
        // Glide.get(this).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
    }

    protected void setHeaderTitle(CharSequence title) {
        View tv = findViewById(R.id.header_title);
        if (tv != null && title != null && tv instanceof TextView) {
            ((TextView) tv).setText(title);
        }
    }

    protected void setHeaderTitle(int resId) {
        setHeaderTitle(getText(resId));
    }

    public String getEText(int resId) {
        return UI.getEText(this, resId);
    }

    public void setEColor(int resId, int color) {
        TextView tv = (TextView) findViewById(resId);
        if (tv != null)
            tv.setTextColor(getResources().getColor(color));
    }

    public void setVColor(int resId, int color) {
        View v = (View) findViewById(resId);
        if (v != null)
            v.setBackgroundColor(getResources().getColor(color));
    }

    public void setEColor(View view, int resId, int color) {
        TextView tv = (TextView) view.findViewById(resId);
        if (tv != null)
            tv.setTextColor(getResources().getColor(color));
    }

    public void setEText(int resId, CharSequence value) {
        UI.setEText(this, resId, value);
    }

    public void setEText(int resId, int resValue) {
        UI.setEText(this, resId, resValue);
    }


    /**
     * 加载图片的字节数组
     *
     * @param model string可以为一个文件路径、uri或者url
     *              uri类型
     *              文件
     *              资源Id,R.drawable.xxx或者R.mipmap.xxx
     *              byte[]类型
     */
    public void setImage(int ImageViewId, Object model) {
        glideUtil.setImage(this, ImageViewId, model);
    }


    public void setImage(int ImageViewId, Object model, boolean isCircle) {
        glideUtil.setImage(this, ImageViewId, model, isCircle);
    }


    public void setImage(int ImageViewId, Object model, int loadingImage) {
        glideUtil.setImage(this, ImageViewId, model, loadingImage);
    }

    /**
     * ****************************以下兼容旧版项目，废弃方法************************************************
     */
    public void setImage(ImageView imageView, Object model, boolean isCircle) {
        glideUtil.setImage(this, imageView, model, isCircle);
    }

    public void setImage(ImageView imageView, Object model) {
        glideUtil.setImage(this, imageView, model);
    }

    public void setImage(ImageView imageView, Object model, int loadingImage) {
        glideUtil.setImage(this, imageView, model, loadingImage);
    }
    /********************************以上兼容旧版项目，废弃方法************************************************/


    /**
     * 调出打电话界面
     *
     * @param phoneNo 要打的电话
     */
    public void call(String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNo));
        startActivity(intent);
    }

    /**
     * 返回上一页前检查是否确实要返回
     *
     * @return
     */
    protected boolean validate() {
        return true;
    }

    public void logout() {

        exit(false);
    }

    public void saveSetting(String name, boolean value) {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public void saveSetting(String name, String value) {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.commit();
    }

    public void saveSetting(String name, int value) {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public void removeSetting(String name) {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.remove(name);
        editor.commit();
    }

    /**
     * 多选对话框点击了一个通用按钮
     *
     * @param view
     * @param selected
     */

    protected void neutralSelected(View view, boolean[] selected) {
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param captions 列表文本
     */
    public void showListDialog(View view, String[] captions) {
        showListDialog(view, 0, captions, null);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param resTitle 列表的显示标题
     * @param resArray 资源内定义的字符串数组
     */
    public void showListDialog(View view, int resTitle, int resArray) {
        showListDialog(view, resTitle, getResources().getStringArray(resArray),
                null);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param captions 列表文本
     * @param images   列表图标, 数量必须与captions保持一致
     */
    public void showListDialog(View view, String[] captions, int[] images) {
        showListDialog(view, 0, captions, images);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view       触发控件
     * @param resTitleId 显示标题栏文字，0 不显示标题栏
     * @param captions   列表文本
     * @param images     列表图标, 数量必须与captions保持一致
     */
    protected void showListDialog(View view, int resTitleId, String[] captions,
                                  int[] images) {
        ListDialog dialog = new ListDialog(this);
        dialog.setOnSelectedListener(new ListDialog.OnSelectedListener() {
            public void onSelected(View view, int index) {
                listSelected(view, index);
            }
        });
        dialog.show(view, resTitleId, captions, images);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     */
    protected void showListDialog(View view, int resTitleId, String[] captions) {
        showListDialog(view, resTitleId, captions, null);
    }

    /**
     * 弹出多选列表
     *
     * @param view     触发控件
     * @param captions 列表文本
     * @param selected 初始值，长度必须与captions相同
     */
    public void showMListDialog(final View view, String[] captions,
                                boolean[] selected) {
        showMListDialog(view, 0, captions, selected);
    }

    /**
     * 弹出多选列表
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     * @param selected   初始值，长度必须与captions相同
     */
    protected void showMListDialog(final View view, int resTitleId,
                                   String[] captions, boolean[] selected) {
        showMListDialog(view, resTitleId, captions, selected, 0);
    }

    /**
     * 弹出多选列表
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     * @param selected   初始值，长度必须与captions相同
     * @param thirdBtn   自定义按钮文字资源， 0 - 无自定义按钮
     */
    protected void showMListDialog(final View view, int resTitleId,
                                   String[] captions, boolean[] selected, int thirdBtn) {
        final MListDialog dialog = new MListDialog(this);
        dialog.setOnSelectedListener(new MListDialog.OnPositiveListener() {
            public void onSelected(View view, boolean[] selected) {
                mlistSelected(view, selected);
            }
        });
        if (thirdBtn > 0) {
            dialog.setOnNeutralListener(new OnNeutralListener() {
                public void onSelected(View view, boolean[] selected) {
                    neutralSelected(view, selected);
                }
            });
        }
        dialog.show(view, resTitleId, captions, selected, thirdBtn);
    }

    /**
     * 弹出Popup菜单，位置就在控件附近
     *
     * @param view  触发控件
     * @param pos   第几个菜单，1 开始
     * @param total 屏幕分为几列菜单，决定菜单宽度，3表示菜单宽度为屏幕的1/3
     * @param menus 菜单文本内容
     */
    public void showMenu(final View view, int pos, int total, String[] menus) {
        showMenu(view, pos, total, menus, new int[menus.length]);
    }

    /**
     * 弹出Popup菜单，位置就在控件附近
     *
     * @param view   触发控件
     * @param pos    第几个菜单，1 开始
     * @param total  屏幕分为几列菜单，决定菜单宽度，3表示菜单宽度为屏幕的1/3
     * @param menus  菜单文本内容
     * @param images 菜单前面显示的图标资源
     */
    @SuppressWarnings("deprecation")
    public void showMenu(final View view, int pos, int total, String[] menus,
                         int[] images) {
        if (menus == null || menus.length == 0) {
            /* 文本内容为空，忽略 */
            return;
        }
        View v = getLayoutInflater().inflate(R.layout.popwindow_light, null);
        ListView listView1 = (ListView) v.findViewById(R.id.PopWindow_lv);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();// deprecated in API 13, ignore it.
        int height = display.getHeight() / 2;

        final PopupWindow popupWindow = new PopupWindow(v, width / total,
                height);

		/* 加上这行可以确保点击外面后让弹出窗口消失 */
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);

        MyData data = new MyData();
        for (int i = 0; i < menus.length; i++) {
            MyRow row = new MyRow();
            if (images != null) {
                row.put("imageResId", images[i]);
            }
            row.put("name", menus[i]);
            data.add(row);
        }
        MenuAdapter adapter = new MenuAdapter(this, data);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                menuSelected(view, arg2);
                popupWindow.dismiss();
            }
        });

        // (View) view.getParent().getParent()得到父容器，然后将popupWindow放在这个位置。
        popupWindow.showAsDropDown((View) view.getParent(), (width / total)
                * (pos - 1), 0);
    }

    @SuppressWarnings("deprecation")
    public void showTipsMenu(final View view, double factor, String[] menus,
                             int[] images) {
        if (menus == null || menus.length == 0) {
            /* 文本内容为空，忽略 */
            return;
        }
        View v = getLayoutInflater().inflate(R.layout.popwindow_light, null);
        ListView listView1 = (ListView) v.findViewById(R.id.PopWindow_lv);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();// deprecated in API 13, ignore it.
        int height = display.getHeight() / 2;

        final PopupWindow popupWindow = new PopupWindow(v,
                (int) (width * factor), height);

		/* 加上这行可以确保点击外面后让弹出窗口消失 */
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);

        MyData data = new MyData();
        for (int i = 0; i < menus.length; i++) {
            MyRow row = new MyRow();
            if (images != null) {
                row.put("imageResId", images[i]);
            }
            row.put("name", menus[i]);
            data.add(row);
        }
        MenuAdapter adapter = new MenuAdapter(this, data);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                menuSelected(view, arg2);
                popupWindow.dismiss();
            }
        });

        // (View) view.getParent().getParent()得到父容器，然后将popupWindow放在这个位置。
        popupWindow.showAsDropDown((View) view.getParent(), 0, 0);
    }

    /**
     * 弹出日期选择框,子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    protected void showDateDialog(final View view, int year, int month, int day, boolean showday) {
        MyDateDialog d = new MyDateDialog(this);
        d.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            boolean fired;

            public void onDateSet(DatePicker v, int year, int month, int day) {
                if (!fired) {
                    if (view instanceof TextView) {
                        String date = U.getDateString(year, month, day);
                        ((TextView) view).setText(date);
                    }
                    dateSet(view, year, month, day);// 回调子类方法
                }
                fired = true;
            }
        });
        d.show(year, month, day, showday);
    }

    /**
     * 填出日期选择框，子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     * @param date 显示已有日期，格式 yyyy-MM-dd
     */
    public void showDateDialog(View view, String date) {
        String[] sdate = date.toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, year, month, day, true);
    }


    /**
     * 只展示年月的日期控件
     * <p/>
     * 填出日期选择框，子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     * @param date 显示已有日期，格式 yyyy-MM-dd
     */
    public void showYearsDialog(View view, String date) {
        String[] sdate = date.split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = ca.get(Calendar.DAY_OF_MONTH);
        }
        showDateDialog(view, year, month, day, false);
    }

    /**
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showDateDialog(View view) {
        int day;
        String[] sdate = ((TextView) view).getText().toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, year, month, day, true);
    }

    /**
     * 显示年或月控件
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showYearOrMonthDialog(View view, String date) {

        if (date.length() > 2) {//显示年
            int month = ca.get(Calendar.MONTH);
            int day = ca.get(Calendar.DAY_OF_MONTH);

            int year = Integer.parseInt(date) + 1;
            showDateDialog(view, year, month, day, true, false, false);
        } else {//显示月
            int year = ca.get(Calendar.YEAR);
            int day = ca.get(Calendar.DAY_OF_MONTH);

            int month = Integer.parseInt(date) - 1;
            showDateDialog(view, year, month, day, false, true, false);
        }

    }

    /**
     * 弹出年或月选择框,子类需要实现回调方法 dateSet
     *
     * @param view  日期控件，必须是TextView /Button/EditText 等，格式2014-12
     * @param year
     * @param month
     * @param day
     */
    protected void showDateDialog(final View view, int year, int month, int day, boolean showYear, boolean showMonth,
                                  boolean showday) {
        MyDateDialog d = new MyDateDialog(this);
        d.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            boolean fired;

            public void onDateSet(DatePicker v, int year, int month, int day) {
                if (!fired) {
                    if (view instanceof TextView) {
                        String date = U.getDateString(year, month, day);
                        ((TextView) view).setText(date);
                    }
                    dateSet(view, year, month, day);// 回调子类方法
                }
                fired = true;
            }
        });
        d.show(year, month, day, showYear, showMonth, showday);
    }

    /**
     * 显示年月控件
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showYearsDialog(View view) {
        int day;
        String[] sdate = ((TextView) view).getText().toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]);
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, year, month, day, false);
    }


    /**
     * 弹出世间选择框，子类需要实现回调方法 timeSet
     *
     * @param view   时间控件，必须是TextView /Button/EditText 等
     * @param hour
     * @param minute
     */
    protected void showTimeDialog(final View view, int hour, int minute) {
        MyTimeDialog d = new MyTimeDialog(this);
        d.setOnTimeSetListener(new OnTimeSetListener() {
            public void onTimeSet(TimePicker v, int hour, int minute) {
                if (view instanceof TextView) {
                    String time = U.getTimeString(hour, minute);
                    ((TextView) view).setText(time);
                }
                timeSet(view, hour, minute);// 回调子类方法
            }
        });
        d.show(hour, minute);
    }

    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view 时间控件，必须是TextView /Button/EditText 等
     * @param time 格式 19:30
     */
    public void showTimeDialog(View view, String time) {
        String[] sdate = time.split(":");
        int hour = Integer.parseInt(sdate[0]);
        int minute = Integer.parseInt(sdate[1]);
        showTimeDialog(view, hour, minute);
    }

    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view 时间控件，必须是TextView /Button/EditText 等
     */
    public void showTimeDialog(View view) {
        String[] sdate = ((TextView) view).getText().toString().split(":");
        int hour = Integer.parseInt(sdate[0]);
        int minute = Integer.parseInt(sdate[1]);
        showTimeDialog(view, hour, minute);
    }

    /**
     * 清除参数设置，
     */
    protected void clearSetting() {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.remove("SessionID");
        // editor.remove("rememberPassword");// 删除记住密码
        editor.commit();
    }

    /**
     * 打开地图，显示地标名称，默认缩放比例为14，使用默认地标图片文件R.drawable.icon_marka
     *
     * @param point 中心点坐标，如果point.name 为空，则显示默认地理名称
     */
    public void openMap(MapPoint point) {
        if (point.name != null) {
            openMap(point, 14);
            return;
        }
        /* 如果地标名称为空，检索默认地理名称 */
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener(
                this));
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(
                point.latitude, point.longitude)));
    }

    /**
     * 打开地图，并显示驾车导航
     *
     * @param point     指定点
     * @param zoomLevel 缩放比例 最好在12~16之间
     */
    public void openMap(final MapPoint point, int zoomLevel) {
        final LatLng pt2 = new LatLng(point.latitude, point.longitude);
        final BaiduMap baiduMap = mMapView.getMap();
        MapUtil.zoomTo(baiduMap, zoomLevel);// 显示合适比例尺
        MapUtil.center(baiduMap, pt2);// 指定中心点
        MapUtil.drawPoint(baiduMap, point);// 显示地标
        if (point.name != null) {// 显示地点名称
            MapUtil.showInfoWindow(this, baiduMap, new LatLng(point.latitude,
                    point.longitude), point.name, 0);
        }
        if (C.location == null) {
            UI.showToast(this, R.string.curr_loc);
            return;
        }
        setNaviListener(point);
        mSearch = RoutePlanSearch.newInstance();
        MapUtil.initSearch(this, baiduMap, mSearch);
        MyOnMarkerClickListener l = new MyOnMarkerClickListener(this, baiduMap);
        baiduMap.setOnMarkerClickListener(l);
    }

    private void setNaviListener(final MapPoint point) {
        final View info = findViewById(R.id.info);
        findViewById(R.id.search).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (view.getId() == R.id.search) {
                    if (info.getVisibility() == View.GONE) {
                        showNaviPath(point); // 第一次进入，显示导航路径
                    } else if (info.getVisibility() == View.VISIBLE) {
                        // 第二次进入， 导航
                        navi(point);
                    }
                }
            }
        });
    }

    /**
     * 显示导航路径。
     *
     * @param point 目标地点
     */
    protected void showNaviPath(MapPoint point) {

        // show(R.id.row_route);
        final BaiduMap baiduMap = mMapView.getMap();
        // baiduMap.hideInfoWindow();
        // 第一次进入搜索显示行车路径
        if (C.location == null) {
            UI.showToast(this, R.string.curr_loc);
            return;
        }
        setNaviListener(point);
        final LatLng pt1 = new LatLng(C.location.getLatitude(),
                C.location.getLongitude());
        final LatLng pt2 = new LatLng(point.latitude, point.longitude);
        if (mSearch == null) {
            mSearch = RoutePlanSearch.newInstance();
            MapUtil.initSearch(this, baiduMap, mSearch);
        }
        MapUtil.drivingSearch(mSearch, pt1, pt2);
    }

    /**
     * 导航。
     *
     * @param point 指定地点，注意 point.name 不能为空
     */

    protected void navi(Point point) {
        if (C.location == null) {
            UI.showToast(this, R.string.curr_loc);
            return;
        }
        NaviParaOption para = new NaviParaOption();
        para.startPoint(new LatLng(C.location.getLatitude(),
                C.location.getLongitude()));
        para.startName(getString(R.string.navi_start));
        para.endPoint(new LatLng(point.latitude, point.longitude));

        para.endName(point.name);
        MapUtil.navi(para, BaseActivity.this);
    }

    /**
     * 打开多点地图
     *
     * @param points    多个点
     * @param zoomLevel 地图缩放比例，一般在12～16之间
     */
    protected void openMap(final MapPoint[] points, int zoomLevel) {

        final BaiduMap baiduMap = mMapView.getMap();
        baiduMap.clear();
        /* 在地图上显示点 */
        Marker[] markers = new Marker[points.length];
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < points.length; i++) {
            Bundle b1 = new Bundle();
            b1.putInt("index", i);
            MapPoint pt = points[i];
            LatLng ll = new LatLng(pt.latitude, pt.longitude);
            markers[i] = MapUtil.drawPoint(baiduMap, pt);
            markers[i].setExtraInfo(b1);
            builder.include(ll);
        }
        MapUtil.center(baiduMap, builder);
        MapUtil.zoomTo(baiduMap, zoomLevel);
        MyOnMarkerClickListener l = new MyOnMarkerClickListener(this, baiduMap);
        baiduMap.setOnMarkerClickListener(l);
    }

    /**
     * 获取地图缩放等级
     *
     * @return 如果地图没有缩放等级 默认使用C.MAP_ZOOM_LEVEL
     */
    protected int getMapZoom() {
        int zoom;
        try {
            BaiduMap baiduMap = mMapView.getMap();
            zoom = (int) baiduMap.getMapStatus().zoom;
        } catch (Exception e) {
            zoom = C.MAP_ZOOM_LEVEL;
        }
        return zoom;
    }

    /**
     * 通用弹出窗口点OK按钮后的回调方法
     *
     * @param type 标识，如果有多个地方触发，用于区分触发的是哪次。
     * @param tag  弹出窗口附加的自定义对象
     */
    public void processDialogOK(int type, Object tag) {
        if (type == C.DLG_ILLEGAL_ACCESS) {
            if (biz != null) {
                biz.openLoginActivity();
            }
        } else if (type == C.DLG_DOWNLOAD_NEW_VERSION) {
            new DownloadFile(this).execute((String) tag);
        }
    }

    /**
     * 通用弹出窗口点取消按钮后的回调方法
     *
     * @param type 标识，如果有多个地方触发，用于区分触发的是哪次。
     * @param tag  弹出窗口附加的自定义对象
     */
    public void processDialogCancel(int type, Object tag) {
    }

    /**
     * 刷新数据的方法，子类可覆盖
     */
    public void refreshData() {
    }

    /**
     * 日期设置回调方法，子类中覆盖此方法
     *
     * @param view  控件标识，如果有多个日期时用这个标识区分。
     * @param year
     * @param month
     * @param day
     */
    public void dateSet(View view, int year, int month, int day) {
    }

    /**
     * 弹出菜单选择事件，子类中覆盖此方法
     *
     * @param view  弹出菜单窗口的控件
     * @param index 选择的菜单项位置
     */
    public void menuSelected(View view, int index) {
    }

    /**
     * 事件设置回调方法，子类中覆盖此方法
     *
     * @param hour
     * @param minute
     */
    public void timeSet(View view, int hour, int minute) {
    }

    /**
     * 列表选择的回调方法，子类覆盖此方法。
     *
     * @param view
     * @param index
     */
    public void listSelected(View view, int index) {
        if (view.equals(getPictureView)) {
            /* 设置图片时，默认的处理代码，设置图片默认也是一个列表菜单 */
            if (index == 0) {// 拍照
                String fname = Environment.getExternalStorageDirectory() + "/"
                        + +System.currentTimeMillis() + ".jpg";
                saveSetting("fname", fname);
                UI.takePicture(this, fname, C.REQUEST_FROM_CAMERA);
            } else if (index == 1) {// 选择图片
                UI.choosePicture(this, C.REQUEST_FROM_PICTURE);
            }
        }
    }

    /**
     * 多选列表回调方法，子类覆盖此方法
     *
     * @param view     触发控件
     * @param selected 选择后的值，长度与文本数组长度相同
     */
    public void mlistSelected(View view, boolean[] selected) {
    }

    /**
     * 检查登陆回调方法，对于需要检查登陆的界面，必须实现此方法
     *
     * @param type
     */
    public void logined(int type) {
    }

    /**
     * 系统级ActivityResult,如登陆，打开联系人，设置图片等，开发人员不用理会
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fname = settings.getString("fname", "");
        if (requestCode == C.REQUEST_LOGIN && resultCode == RESULT_OK) {
            logined(biz.getLoginType());
        } else if (requestCode == C.REQUEST_SELECT_CONTACTS
                && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Cursor c = managedQuery(uri, null, null, null, null);
            c.moveToFirst();
            ContactInfo contact = getContactPhone(c);
            if (contact.name != null) {
                selectedContact(contact);
            }
        } else if (requestCode == C.REQUEST_FROM_CAMERA) {// 照相后剪裁
            File file = new File(fname);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                UI.cropPicture(this, C.REQUEST_HANDLE_PICTURE, uri);
            }
        } else if (requestCode == C.REQUEST_FROM_PICTURE) {// 选取图片后剪裁
            if (data != null) {
                Uri uri = data.getData();
                UI.cropPicture(this, C.REQUEST_HANDLE_PICTURE, uri);
            }
        } else if (requestCode == C.REQUEST_HANDLE_PICTURE) {// 处理剪裁结果
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            Bitmap large = extras.getParcelable("data");
            String tmpfile = ImageUtil.saveTempBitmap(large, "tmp", ".jpg");
            Bitmap small = Bitmap.createScaledBitmap(large, C.small, C.small,
                    true);

            if (isShowPictrue)
                iv.setImageBitmap(small);


            if (tmpfile == null) {
                UI.showInfo(this, R.string.save_picture_failed);
                return;
            }
            /*
             * new MyAsyncTask(this, UploadFile.class).execute(tmpfile,
			 * C.baseUrl + "Upload.aspx");
			 */
            /*
             * new MyAsyncTask(this, UploadFile.class).execute(tmpfile,
			 * C.baseUrl + C.uploadMethod);
			 */

            biz.UploadFile(this, tmpfile);

            File f = new File(fname);
            if (f.exists()) {
                f.delete();
            }

        }
        if (null != UmShare.action) {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 设置服务地址后的基础处理
     *
     * @param type
     */
    protected void setServerType(int type) {
        saveSetting("baseImageUrl", C.baseImageUrl);
        saveSetting("baseUrl", C.baseUrl);
        saveSetting("serverType", type);
        saveSetting("wsUrl", C.wsUrl);
    }

    /*
     * 此方法调用的前提：程序被系统销毁
     * (non-Javadoc)
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreVars();
    }

    /**
     * 恢复全局变量。因为系统会在休眠后不定时将静态变量清空， <br/>
     * 因此静态变量必须在赋值后存入硬盘。在使用时恢复。子类覆盖此方法
     */
    private void restoreVars() {
        if (biz != null) {
            biz.restoreVars();
        }
    }

    /**
     * 初始化列表。调用此方法后，系统会按需自动显示“更多”按钮。 子类需要覆盖 refreshData()
     *
     * @param listView
     * @param adapter
     * @param criteria
     */

    private MyRow lists = new MyRow();

    /**
     * 初始化 ListView, 以便统一处理更多翻页和无数据文字显示情况
     *
     * @param listView 指定的ListView
     * @param adapter  数据适配器
     * @param criteria 查询条件，要翻页时必须有此参数
     * @param op       对应ListView查询数据的远程方法
     */
    protected void initList(ListView listView, ArrayAdapter<?> adapter,
                            Criteria criteria, Class<?> op) {
        initList(listView, adapter, criteria, op, 0);
    }

    /**
     * 初始化 ListView, 以便统一处理更多翻页和无数据文字显示情况
     *
     * @param listView 指定的ListView
     * @param adapter  数据适配器
     * @param op       对应ListView查询数据的远程方法
     */

    protected void initList(ListView listView, ArrayAdapter<?> adapter,
                            Class<?> op) {
        initList(listView, adapter, null, op, 0);
    }

    /**
     * 初始化 ListView, 以便统一处理更多翻页和无数据文字显示情况
     *
     * @param listView  指定的ListView
     * @param adapter   数据适配器
     * @param op        对应ListView查询数据的远程方法
     * @param noDataTip 无数据时显示的自定义文字，资源ID
     */
    protected void initList(ListView listView, ArrayAdapter<?> adapter,
                            Class<?> op, int noDataTip) {
        initList(listView, adapter, null, op, noDataTip);
    }

    /**
     * 初始化 ListView, 以便统一处理更多翻页和无数据文字显示情况
     *
     * @param listView  指定的ListView
     * @param adapter   数据适配器
     * @param criteria  查询条件，要翻页时必须有此参数
     * @param op        对应ListView查询数据的远程方法
     * @param noDataTip 无数据时显示的自定义文字，资源ID
     */
    protected void initList(ListView listView, ArrayAdapter<?> adapter,
                            Criteria criteria, Class<?> op, int noDataTip) {
        View footer = getLayoutInflater().inflate(R.layout.list_footer, null);
        footer.setOnClickListener(clickListener);
        ListObject lo = new ListObject();
        lo.adapter = adapter;
        lo.listView = listView;
        lo.footer = footer;
        lo.criteria = criteria;
        lo.noDataTip = noDataTip;
        lists.put(op.getName(), lo);
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footer);// 防止重复添加footer
        } else {
            hide(R.id.no_data);
            hide(R.id.more_data);
        }

        listView.setAdapter(adapter);
        listView.setOnScrollListener(new MyOnScrollListener(this, lo));
    }

    protected void addEmptyView(ListView listView) {
        LayoutInflater inflater = getLayoutInflater();
        View emptyView = inflater.inflate(R.layout.list_empty_view, null);
       /* emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        ((ViewGroup)listView.getParent()).addView(emptyView);*/
        addContentView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listView.setEmptyView(emptyView);
        hide(R.id.emptyview);

    }

    private void displayFooter(final int currTotal, final ListObject lo) {
        hide(R.id.no_data);
        hide(R.id.more_data);
        lo.hasMore = false;


        if (currTotal == 0 && lo.adapter.getCount() == 0) {
            if (lo.noDataTip > 0) {
                UI.setEText(lo.footer, R.id.no_data, lo.noDataTip);
            }
            show(R.id.emptyview);
            show(R.id.no_data);
        } else if (lo.criteria != null && currTotal == lo.criteria.PageSize) {
            /* 2014-07-02 暂时用拖动刷新 */
            // show(footer,R.id.more_data);
            lo.hasMore = true;
        }
    }

    public void moreData(ListObject lo) {
        if (lo.hasMore) {
            show(lo.footer, R.id.more_data);
            lo.criteria.PageIndex += 1;
            refreshData();
        }
    }

    /**
     * 刷新数据
     *
     * @param clearData ，清除现有数据
     */
    public void refreshData(boolean clearData, Class<?> op) {
        if (clearData) {
            ListObject lo = (ListObject) lists.get(op.getName());
            if (lo != null) {
                lo.adapter.clear();// clear data since criteria changed
                if (lo.criteria != null) {
                    lo.criteria.PageIndex = C.PageIndex;
                    hide(lo.footer, R.id.no_data);
                    hide(lo.footer, R.id.more_data);
                }
            }
        }
        refreshData();
    }

    /**
     * 远程方法结束后的工作，子类可覆盖此方法
     *
     * @param op
     * @param result
     */
    public void onPostExecute(Class<?> op, Result result) {
        if (result.code == RT.SUCCESS) {
            ListObject lo = (ListObject) lists.get(op.getName());
            if (lo != null) {
                MyData d = (MyData) result.obj;
                displayFooter(d.size(), lo);// display more data/no data
            }
        }
        if (biz != null) {
            /* 调用MyBiz 方法执行项目自己的通用异步方法 */
            biz.processAsyncResult(op, result);
        }
    }

    /**
     * 打开联系人，子类需要覆盖回调方法selectedContact
     */
    protected void openContacts() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, C.REQUEST_SELECT_CONTACTS);
    }

    /**
     * 打开联系人的回调方法，处理选择联系人后的动作
     *
     * @param contact 选中的联系人
     */
    protected void selectedContact(ContactInfo contact) {

    }

    private ContactInfo getContactPhone(Cursor cursor) {
        ContactInfo contact = new ContactInfo();
        int column = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(column);
        if (phoneNum > 0) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            Cursor cs = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            // 遍历所有的电话号码
            if (cs.moveToFirst()) {
                for (; !cs.isAfterLast(); cs.moveToNext()) {
                    int index = cs
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeIndex = cs
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phoneType = cs.getInt(typeIndex);
                    String phone = cs.getString(index);
                    String name = cs.getString(cs
                            .getColumnIndex(PhoneLookup.DISPLAY_NAME));
                    if (phoneType == 2) {
                        contact.mobilePhone = phone;
                    } else {
                        contact.phoneNumber = phone;
                    }
                    contact.name = name;
                }
                if (!cs.isClosed()) {
                    cs.close();
                }
            }
        }
        return contact;
    }

    /**
     * 虚方法， Handler 要实现的方法
     *
     * @param type 类型标识，以便区分有多个Handler的情况
     * @param msg
     */
    protected void updateUI(int type, Message msg) {

    }

    /**
     * 上传图片方法，供子类调用
     *
     * @param view 触发此按钮弹出菜单“拍照/选图片”
     * @param iv   UI上要修改的图片控件
     */
    protected void getPicture(View view, String[] pic_options, ImageView iv) {
        getPictureView = view;
        this.iv = iv;
        int[] images = new int[]{R.drawable.take_picture,
                R.drawable.choose_picture};
        showListDialog(view, pic_options, images);
    }

    /**
     * 用弹出窗口编辑单个文本内容
     *
     * @param title 窗口显示标题
     * @param view  要修改的主窗口文本控件
     */
    protected void editOneText(CharSequence title, View view) {
        editOneText(title, view, InputType.TYPE_CLASS_TEXT, 0);
    }

    /**
     * 用弹出窗口编辑单个文本内容
     *
     * @param title     窗口显示标题
     * @param view      要修改的主窗口文本控件
     * @param inputType 输入类型
     * @param maxLength 长度
     */
    protected void editOneText(CharSequence title, View view,
                               final int inputType, final int maxLength) {
        CustomDialog cd = new CustomDialog(this, R.layout.text);
        final TextView tv = (TextView) view;
        cd.setInitializer(new Initializer() {
            public void init(Builder alertDialog, View view) {
                String s = tv.getText().toString();
                EditText edit = (EditText) view.findViewById(R.id.input_text);
                edit.setInputType(inputType);
                if (maxLength > 0) {
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                            maxLength)});
                    if (s.length() > maxLength) {
                        s = s.substring(0, maxLength);
                    }
                }
                if (inputType == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
                    edit.setSingleLine(false);
                }
                edit.setText(s);
                edit.setSelection(s.length());
            }
        });
        cd.setOKProcessor(new OKProcessor() {
            public void process(Builder alertDialog, View view) {
                tv.setText(UI.getEText(view, R.id.input_text));
            }
        });
        cd.show(title, true);
    }

    /**
     * 显示一组图像的平铺图
     *
     * @param data 服务端获得的数据MyData
     * @param prop
     */
    protected void showSmallPictures(MyData data, ImageDispProp prop) {
        Bundle b = new Bundle();
        b.putSerializable("data", data);
        b.putSerializable("prop", prop);
        openIntent(ImageGridActivity.class, getText(R.string.image_browse), b);

    }

    /**
     * 通过url获取bitmap对象
     *
     * @param url 缓存时的标识，一般是图像的 URL
     * @return
     */
    protected Bitmap getBitmapFromDiskCache(String url) {
        Bitmap myBitmap = null;
        try {
            myBitmap = Glide.with(this)
                    .load(url)
                    .asBitmap() //必须
                    .placeholder(R.drawable.icon_empty) // 占位符
                    .error(R.drawable.icon_empty)
                    .centerCrop()
                    .into(500, 500)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return myBitmap;


        //  return C.mImageFetcher.getBitmapFromDiskCache(id);
    }

    /**
     * 检查版本
     *
     * @param row         调用远程方法CheckNewVersion后得到的数据
     * @param downloadUrl 需要下载的apk文件地址
     */
    protected void processCheckVersionResult(MyRow row, String downloadUrl) {
        int currVersion = U.getVersion(this);
        int versionCode = row.getInt("versionCode");
        String versionName = row.getString("versionName");
        boolean forceUpdate = row.getBoolean("forceUpdate");
        // String description = row.getString("description");
        boolean manual = row.getBoolean("manual");
        if (versionCode <= currVersion) {
            if (manual) {
                UI.showInfo(this, R.string.no_new_version);// 无新版本，登录时不提示
            }
        } else if (versionCode > currVersion) {
            String ver = getString(R.string.found_new_version);
            String msg = String.format(ver, versionName);
            if (forceUpdate) {
                msg += getString(R.string.force_download);
                UI.showInfo(this, msg, C.DLG_DOWNLOAD_NEW_VERSION, downloadUrl);
            } else {
                msg += getString(R.string.ask_download);
                UI.showOKCancel(this, C.DLG_DOWNLOAD_NEW_VERSION, msg,
                        getText(R.string.check_version), downloadUrl);
            }
        }
    }

    /**
     * 退出系统关闭推送消息 这个方法在应用没有用极光推送的情况下都传false.
     */
    protected void exit(boolean exitApp) {
        ((MyApplication) getApplication()).exit(exitApp);
    }

    /**
     * 处理通用的点击
     *
     * @author Administrator
     */
    class MyOnClickListener implements OnClickListener {
        public void onClick(View view) {
            int id = view.getId();
            int menu = UI.getId(BaseActivity.this, "menu", "id");
            int ic_home = UI.getId(BaseActivity.this, "ic_home", "id");
            if (id == menu) {
                if (menus != null && menus.length > 0) {
                    showMenu(view, 2, 2, menus, menuImages);
                }
            } else if (id == ic_home) {
                goHome();
            } else if (id == R.id.header_back) {
                if (validate()) {
                    back();
                }
                // } else if (id == R.id.ic_search) {
                // handle search in your own file
            }
        }
    }

    private void goHome() {
        if (biz != null) {
            biz.goMain();
        }
    }

    public void home(View view) {
        goHome();
    }

    public void back() {
        finish();
    }

    /**
     * 隐藏对象
     *
     * @param resId
     */
    public void hide(int resId) {
        setV(findViewById(resId), View.GONE);
    }

    public void hide(View view, int resId) {
        setV(view.findViewById(resId), View.GONE);
    }

    public void show(int resId) {
        setV(findViewById(resId), View.VISIBLE);
    }

    public void show(View view, int resId) {
        setV(view.findViewById(resId), View.VISIBLE);
    }

    private void setV(View v, int value) {
        if (v != null) {
            v.setVisibility(value);
        }
    }

    private PopupWindow pop;

    /**
     * Show a pop up window
     *
     * @param resId  layout file in pop up window
     * @param anchor show pop up window below this control
     */
    @SuppressWarnings("deprecation")
    protected void showPopup(int resId, View anchor) {
        if (pop == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(resId, null);
            pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, false);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
        }
        if (pop.isShowing()) {
            pop.dismiss();
            pop = null;
        } else {
            pop.showAsDropDown(anchor);
        }
    }

    protected RadioButton getRadio(int resId) {
        return (RadioButton) findViewById(resId);
    }

    protected void setRadio(int resId, boolean checked) {
        ((RadioButton) findViewById(resId)).setChecked(checked);
    }

    protected CheckBox getCheck(int resId) {
        return (CheckBox) findViewById(resId);
    }

    protected void setCheck(int resId, boolean checked) {
        ((CheckBox) findViewById(resId)).setChecked(checked);
    }

    /**
     * 数据列表的滚动事件处理方法，项目子类覆盖此方法 本 Activity 使用了 initList()方法进行初始化
     *
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    public void specificOnScroll(AbsListView listView,
                                 final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
    }

    /**
     * 点击地图项的回调方法，子类覆盖此方法
     *
     * @param index 地图上的item序号
     */
    public void onMapItemClick(int index) {

    }

    protected void copyToClipboard(CharSequence str) {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("label", str);
        cm.setPrimaryClip(cd);
    }

    /**
     * 纯文字分享
     *
     * @param contentString
     * @author zgh
     * @date 2015-3-3上午11:26:25
     */
    public void shareText(String contentString) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, contentString);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, getTitle()));
    }

    /**
     * 分享图片列表
     *
     * @param imageList
     * @author zgh
     * @date 2015-3-3上午11:29:05
     */
    public void shareImage(ArrayList<Uri> imageList) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageList);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, getTitle()));
    }

    /**
     * 隐藏软键盘
     *
     * @author fengyq
     */
    public void hideSoftKeyboard() {
        UI.hideSoftKeyboard(this);
    }

    /**
     * 检测Sdcard是否存在
     *
     * @author fengyq
     */
    protected static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /*
     * 当程序处于挂起状态被调用比如home键
     * (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        restoreVars();

    }

    /**
     * 判断当前设备是手机还是平板
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void setisShowPictrue(boolean isShowPictrue) {
        this.isShowPictrue = isShowPictrue;
    }

    /**
     * 标题菜单切换
     * 设置点击的文字颜色和线的颜色
     */
    @SuppressLint("ResourceAsColor")
    public void setClickColor(int clickid, int[] clickTextId, int[] clickLineId) {

        for (int i = 0; i < clickTextId.length; i++) {
            if (clickid == clickTextId[i]) {
                setEColor(clickid, R.color.orange);
                setVColor(clickLineId[i], R.color.orange);

            } else {
                setEColor(clickTextId[i], R.color.black);
                setVColor(clickLineId[i], R.color.white_aa);
            }
        }

    }

    /**
     * 页面回到顶部（初始化界面后焦点不在顶部的问题）
     * 在界面最后一个listview高度设置完,界面完全绘制好之后，使用该方法
     * 在OnCreate中调用无效
     *
     * @param scroll
     */
    public static void scrollToBottom(final ScrollView scroll) {

        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                scroll.smoothScrollTo(0, 0);
            }
        });

    }

    public static void setListViewHeightBasedOnChildren(GridView listView, int NumColumns) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = NumColumns;
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight() + 10;
        }

        // 获取listview的布局参数
        LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }


    /**
     * 获取缓存大小
     * 图片缓存加数据缓存
     */
    public String getCacheSize() {
        ACache aCache = ACache.get(this);
        double cacheSize = GlideCacheUtil.getInstance().getCacheSizeDouble(this);
        if (null != aCache) {
            cacheSize += aCache.getCacheSizeDouble();
        }
        return GlideCacheUtil.getInstance().getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     */
    public void clearAllCache() {
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        ACache aCache = ACache.get(this);
        if (null != aCache) {
            aCache.clear();
        }
    }

    public void logd(String o, String methodName) {
        Log.d("logd", this.getClass().getSimpleName() + "+" + methodName + ":" + o);
    }
}