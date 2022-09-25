package com.food.app.db_package;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.food.app.data_class.CustomerData;
import com.food.app.data_class.ItemData;
import com.food.app.data_class.OrderData;
import com.food.app.data_class.RestaurantData;

public class DatabaseInitializer extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "food_database.db";

    public static final String SESSION_DATA_TABLE_NAME = "SessionData";
    public static final String SESSION_LOGGED_CUSTOMER_ID = "CurrentLoggedUser";

    public DatabaseInitializer(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("CREATE TABLE " + SESSION_DATA_TABLE_NAME + "(" +
                SESSION_LOGGED_CUSTOMER_ID + " INTEGER)" );


        // <TABLE> ==== ITEM DATA ==== <TABLE> //
        sqLiteDatabase.execSQL("CREATE TABLE " + ItemData.TABLE_NAME + "(" +
                ItemData.ITEM_ID_PK + " TEXT PRIMARY KEY, " +
                ItemData.RESTAURANT_ID_FK + " TEXT," +
                ItemData.ITEM_NAME + " TEXT, " +
                ItemData.ITEM_DESCRIPTION + " TEXT, " +
                ItemData.ITEM_PRICE + " REAL, " +
                ItemData.IMAGE_REF + " TEXT, " +
                "FOREIGN KEY (" + ItemData.RESTAURANT_ID_FK + ") " +
                "REFERENCES " + RestaurantData.TABLE_NAME + " ("+ RestaurantData.RESTAURANT_ID_PK + ")) ");


        // <TABLE> ==== RESTAURANT DATA ==== <TABLE> //
        sqLiteDatabase.execSQL("CREATE TABLE " + RestaurantData.TABLE_NAME + "(" +
                RestaurantData.RESTAURANT_ID_PK + " TEXT PRIMARY KEY, " +
                RestaurantData.RESTAURANT_NAME + " TEXT, " +
                RestaurantData.RESTAURANT_DESC + " TEXT, " +
                RestaurantData.IMAGE_REF + " TEXT)");


        // <TABLE> ==== CUSTOMER DATA ==== <TABLE> //
        sqLiteDatabase.execSQL("CREATE TABLE " + CustomerData.TABLE_NAME + "(" +
                CustomerData.CUSTOMER_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CustomerData.CUSTOMER_FIRSTNAME + " TEXT, " +
                CustomerData.CUSTOMER_LASTNAME + " TEXT, " +
                CustomerData.CUSTOMER_USERNAME + " TEXT, " +
                CustomerData.CUSTOMER_PASSWORD + " TEXT, " +
                CustomerData.CUSTOMER_EMAIL + " TEXT)");


        // <TABLE> ==== ORDER DATA ==== <TABLE> //
        sqLiteDatabase.execSQL("CREATE TABLE " + OrderData.TABLE_NAME + "(" +
                OrderData.ORDER_ID_PK + " INTEGER NOT NULL, " +
                OrderData.ORDER_DATE + " TEXT, " +
                OrderData.CUSTOMER_ID_FK + " TEXT, " +
                OrderData.ITEM_ID_FK + " TEXT, " +
                OrderData.ITEM_QUANTITY + " TEXT, " +

                "FOREIGN KEY (" + OrderData.CUSTOMER_ID_FK + ") " +
                "REFERENCES " + CustomerData.TABLE_NAME + " ("+ CustomerData.CUSTOMER_ID_PK + "), " +

                "FOREIGN KEY (" + OrderData.ITEM_ID_FK + ") " +
                "REFERENCES " + ItemData.TABLE_NAME + " ("+ ItemData.ITEM_ID_PK + ")) ");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}