package com.food.app.db_package;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.food.app.data_class.RestaurantData;

public class RestaurantCursor extends CursorWrapper {
    public RestaurantCursor(Cursor cursor) { super(cursor); }

    public RestaurantData getRestaurant()
    {
        String id = getString(getColumnIndex(RestaurantData.RESTAURANT_ID_PK));
        String name = getString(getColumnIndex(RestaurantData.RESTAURANT_NAME));
        String desc = getString(getColumnIndex(RestaurantData.RESTAURANT_DESC));
        String image_ref = getString(getColumnIndex(RestaurantData.IMAGE_REF));

        return new RestaurantData(id, name, desc, image_ref);
    }

}
