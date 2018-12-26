package com.clubank.domain;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/***
 * Constants definition
 *
 * @author chenyh
 *
 */
@SuppressLint("SimpleDateFormat")
public class BC {
    //套餐测试地址192.168.0.51.8020
    public static final int type = 0;// 0标准版
    // 1凯歌（厦门）版（备注可选择）2.吉云轩版（口味index从1开始pagesize=2000一次获取完）
    public static boolean engineerMode = false;
    public static final String BUSINESS_PHONE = "4000166396";
    public static final String PROVIDER_PHONE = "075588824068";
    public static final String APP_ID = "Dining";
    public static final String DB_NAME = "Dining.db";
    public static final int timeout = 500;
    public static final String NameSpace = "http://www.clubank.com/";
    public static final String demoData = "http://www.golfbaba.com/dining.zip";
    //    public static final String demoData = "http://192.168.0.202/dining.zip";
//    public static final String demoData = "http://192.168.0.44/dining.zip";
    public static final String zipfile = "dining.zip";

    public static String BASE_URL_INTERNET = "http://192.168.0.55:9009/";
    // public static String BASE_URL_INTERNET = "http://119.122.151.148:8099/";
    // public static String wsUrl = BASE_URL_INTERNET + "DService.asmx";

    /**
     * token for authentication in server side
     */
    public static final int cnt = 25;// 每页数据条数
    public static final int compress = 80;
    public static final int large = 500;// 头像大图
    public static final String UNSPECIFIED_IMAGE = "image/*";

    public static final int TIMEOUT = 30000;

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_SOCKET_TIMEOUT = 100;
    public static final int RESULT_SOCKET_ERROR = 101;
    public static final int RESULT_SERVER_ERROR = 102;
    public static final int RESULT_UNKNOWN_ERROR = 99;//
    public static final int RESULT_LOWER_CLIENT_VERSION = 105;
    public static final int RESULT_ENGINEER_MODE = 900;

    public static final int RESULT_AUTHENTICATION_FAILED = 403;// 机器未授权
    public static final int RESULT_OVER_POINTS = 5;// 超过点数
    public static final int RESULT_OPERATION_FAILED = -1;// 操作失败
    public static final int RESULT_INTERFACE_ERROR = 104;// 意外的接口错误

    public static final int RESULT_INVALID_USERNAME_PASSWORD = 1001;// 用户名或密码不正确
    public static final int RESULT_USER_DISABLED = 1002;// 用户名失效
    public static final int RESULT_PASSWORD_EXPIRED = 1003;// 密码过期
    public static final int RESULT_REGIST_CONSUME_CARD = 2001;// 未登记的卡号
    public static final int RESULT_INVALID_CONSUME_CARD = 2002;// 无效的卡号
    public static final int RESULT_OLD_BILL = 3001;// 往日账单不允许处理
    public static final int RESULT_NO_CHECKIN_EXIST = 3002;// 没有找到登记客人信息
    public static final int RESULT_BILL_CHECKED_OUT = 3003;// 此客人当前状态为结帐
    public static final int RESULT_UNCHARGEABLE_BILL = 3004;// 不允许记账
    public static final int RESULT_CONSUME_CARD_EXPIRED = 3005;// 消费卡过期
    public static final int RESULT_CONSUME_CARD_LEVEL = 3006;// 消费卡过期
    public static final int RESULT_INSUFFICIENT_PREVILEGE = 3008;// 您的打折权限不够

    public static final DecimalFormat nf_i = new DecimalFormat("#,##0");// integer
    public static final DecimalFormat nf_a = new DecimalFormat("#,##0.00");// amount
    public static final DecimalFormat nf_l = new DecimalFormat("###0.000000");// GPS
    public static final DateFormat df_y = new SimpleDateFormat("yyyy");
    public static final DateFormat df_yM = new SimpleDateFormat("yyyy-MM");
    public static final DateFormat df_yMd = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat df_yMdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final DateFormat df_yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat df_Hm = new SimpleDateFormat("HH:mm");

    public static final String OP_CHECK_NEW_VERSION = "checkNewVersion";
    public static final String OP_LOGIN = "login";
    final public static int MENU_ORDER = 9;
    final public static int MENU_SHOW_BIG_IMAGE = 10;
    public static final int QUER_PACKAGE_DETAIL = 13;
    final public static int NOTIFY_PRINTER_ERROR = 0;

    // public static String username;
    // public static String password;
    // public static String realName;
    public static boolean exit;

    public static final int DISCOUNT_TYPE_BY_RATE = 1;
    public static final int DISCOUNT_TYPE_BY_FIXED = 2;
    // 扫描菜品返回码
    public static final int REQUEST_SCAN_DISH = 10201;
    // 确认扫描
    public static final int DLG_CONFIRM_SCAN_DELIVER = 10231;
    public static String conversionCode = "";
    // 登陆过期（token失效需重新登录）
    public static final int LOGIN_OUT_TIME = 3007;

    public static final int DB_VERSION = 4;//数据库版本
    //    //默认是否显示入厨方式
    public static final boolean showKitchenType = true;

    public static final String DiningDebugPathName = "DiningDebug";

    public static boolean IsHoliday = false;//true代表假日,false代表普通日期

    public static String keyA = "SEIN2K"; // NFC keyA
    public static String VERSION = "7.5";
    public static final int REQUEST_PAY = 10008;
    public static final int REQUEST_CHOOSE_MEMBER = 10009;
    public static final int CLUB_OFFLINE = 10014;// 球会端不在线(特殊，暂时放common)
    public static final int UNKNOWN_ERROR = 10099;// 其它错误
    public static String CLUB_ID = "clubid";
    public static String PAY_BASE_URL = "http://onlinepay.clubank.com";
    public static final String PAY_BASE_SERVICE_ASMX = "/PosCommand/PosCommand.ashx";
    public static String payWsUrl = PAY_BASE_URL + PAY_BASE_SERVICE_ASMX;
}
