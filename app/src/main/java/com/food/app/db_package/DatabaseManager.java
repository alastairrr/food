package com.food.app.db_package;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.food.app.data_class.CustomerData;
import com.food.app.data_class.ItemData;
import com.food.app.data_class.OrderData;
import com.food.app.data_class.RestaurantData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatabaseManager{
    private SQLiteDatabase db;

    private final ArrayList<RestaurantData> restaurantDataArr = new ArrayList<>();
    private final ArrayList<ItemData> itemDataArr = new ArrayList<>();


    public DatabaseManager() {}

    private void load(Context context) {
        this.db = new DatabaseInitializer( // Open database
                context.getApplicationContext()).getWritableDatabase();

        try (ItemCursor itemCursor = new ItemCursor(
                db.query(ItemData.TABLE_NAME, null, null, null, null, null, null))) {
            itemCursor.moveToFirst();
            while (!itemCursor.isAfterLast()) {
                itemDataArr.add(itemCursor.getItems());
                itemCursor.moveToNext();
            }
        }

        try (RestaurantCursor restaurantCursor = new RestaurantCursor(
                db.query(RestaurantData.TABLE_NAME, null, null, null, null, null, null))) {
            restaurantCursor.moveToFirst();
            while (!restaurantCursor.isAfterLast()) {
                restaurantDataArr.add(restaurantCursor.getRestaurant());
                restaurantCursor.moveToNext();
            }
        }

        Cursor queryCursor = db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, null, null, null, null, null, null, "1");
        if ( queryCursor.getCount() == 0 ) {
            ContentValues cv = new ContentValues();
            cv.putNull(DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID);
            db.replace(DatabaseInitializer.SESSION_DATA_TABLE_NAME, null, cv);
        }
        queryCursor.close();
    }

    public static DatabaseManager initializeDatabase(Context context) {

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.load(context);

        return databaseManager;
    }


    public ArrayList<ItemData> getItemData() { return itemDataArr; }


    public void add_Restaurant(RestaurantData newRestaurant) {
        Cursor queryCursor = db.query(RestaurantData.TABLE_NAME, new String[]{RestaurantData.RESTAURANT_ID_PK}, RestaurantData.RESTAURANT_ID_PK + " =?" , new String[]{newRestaurant.getRestaurantID()}, null, null, null, "1");

        if ( queryCursor.getCount() < 1 ) {
            restaurantDataArr.add(newRestaurant);
            // ...
            ContentValues cv = new ContentValues();
            cv.put(RestaurantData.RESTAURANT_ID_PK, newRestaurant.getRestaurantID());
            cv.put(RestaurantData.RESTAURANT_NAME, newRestaurant.getName());
            cv.put(RestaurantData.RESTAURANT_DESC, newRestaurant.getDescription());
            cv.put(RestaurantData.IMAGE_REF, newRestaurant.getImageRef());
            db.insert(RestaurantData.TABLE_NAME, null, cv);
        }
        queryCursor.close();
    }


    public ArrayList<RestaurantData> getRestaurantData() { return restaurantDataArr; }


    public void add_Item(ItemData newItem) {
        Cursor queryCursor = db.query(ItemData.TABLE_NAME, new String[]{ItemData.ITEM_ID_PK}, ItemData.ITEM_ID_PK + " =?" , new String[]{newItem.getID()}, null, null, null, "1");

        if ( queryCursor.getCount() < 1 ) {
            itemDataArr.add(newItem);
            // ...

            ContentValues cv = new ContentValues();
            cv.put(ItemData.ITEM_ID_PK, newItem.getID());
            cv.put(ItemData.RESTAURANT_ID_FK, newItem.getRestaurantID());
            cv.put(ItemData.ITEM_NAME, newItem.getName());
            cv.put(ItemData.ITEM_PRICE, newItem.getPrice());
            cv.put(ItemData.IMAGE_REF, newItem.getImageRef());
            cv.put(ItemData.ITEM_DESCRIPTION, newItem.getDescription());

            db.insert(ItemData.TABLE_NAME, null, cv);
        }
        queryCursor.close();
    }

    public ArrayList<OrderData> getOrderList() {
        int authenticatedUserID;
        ArrayList<OrderData> orderDataArrayList = new ArrayList<>();


        try (SessionCursor queryCursor_userID = new SessionCursor(db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, new String[]{DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID}, null, null, null, null, null, "1"))) {
            queryCursor_userID.moveToFirst();
            authenticatedUserID = queryCursor_userID.getID();
        }

        try (OrderCursor queryCursor = new OrderCursor( db.query(true, OrderData.TABLE_NAME, null, OrderData.CUSTOMER_ID_FK + " =?", new String[]{String.valueOf(authenticatedUserID)}, OrderData.ORDER_ID_PK, null, null, null))) {
            queryCursor.moveToFirst();
            while (!queryCursor.isAfterLast()) {
                OrderData newOrder = queryCursor.getOrder();

                try (OrderCursor queryOrderItemCursor = new OrderCursor(db.query(OrderData.TABLE_NAME, null, OrderData.CUSTOMER_ID_FK + " =? AND " + OrderData.ORDER_ID_PK + " =?", new String[]{String.valueOf(authenticatedUserID), String.valueOf(newOrder.getOrderId_PK())}, null, null, null, null))) {
                    queryOrderItemCursor.moveToFirst();
                    while (!queryOrderItemCursor.isAfterLast()) {
                        OrderData entry = queryOrderItemCursor.getOrder();
                        newOrder.cart.put(getItemDataFromID(entry.getItemID_FK()), entry.getItemQuantity());
                        queryOrderItemCursor.moveToNext();
                    }

                }
                orderDataArrayList.add(newOrder);
                queryCursor.moveToNext();
            }

        }
        return orderDataArrayList;

    }


    public void add_Order(HashMap <String, Integer> hashMapCart) {
        int authenticatedUserID;

        try (SessionCursor queryCursor_userID = new SessionCursor(db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, new String[]{DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID}, null, null, null, null, null, "1"))) {
            queryCursor_userID.moveToFirst();
            authenticatedUserID = queryCursor_userID.getID();
        }

       try ( OrderCursor queryCursor = new OrderCursor( db.query(OrderData.TABLE_NAME, new String[]{"MAX(" + OrderData.ORDER_ID_PK + ")"}, null, null, null, null, null, null))) {

           if ( queryCursor.moveToFirst()) {
               int maxID = queryCursor.getOrderID();

               Log.d("add_Order", " max id: " + maxID);
               for (Map.Entry<String, Integer> entry : hashMapCart.entrySet()) {
                   if (entry.getValue() != 0 ) {

                       Log.d("USER LOGGED IN IS", String.valueOf(authenticatedUserID));


                       ContentValues cv = new ContentValues();
                       cv.put(OrderData.ORDER_ID_PK, maxID + 1);
                       String date = new SimpleDateFormat("d EEE, yyyy  |  HH:mm", Locale.getDefault()).format(new Date());
                       cv.put(OrderData.ORDER_DATE, date);
                       cv.put(OrderData.CUSTOMER_ID_FK, authenticatedUserID);
                       cv.put(OrderData.ITEM_ID_FK, entry.getKey());
                       cv.put(OrderData.ITEM_QUANTITY, entry.getValue());

                       db.insert(OrderData.TABLE_NAME, null, cv);
                   }
               }
           }
           else {

               for (Map.Entry<String, Integer> entry : hashMapCart.entrySet()) {

                   try (SessionCursor queryCursor_userID = new SessionCursor(db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, new String[]{DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID}, null, null, null, null, "1"))) {
                       queryCursor_userID.moveToFirst();
                       while (!queryCursor_userID.isAfterLast()) {
                           authenticatedUserID = queryCursor_userID.getID();

                           Log.d("USER LOGGED IN IS", String.valueOf(authenticatedUserID));


                           ContentValues cv = new ContentValues();
                           cv.put(OrderData.ORDER_ID_PK, 1);
                           String date = new SimpleDateFormat("yyyy MM dd  |  HH:mm", Locale.getDefault()).format(new Date());
                           cv.put(OrderData.ORDER_DATE, date);
                           cv.put(OrderData.CUSTOMER_ID_FK, authenticatedUserID);
                           cv.put(OrderData.ITEM_ID_FK, entry.getKey());
                           cv.put(OrderData.ITEM_QUANTITY, entry.getValue());

                           db.insert(OrderData.TABLE_NAME, null, cv);
                           queryCursor_userID.moveToNext();
                       }
                   }
               }

           }
       }
    }

    public boolean add_Customer(CustomerData newCustomer) {
        Cursor queryCustomerEmailCursor = db.query(CustomerData.TABLE_NAME, new String[]{CustomerData.CUSTOMER_EMAIL, CustomerData.CUSTOMER_USERNAME},
                CustomerData.CUSTOMER_EMAIL + " =?" + " OR " + CustomerData.CUSTOMER_USERNAME + " =?" , new String[]{newCustomer.getCustomerEmail(), newCustomer.getCustomerUsername()}, null, null, null, "1");

        if ( queryCustomerEmailCursor.getCount() < 1 ) {
            // ...

            ContentValues cv = new ContentValues();
            cv.put(CustomerData.CUSTOMER_FIRSTNAME, newCustomer.getCustomerFirstname());
            cv.put(CustomerData.CUSTOMER_LASTNAME, newCustomer.getCustomerLastname());
            cv.put(CustomerData.CUSTOMER_USERNAME, newCustomer.getCustomerUsername());
            cv.put(CustomerData.CUSTOMER_PASSWORD, newCustomer.getCustomerPassword());
            cv.put(CustomerData.CUSTOMER_EMAIL, newCustomer.getCustomerEmail());

            db.insert(CustomerData.TABLE_NAME, null, cv);
            queryCustomerEmailCursor.close();

            return true;
        }
        else {
            queryCustomerEmailCursor.close();
            return false;
        }
    }


    public ArrayList<ItemData> getItemsFromRestaurantData( RestaurantData restaurantData ) {
        ArrayList<ItemData> retArr = new ArrayList<>();

        try (ItemCursor queryCursor = new ItemCursor(db.query(ItemData.TABLE_NAME, null, ItemData.RESTAURANT_ID_FK + " =?", new String[]{restaurantData.getRestaurantID()}, null, null, null, null))) {
            queryCursor.moveToFirst();
            while (!queryCursor.isAfterLast()) {
                retArr.add(queryCursor.getItems());
                queryCursor.moveToNext();
            }
        }
        return retArr;
    }

    public RestaurantData getRestaurantFromItem(ItemData itemData) {
        RestaurantData restaurantData = null;
        try (RestaurantCursor queryCursor = new RestaurantCursor(db.query(RestaurantData.TABLE_NAME, null, RestaurantData.RESTAURANT_ID_PK + " =?", new String[]{itemData.getRestaurantID()}, null, null, null, "1"))) {
            queryCursor.moveToFirst();
            while (!queryCursor.isAfterLast()) {
                restaurantData = queryCursor.getRestaurant();
                queryCursor.moveToNext();
            }
        }
        return restaurantData;
    }

    public ItemData getItemDataFromID(String itemID) {
        ItemData itemData = null;

        try (ItemCursor queryCursor = new ItemCursor(db.query(ItemData.TABLE_NAME, null, ItemData.ITEM_ID_PK + " =?", new String[]{itemID}, null, null, null, "1"))) {
            queryCursor.moveToFirst();
            while (!queryCursor.isAfterLast()) {
                itemData = queryCursor.getItems();
                queryCursor.moveToNext();
            }
        }
        return itemData;
    }

    public CustomerData getCustomerLoggedIn() {
        CustomerData customerData = null;

        Cursor queryCursorLoggedIn = db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, new String[]{DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID}, DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID + " is not null", null, null, null, null, "1");

        if ( queryCursorLoggedIn.getCount() > 0 ) {


            try (SessionCursor queryCursor_userID = new SessionCursor( db.query(DatabaseInitializer.SESSION_DATA_TABLE_NAME, new String[]{DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID}, null, null, null, null, "1"))) {
                queryCursor_userID.moveToFirst();
                while (!queryCursor_userID.isAfterLast()) {
                    int authenticatedUserID = queryCursor_userID.getID();
                    Log.d("USER LOGGED IN IS", String.valueOf(authenticatedUserID));

                    try (CustomerCursor queryCursor = new CustomerCursor(db.query(CustomerData.TABLE_NAME, null, CustomerData.CUSTOMER_ID_PK + " =?", new String[]{String.valueOf(authenticatedUserID)}, null, null, null, "1"))) {
                        queryCursor.moveToFirst();
                        while (!queryCursor.isAfterLast()) {
                            customerData = queryCursor.getCustomer();
                            queryCursor.moveToNext();
                        }
                    }
                    queryCursor_userID.moveToNext();
                }
            }
        }
        queryCursorLoggedIn.close();
        return customerData;

    }

    public boolean authenticateUser( String username, String password ) {
        Cursor queryCursor = db.query(CustomerData.TABLE_NAME, new String[]{CustomerData.CUSTOMER_USERNAME, CustomerData.CUSTOMER_PASSWORD}, CustomerData.CUSTOMER_USERNAME + " =?" + " AND " + CustomerData.CUSTOMER_PASSWORD + " =?", new String[]{username, password}, null, null, null, "1");
        if (queryCursor.getCount() < 1) {
            queryCursor.close();
            return false;
        }

        else {

            try (CustomerCursor queryCursor_userID = new CustomerCursor(db.query(CustomerData.TABLE_NAME, null, CustomerData.CUSTOMER_USERNAME + " =?" + " AND " + CustomerData.CUSTOMER_PASSWORD + " =?", new String[]{username, password}, null, null, null, "1"))) {
                queryCursor_userID.moveToFirst();
                while (!queryCursor_userID.isAfterLast()) {
                    ContentValues cv = new ContentValues();
                    int authenticatedUserID = queryCursor_userID.getCustomerID();
                    Log.d("app - i authenticated", String.valueOf(authenticatedUserID));
                    cv.put(DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID, authenticatedUserID);
                    db.update(DatabaseInitializer.SESSION_DATA_TABLE_NAME, cv, null, null);
                    queryCursor_userID.moveToNext();
                }
            }

            queryCursor.close();
            return true;
        }
    }

    public void logOut() {
        ContentValues cv = new ContentValues();

        cv.putNull(DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID);
        db.update(DatabaseInitializer.SESSION_DATA_TABLE_NAME, cv, DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID + " is not null", null);

    }

}
