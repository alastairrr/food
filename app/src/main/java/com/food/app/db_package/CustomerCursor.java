package com.food.app.db_package;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.food.app.data_class.CustomerData;

public class CustomerCursor extends CursorWrapper {
    public CustomerCursor(Cursor cursor) { super(cursor); }
    public CustomerData getCustomer()
    {
        String firstname = getString(getColumnIndex(CustomerData.CUSTOMER_FIRSTNAME));

        String lastname = getString(getColumnIndex(CustomerData.CUSTOMER_LASTNAME));
        String username = getString(getColumnIndex(CustomerData.CUSTOMER_USERNAME));

        String password = getString(getColumnIndex(CustomerData.CUSTOMER_PASSWORD));

        String email = getString(getColumnIndex(CustomerData.CUSTOMER_EMAIL));

        return new CustomerData(firstname, lastname, username, password, email);
    }

    public int getCustomerID()
    {
        return getInt(getColumnIndex(CustomerData.CUSTOMER_ID_PK));
    }

}
