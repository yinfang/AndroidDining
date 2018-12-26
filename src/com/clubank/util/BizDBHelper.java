package com.clubank.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BizDBHelper extends MyDBHelper {
    //点餐项目的第一个数据哭版本号为１,在android:versionCode="14" android:versionName="2.4.9 " 的基础上添加功能时改为2


    private Context context;

    public BizDBHelper(Context context, String name, CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        createTables(db);
    }


    //因为客户安装为卸载后安装，所以不在使用数据库升级方法中的代码
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    public void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE t_DiningArea");
        db.execSQL("DROP TABLE t_DiningTable");
        db.execSQL("DROP TABLE t_ItemCategory");
        db.execSQL("DROP TABLE t_Dish");
        db.execSQL("DROP TABLE T_CookingStyle");
        db.execSQL("DROP TABLE t_PackageDish");
        db.execSQL("DROP TABLE T_KitchenType");
        db.execSQL("DROP TABLE T_CookingFlavor");
        db.execSQL("DROP TABLE T_RecommendDish");
    }

    public void createTables(SQLiteDatabase db){
        //t_Dish　　T_RecommendDish两张表添加HPrice字段,用来存储假日价格
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS t_DiningArea(AreaCode TEXT PRIMARY KEY, " +
                    "AreaName TEXT, Description TEXT, ShopCode TEXT,Prefix TEXT, Sort INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS t_DiningTable(Code TEXT PRIMARY KEY, Name " +
                    "TEXT, AreaCode TEXT, Status INTEGER,Sort INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS t_ItemCategory(Code TEXT PRIMARY KEY, Name " +
                    "TEXT, EnName TEXT, Sort INTEGER,ParentCode TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS t_Dish(Code TEXT PRIMARY KEY, Name TEXT, " +
                    "Category TEXT, EnName TEXT, ShortCut TEXT, Description TEXT,EnDescription " +
                    "TEXT, Price REAL,HPrice REAL,SmallPicture BLOB,LargePicture BLOB, CanDiscount BOOLEAN," +
                    "SoldOut BOOLEAN, Hits INTEGER,ImageDate TEXT,Pricing INTEGER,Updated " +
                    "INTEGER,IfStyle BOOLEAN," +
                    "ExtCode TEXT,ComposeType INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS T_CookingStyle(ID INTEGER PRIMARY KEY,ItemCode" +
                    " TEXT, Name TEXT,PriceChange REAL, Remarks TEXT, Sort Integer)");

// db.execSQL("CREATE TABLE IF NOT EXISTS T_CookingFlavor(Code TEXT PRIMARY KEY,Name TEXT,
// Description TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS t_PackageDish(ID INTEGER PRIMARY KEY, " +
                    "ParentCode TEXT, ItemCode TEXT, ItemName TEXT, Quantity INTEGER, IfCahnge " +
                    "INTEGER ,Hits INTEGER,ImageDate TEXT,Pricing INTEGER,Updated INTEGER,ExtCode " +
                    "TEXT)");
            //version 2新建立的表
            db.execSQL("CREATE TABLE IF NOT EXISTS T_KitchenType(ID INTEGER PRIMARY KEY,Code " +
                    "TEXT,Name TEXT,Memo TEXT,Sort Integer)");
            //version 3改变的表
            db.execSQL("CREATE TABLE IF NOT EXISTS T_CookingFlavor(Code TEXT PRIMARY KEY,Name " +
                    "TEXT,Description TEXT,FlavorType TEXT,TypeName TEXT)");

            //菜品推荐的数据库表
            db.execSQL("CREATE TABLE IF NOT EXISTS T_RecommendDish(Code TEXT PRIMARY KEY,Name " +
                    "TEXT,EnName TEXT,Shortcut TEXT,Category TEXT,Description TEXT,EnDescription " +
                    "TEXT,Price REAL,HPrice REAL,Pricing INTEGER,CanDiscount BOOLEAN,Hits INTEGER,SoldOut " +
                    "BOOLEAN, ExtCode TEXT,ComposeType INTEGER,ImageDate TEXT,SmallPicture BLOB," +
                    "LargePicture BLOB,Updated INTEGER)");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
