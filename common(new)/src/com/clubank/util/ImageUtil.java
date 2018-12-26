package com.clubank.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import com.clubank.common.R;

public class ImageUtil {

    public static Bitmap getBitmap(byte[] b) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 1; // width，hight设为原来的十分一
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return BitmapFactory.decodeStream(bais, null, opt);
    }

    public static String saveTempBitmap(Bitmap bmp, String prefix, String suffix) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File file = File.createTempFile(prefix, suffix,
                        Environment.getExternalStorageDirectory());
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(CompressFormat.JPEG, 70, fos);
                fos.flush();
                fos.close();
                String filePath = file.getAbsolutePath();
                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存图片到磁盘上，类型统一为PNG
     *
     * @param context
     * @param bmp     要保存的图像
     * @param path    要保存的路径
     * @param fname   文件名。如果为空，则系统会生成一个
     */
    public static void saveImage(Context context, Bitmap bmp, String path,
                                 String fname) {
        File rootDir = Environment.getExternalStorageDirectory();
        InputStream in = bitmap2InputStream(bmp);
        // Used the File-constructor

        // Transfer bytes from in to out
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int len;
        String msg = context.getString(R.string.save_picture_failed);
        if (fname == null || fname.equals("")) {
            fname = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    .format(new Date());
        }
        fname += ".png";
        File dir = new File(rootDir, path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            out = new FileOutputStream(new File(dir, fname));
            // A little more explicit
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, len);
            }
            msg = context.getString(R.string.save_picture_as);
            msg = String.format(msg, dir.getAbsolutePath() + "/" + fname);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static InputStream drawable2InputStream(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2InputStream(bitmap);
    }

    public static InputStream bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static InputStream bitmap2InputStream(Bitmap bm) {
        return bitmap2InputStream(bm, 100);
    }

    /**
     * 存储图片16进制串为图片文件
     *
     * @param src    图片16进制文本串
     * @param output 要保存的图像文件路径
     */
    public File saveToImgFile(String src, String output) {
        if (src == null || src.length() == 0) {
            return null;
        }
        try {
            File file = new File(output);
            FileOutputStream out = new FileOutputStream(file);
            byte[] bytes = src.getBytes();
            for (int i = 0; i < bytes.length; i += 2) {
                out.write(charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]));
            }
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int charToInt(byte ch) {
        int val = 0;
        if (ch >= 0x30 && ch <= 0x39) {
            val = ch - 0x30;
        } else if (ch >= 0x41 && ch <= 0x46) {
            val = ch - 0x41 + 10;
        }
        return val;
    }
}
