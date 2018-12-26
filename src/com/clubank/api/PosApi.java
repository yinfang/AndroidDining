package com.clubank.api;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import rx.Observable;

/**
 * Created by yeqing on 2016/6/14.
 * PosApi
 */
public class PosApi extends BaseApi {
    private static PosApi sApi;

    public static PosApi getInstance(Context context) {
        if (sApi == null) {
            sApi = new PosApi(context);
        }
        return sApi;
    }

    public PosApi(Context context) {
        super(context);
    }

    /**
     * 在线支付，增加一条支付请求
     *
     * @param module   (支付方式，支付宝 = 1,微信 = 2，银联= 7)
     * @param mode     (用户用手机扫 = 1,用户条码被扫 = 2（要提供authcode)
     * @param amount
     * @param authCode
     * @return
     */
    public Observable<Result> SubmitOnlinePay(String module, String mode, String amount, String authCode) {
        String func = "SubmitOnlinePay";
        MyRow row = new MyRow();
        row.put("payModule", module);
        row.put("mode", mode);
        row.put("amount", amount);
        row.put("authCode", authCode);
        row.put("device", C.udid);

        return getObservable(func, row);
    }

    /**
     * 获取支付二维码
     *
     * @param SerialNo
     * @return
     */
    public Observable<Result> GetOnlinePayQRcode(String SerialNo) {
        String func = "GetOnlinePayQRcode";
        MyRow row = new MyRow();
        row.put("SerialNo", SerialNo);

        return getObservable(func, row);
    }

    /**
     * 获取支付结果
     *
     * @param SerialNo
     * @return
     */
    public Observable<Result> GetOnlinePay(String SerialNo) {
        String func = "GetOnlinePay";
        MyRow row = new MyRow();
        row.put("serialno", SerialNo);

        return getObservable(func, row);
    }

    /**
     * 营业点账单结账
     */
    public Observable<Result> CheckoutWorkshopBill(String billcode, String shopCode, String list) {
        String func = "CheckoutWorkshopBill";
        MyRow row = new MyRow();
        row.put("billcode", billcode);
        row.put("shopcode", shopCode);
        row.put("list", list);

        return getObservable(func, row);
    }

    /**
     * 补签会员挂账
     *
     * @return
     */
    public Observable<Result> PostBillByMemNo(String memno, String shopcode, String guestnum, String tableno, String billcode, String decTotal) {
        String func = "PostBillByMemNo";
        MyRow row = new MyRow();
        row.put("memno", memno);
        row.put("shopcode", shopcode);
        row.put("guestnum", guestnum);
        row.put("tableno", tableno);
        row.put("billcode", billcode);
        row.put("decTotal", decTotal);

        return getObservable(func, row);
    }

    /**
     * 会员挂账
     *
     * @param memno
     * @param shopcode
     * @param guestnum
     * @param list
     * @return
     */
    public Observable<Result> PostBillViaMemNo(String memno, String shopcode, String guestnum, String list, String tableno, String remarks) {
        String func = "PostBillViaMemNo";
        MyRow row = new MyRow();
        row.put("memno", memno);
        row.put("shopcode", shopcode);
        row.put("guestnum", guestnum);
        row.put("list", list);
        row.put("tableno", tableno);
        row.put("remarks", remarks);

        return getObservable(func, row);
    }


    /**
     * 获取会员照片
     *
     * @param memNo
     * @return
     */
    public Observable<Result> GetMemberPhoto(String memNo) {
        String func = "GetMemberPhoto";
        MyRow row = new MyRow();
        row.put("memNo", memNo);

        return getObservable(func, row);
    }

    /**
     * 获取会员签名
     *
     * @param memNo
     * @return
     */
    public Observable<Result> GetMemberSignPhoto(String memNo) {
        String func = "GetMemberSignPhoto";
        MyRow row = new MyRow();
        row.put("memNo", memNo);

        return getObservable(func, row);
    }

    /**
     * 保存签名
     */
    public Observable<Result> AndroidInsertIntoScreen(String paybillno, String base64, String stime) {
        String func = "AndroidInsertIntoScreen";
        MyRow row = new MyRow();
        row.put("paybillno", paybillno);
        row.put("base64", base64);
        row.put("stime", stime);

        return getObservable(func, row);
    }
}
