package com.clubank.device;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.domain.S;
import com.clubank.op.CheckNewVersion;
import com.clubank.op.Login;
import com.clubank.util.BizDBHelper;
import com.clubank.util.DB;
import com.clubank.util.EGDBHelper;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyDBHelper;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

import java.io.File;

public class MyBiz extends BaseBiz {

    public MyBiz(BaseActivity ba) {
        super(ba);
    }

    public static void setServerType(BaseActivity ba, int type) {
        C.baseUrl = BC.BASE_URL_INTERNET;
        if (!ba.settings.getString("wsUrl", "").equals("")) {
            C.wsUrl = ba.settings.getString("wsUrl", "");
        } else {
            C.wsUrl = C.baseUrl + "DService.asmx";
        }
        ba.setServerType(type);

    }

    @Override
    public void checkLogin(int loginType) {
        if (S.token != null) {
            ba.logined(loginType);
            return;
        } else {
            String userCode = ba.settings.getString("userCode", "");
            String password = ba.settings.getString("password", "");
            String imei = U.getUdid(ba);
            String machineName = Build.MODEL + "(" + Build.VERSION.RELEASE
                    + ")";
            if (userCode != null && password != null) {
                new MyAsyncTask(ba, Login.class).execute(userCode, password,
                        imei, machineName, "1.0");
            } else {
                openLoginActivity();
            }
        }
    }

    @Override
    public void goMain() {
        ba.openIntent(MainActivity.class, ba.getString(R.string.app_name));
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(ba, LoginActivity.class);
        intent.putExtra("title", ba.getText(R.string.app_name));
        ba.startActivityForResult(intent, C.REQUEST_LOGIN);
    }

    @Override
    public void loginSuccess(Result result) {
        MyRow row = (MyRow) result.obj;
        S.userCode = row.getString("UserCode");
        S.userName = row.getString("UserName");
        S.shopCode = row.getString("ShopCode");
        /* S.allowWithdrawDish = row.getBoolean("AllowWithdrawDish"); */
        S.AllowDiscount = row.getBoolean("AllowDiscount");
        S.AllowCreditToCard = row.getBoolean("AllowCreditToCard");
        S.bill.BillShop = S.shopCode;
        S.shopName = row.getString("ShopName");
        S.token = row.getString("Token");
    }

    /**
     * serverType 一般1 代表 远程 2 代表测试服务端 应用初始化时在splash Oncreat中赋值
     */
    @Override
    public void setServerType(int serverType) {
        C.baseUrl = BC.BASE_URL_INTERNET;
        if (!ba.settings.getString("wsUrl", "").equals("")) {
            C.wsUrl = ba.settings.getString("wsUrl", "");
        } else {
            C.wsUrl = C.baseUrl + "DService.asmx";
        }
        ba.setServerType(serverType);
    }

    /**
     * 恢复全局变量。因为系统会在休眠后不定时将静态变量清空， <br/>
     * 因此静态变量必须在赋值后存入硬盘。在使用时恢复。
     */
    @Override
    public void restoreVars() {
        // TODO Auto-generated method stub

    }

    @Override
    public void processAsyncResult(Class<?> op, Result result) {
        if (op == Login.class) {
            if (result.code == RT.SUCCESS) {
                loginSuccess(result);
                ba.logined(loginType);
            } else {
                openLoginActivity();
            }
        } else if (op == CheckNewVersion.class) {
            if (result.code == RT.SUCCESS) {
                MyRow row = (MyRow) result.obj;
                // processCheckVersionResult(row);
                String fname = "";
                try {
                    PackageInfo info = ba.getPackageManager().getPackageInfo(
                            ba.getPackageName(), 0);
                    int n = info.packageName.lastIndexOf('.');
                    fname = info.packageName.substring(n + 1);
                } catch (NameNotFoundException e) {
                }
                ba.processCheckVersionResult(row, BC.BASE_URL_INTERNET + fname
                        + ".apk");
            }

        }
    }

    /**
     * 旧版本升级方法（目前版本升级都会报服务器连接错误）
     *
     * @param
     */
	/*private void processCheckVersionResult(MyRow row) {
		String sVersionCode = (String) row.get("versionCode");
		int versionCode = Integer.valueOf(sVersionCode);
		String versionName = (String) row.get("versionName");
		String description = (String) row.get("description");
		boolean manual = (Boolean) row.get("manual");
		if (versionCode <= U.getVersion(ba)) {
			if (manual) {// 如果是手动检查版本
				UI.showInfo(ba, R.string.msg_no_new_version);
			}
		} else if (versionCode > U.getVersion(ba)) {
			String ver = ba.getText(R.string.msg_found_new_version).toString();
			String msg = String.format(ver, versionName);
			String[] descs = description.split(";");
			String url = BC.BASE_URL_INTERNET;
			url += "Dining.apk";// 球会版

			for (int i = 0; i < descs.length; i++) {
				msg += System.getProperty("line.separator") + descs[i];
			}
			msg += System.getProperty("line.separator")
					+ ba.getText(R.string.msg_ask_download).toString();
			UI.showOKCancel(ba, C.DLG_DOWNLOAD_NEW_VERSION, msg,
					ba.getText(R.string.m_check_version), url);
		}
	}*/
    @Override
    public void checkVersion(boolean isManual) {
        new MyAsyncTask(ba, CheckNewVersion.class).execute(U.getVersion(ba),
                isManual);
    }

    @Override
    public MyDBHelper initDBHelper(Context context) {
        C.DB_VERSION = BC.DB_VERSION;
        MyDBHelper helper = null;
        if (BC.engineerMode) {
            helper = new EGDBHelper(ba, ba.getExternalFilesDir(null) + File.separator + BC.DB_NAME, null, 3);
        } else {
            helper = new BizDBHelper(ba, C.DB_NAME, null, C.DB_VERSION);
        }

        return helper;
    }

    @Override
    public void UploadFile(Context context, String filePath) {
        // TODO Auto-generated method stub

    }

}
