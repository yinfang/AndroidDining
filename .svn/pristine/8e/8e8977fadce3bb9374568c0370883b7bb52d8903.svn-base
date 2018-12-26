package com.clubank.device;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clubank.SignatureUtils.SignaturePad;
import com.clubank.api.PosApi;
import com.clubank.dining.R;
import com.clubank.domain.Result;
import com.clubank.util.RxNetworking;
import com.clubank.util.U;
import com.clubank.util.UI;
import com.clubank.util.WaitingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.functions.Action1;

public class SignatureActivity extends BaseActivity {
    private String paybillno = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private ImageView mClearButton;
    private Button mSaveButton;
    private TextView tips;
    private String rlt = "";
    private Bitmap signatureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_signature);
        paybillno = getIntent().getStringExtra("data");
        hide(R.id.header_back);
        setHeaderTitle("签名");
        tips = (TextView) findViewById(R.id.tips);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //当垫被触摸事件触发
                // Toast.makeText(MainActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
                tips.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSigned() {
                //当垫被签名事件触发
                // Toast.makeText(SignatureActivity.this, "在开始签名", Toast.LENGTH_SHORT).show();
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                //当垫被清除触发事件
                tips.setVisibility(View.VISIBLE);
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });
        mClearButton = (ImageView) findViewById(R.id.clear);
        mSaveButton = (Button) findViewById(R.id.confirm);
        mSaveButton.setEnabled(false);

    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SignatureActivity.this, "无法将图像写入外部存储器", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mSignaturePad.clear();
                break;
            case R.id.confirm:
                mSaveButton.setEnabled(false);
                compressToBitmap();
                break;
        }
    }

    private void compressToBitmap() {
        signatureBitmap = mSignaturePad.getSignatureBitmap();
        File photo = new File(getAlbumStorageDir("lite_pos"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
        try {
            saveBitmapToJPG(signatureBitmap, photo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {//压缩图片 上传签名
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPath(), opts);
            opts.inSampleSize = calculateInSampleSize(opts, 500, 600);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(photo.getPath(), opts);
            FileOutputStream fos = new FileOutputStream(photo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            signatureBitmap = bitmap;
            rlt = U.bitmapToBase64(signatureBitmap);
            SaveSignature();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;//获取图片的高
        final int width = options.outWidth;//获取图片的宽
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;//求出缩放值
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        stream.close();
    }

    private void SaveSignature() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(curDate);
        Observable.Transformer<Result, Result> networkingIndicator
                = RxNetworking.bindConnecting(new WaitingDialog(sContext));
        PosApi.getInstance(sContext)
                .AndroidInsertIntoScreen(paybillno, rlt, date)
//                .compose(this.<Result>bindToLifecycle())
                .compose(networkingIndicator)
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        if (result.code == 0) {
                            UI.showToast(sContext, "上传签名成功");
                            Intent i = new Intent(SignatureActivity.this, MainActivity.class);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        } else {
                            UI.showInfo(sContext, "上传签名失败" + result.msg);
                            mSaveButton.setEnabled(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        UI.showToast(sContext, e.getMessage());
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
        builder.setTitle("系统提示");
        builder.setMessage("不签名退出！");
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SignatureActivity.this, MainActivity.class);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        dialog.dismiss();
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
