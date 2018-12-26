package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.clubank.dining.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.util.U;

public class SplashActivity extends BaseActivity {
    /**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BC.exit = false;
        C.DB_VERSION = BC.DB_VERSION;
        C.udid = U.getUdid(this);
        setContentView(R.layout.splash);
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2500);
                    }
                } catch (InterruptedException ex) {
                }
                saveSetting("namespace", BC.NameSpace);
                int serverType = settings.getInt("serverType",
                        C.SERVER_INTERNET);
                biz.setServerType(serverType);
                saveSetting("inputcardno", true);//默认消费卡必填
                BC.CLUB_ID = settings.getString("CLUB_ID", BC.CLUB_ID);
//				String userCode=settings.getString("userCode", "")	;
//				String password=settings.getString("password", "");	
//				String imei = "";
//				String machineName = Build.MODEL + "(" + Build.VERSION.RELEASE
//						+ ")";
//				if (userCode != null && password != null) {
//					login(userCode,password,imei,machineName);
//				} else {
//					biz.openLoginActivity();
//				}		

                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        mSplashThread.start();
    }

	/*private void login(String userCode,String password,String imei, String machineName) {
		Looper.prepare();
		new MyAsyncTask(this, Login.class,false).execute(userCode, password,
				imei, machineName, "1.0");
	 Looper.loop();
	}*/
	
	/*@Override
	public void onPostExecute(Class<?> op, Result result) {
		super.onPostExecute(op, result);
		if (op == Login.class) {
			if (result.code == BC.RESULT_SUCCESS) {
				biz.loginSuccess(result);
				biz.goMain();
				setResult(RESULT_OK);
				finish();
			} else {
				biz.openLoginActivity();
			}
		}
	}*/

    /**
     * guide界面时触摸唤醒应用，进入应用
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }

}
