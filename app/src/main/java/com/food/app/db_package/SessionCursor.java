package com.food.app.db_package;

import android.database.Cursor;
import android.database.CursorWrapper;

public class SessionCursor extends CursorWrapper {
    public SessionCursor(Cursor cursor) { super(cursor); }
    public int getID()
    {
        return getInt(getColumnIndex(DatabaseInitializer.SESSION_LOGGED_CUSTOMER_ID));
    }

}
