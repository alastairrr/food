package com.food.app;



/* ********************************************************* *
 * </ IMPORT />  ====  FILE DEPENDENCIES  ====  </ IMPORT /> *
 * ********************************************************* */

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import com.food.app.data_class.ItemData;
import com.food.app.data_class.OrderData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;
import com.food.app.fragments.FragmentAccount;
import com.food.app.fragments.FragmentCarts;
import com.food.app.fragments.FragmentHome;
import com.food.app.fragments.FragmentSearch;
import com.food.app.fragments.fragments_dialog.FragmentAuthDialog;
import com.food.app.fragments.fragments_dialog.FragmentCartDialog;
import com.food.app.fragments.fragments_dialog.FragmentOrderDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Collections;
import java.util.Random;



/* ************************************************************* *
 * </ BEGIN />  ====  APPLICATION ENTRY POINT  ====  </ BEGIN /> *
 * ************************************************************* */

public class MainActivity extends AppCompatActivity {



    /* ***************************************************************** *
     * </ REFERENCE />  ====  ACTIVITY REFERENCES  ====  </ REFERENCE /> *
     * ***************************************************************** */

    //  ==== OBJECT REFERENCES ==== //

    // SELECTED FRAGMENT REFERENCE - Current selected fragment, initialized to Home.
    public static Fragment currSelectedFragment;

    // DATABASE MANAGER - Database Content Manager
    DatabaseManager dbManager;


    //  ====  DEBOUNCE FIELDS  ====  //

    // BOOLEAN ORDER DIALOG DEBOUNCE - Prevents additional showOrderDialog events from firing when dialog is launched
    private static Boolean orderDeBounce = false;

    // BOOLEAN ORDER DIALOG DEBOUNCE - Prevents additional requireLoginAuth events from firing when dialog is launched
    private static Boolean loginDeBounce = false;

    // BOOLEAN CART DIALOG DEBOUNCE - Prevents additional showCartDialog events from firing when dialog is launched
    private static Boolean cartDeBounce = false;



    /* ******************************************************* *
     * </ EVENTS />  ====  ACTIVITY EVENTS  ====  </ EVENTS /> *
     * ******************************************************* */

    //  ====  ON CREATE EVENT  ====  //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = DatabaseManager.initializeDatabase(this);
        ContentPreload.getPreloadContent(dbManager);

        currSelectedFragment = new FragmentHome(dbManager);
        NavigationBarView nav_bottom = findViewById(R.id.nav_bar_bottom_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, currSelectedFragment).commit();

            Random rand = new Random();

            for (ItemData itemData : dbManager.getItemData()){
                int randInt = rand.nextInt(100);
                if (randInt < 35 ) {
                    FragmentHome.recyclerData.add(itemData);
                    Collections.shuffle(FragmentHome.recyclerData);
                }
            }

        }

        //  ====  MENU NAVIGATION  ====  //
        nav_bottom.setOnItemSelectedListener(item -> {

            int itemID = item.getItemId();

            if ( itemID == R.id.nav_home ) {
                currSelectedFragment = new FragmentHome(dbManager);
            }
            else if ( itemID == R.id.nav_search ) {
                currSelectedFragment = new FragmentSearch(dbManager);
            }
            else if ( itemID == R.id.nav_carts ) {
                currSelectedFragment = new FragmentCarts(dbManager);
            }
            else if ( itemID == R.id.nav_account ) {
                currSelectedFragment = new FragmentAccount(dbManager);
            }
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, currSelectedFragment).commit();
            }
            return true;
        });
    }


    /* ********************************************************** *
     * </ METHODS />  ====  ACTIVITY METHODS  ====  </ METHODS /> *
     * ********************************************************** */

    //  ====  ORDER DIALOG REQUESTS  ====  //

    public void showOrderDialog(RestaurantData restaurantData, ItemData itemClicked) {
        if (!orderDeBounce) {
            orderDeBounce = true;
            FragmentOrderDialog.display(getSupportFragmentManager(), restaurantData, itemClicked, dbManager);
        }
    }

    public void showOrderDialog(RestaurantData restaurantData) {
        if (!orderDeBounce) {
            orderDeBounce = true;

            FragmentOrderDialog.display(getSupportFragmentManager(),restaurantData, dbManager);
        }
    }


    //  ====  LOGIN DIALOG REQUESTS  ====  //

    public void requireLoginAuth() {
        if (!loginDeBounce) {
            loginDeBounce = true;
            FragmentAuthDialog.display(getSupportFragmentManager(), dbManager);
        }
    }


    //  ====  LOGIN DIALOG REQUESTS  ====  //

    public void showCartDialog(OrderData orderData) {
        if (!cartDeBounce) {
            cartDeBounce = true;
            FragmentCartDialog.display(getSupportFragmentManager(), orderData, dbManager);
        }
    }


    //  ====  ORDER DIALOG DEBOUNCE RESET  ====  //

    public static void resetOrderDebounce() {
        orderDeBounce = false;
    }


    //  ====  LOGIN DIALOG DEBOUNCE RESET  ====  //

    public static void resetLoginDebounce() {
        loginDeBounce = false;
    }

    public static void resetCartDebounce() {
        cartDeBounce = false;
    }

}