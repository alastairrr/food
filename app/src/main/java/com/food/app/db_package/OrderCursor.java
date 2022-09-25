package com.food.app.db_package;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.food.app.data_class.OrderData;

public class OrderCursor extends CursorWrapper {
    public OrderCursor(Cursor cursor) { super(cursor); }
    public OrderData getOrder() {
        int id = getInt(getColumnIndex(OrderData.ORDER_ID_PK));
        String date = getString(getColumnIndex(OrderData.ORDER_DATE));
        int customerID_FK = getInt(getColumnIndex(OrderData.CUSTOMER_ID_FK));
        String itemID_FK = getString(getColumnIndex(OrderData.ITEM_ID_FK));
        int itemAmount = getInt(getColumnIndex(OrderData.ITEM_QUANTITY));

        return new OrderData(id, date, customerID_FK, itemID_FK, itemAmount);
    }
    public int getOrderID()
    {
        return getInt(0);
    }


}
