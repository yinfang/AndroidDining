package com.clubank.device;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.clubank.dining.R;
import com.clubank.domain.S;

public class ChectoutPaySuccessfulActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chectout_pay_successful);
        setHeaderTitle(getString(R.string.checkout_pay_successful));
        hide(R.id.ic_back);
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.header_save:
                openIntent(MainActivity.class, "");
                finish();
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
