package com.clubank.util;

/**
 * Created by fengyq on 2015/12/5.
 */

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


/**
 * 生成二维码一维码
 * @author fengyq
 *
 */
public final class EncodingHandler {
    private static final int BLACK = 0xff000000;

    //BarcodeFormat.QR_CODE二维码 CODE_128条形码
    public static Bitmap createQRCode(String str, int bitmapwidth,int bitmapHeight,BarcodeFormat format) throws WriterException {

        BitMatrix matrix = new MultiFormatWriter().encode(str,format
                , bitmapwidth, bitmapHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);




        return bitmap;
    }


}