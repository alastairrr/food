package com.food.app.db_package;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.food.app.data_class.ItemData;

public class ItemCursor extends CursorWrapper {
    public ItemCursor(Cursor cursor) { super(cursor); }
    public ItemData getItems()
    {
        String id = getString(getColumnIndex(ItemData.ITEM_ID_PK));
        String rest_id = getString(getColumnIndex(ItemData.RESTAURANT_ID_FK));

        String name = getString(getColumnIndex(ItemData.ITEM_NAME));
        double price = getDouble(getColumnIndex(ItemData.ITEM_PRICE));
        String desc = getString(getColumnIndex(ItemData.ITEM_DESCRIPTION));

        String image_ref = getString(getColumnIndex(ItemData.IMAGE_REF));

        return new ItemData(id, rest_id, name, desc, price, image_ref);
    }

}
