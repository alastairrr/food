package com.food.app;


import com.food.app.data_class.ItemData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;


public class ContentPreload {

    private static final ArrayList<RestaurantData> preloadedRestaurants = new ArrayList<>( Arrays.asList(
            new RestaurantData("00001", "McDonald's", "1.3 km  |  30 – 40 min", "pexels_ready_made_4021944"),
            new RestaurantData("00002", "Nobu (Crown)", "7.9 km  |  55 – 65 min", "pexels_rajesh_tp_2098143"),
            new RestaurantData("00003", "James Parker Sushi & Sake", "9.5 km  |  60 – 75 min", "sushi_set_on_stone_slate"),
            new RestaurantData("00004", "KFC", "0.7 km  |  25 – 30 min", "kfc_image"),
            new RestaurantData("00005", "Ciao Italia", "6.9 km  |  55 – 65 min", "pexels_eneida_nieves_905847"),
            new RestaurantData("00006", "Palsaik Korean BBQ", "10.9 km  |  70 – 80 min", "pexels_becerra_govea_photo_5773968")
    ));

    private static final ArrayList<ItemData> preloadedItems = new ArrayList<>( Arrays.asList(
            new ItemData("00011", "00001", "Small Burger", "Delicious beef burger", 4.5, "pexels_gonzalo_acuna_10922928"),
            new ItemData("00012", "00001", "Small Fries", "Hot Fries", 3.5, "pexelsnikitakrasnov6006652"),
            new ItemData("00013", "00001", "Small Coke", "Fizzy Cola", 2, "pexelsjonathanborba2983100"),
            new ItemData("00014", "00001", "Medium Burger", "Delicious beef burger", 5.5, "pexels_gonzalo_acuna_10922925"),
            new ItemData("00015", "00001", "Medium Fries", "Hot Fries", 4.5, "pexelsnikitakrasnov6006652"),
            new ItemData("00016", "00001", "Medium Coke", "Fizzy Cola", 3, "pexelsjonathanborba2983100"),
            new ItemData("00017", "00001", "Large Burger", "Delicious beef burger", 6.5, "pexels_gonzalo_acuna_10922926"),
            new ItemData("00018", "00001", "Large Fries", "Hot Fries", 5.5, "pexelsnikitakrasnov6006652"),
            new ItemData("00019", "00001", "Large Coke", "Fizzy Cola", 4, "pexelsjonathanborba2983100"),

            new ItemData("00021", "00002", "Sushi Set Premium", "Assorted Sushi", 30.6, "pexelshorizoncontent3763808"),
            new ItemData("00022", "00002", "Sushi Set Deluxe", "Assorted Sushi", 27.6, "pexelshorizoncontent3763816"),
            new ItemData("00023", "00002", "Sushi Set Regular", "Assorted Sushi", 25.6, "pexelsvaleriaboltneva1199987"),
            new ItemData("00024", "00002", "Teppenyaki Set", "Chicken Teppenyaki set with rice", 22.9, "pexelsvaleriaboltneva1860207"),
            new ItemData("00025", "00002", "Gyoza Set", "Gyoza bento set", 19.9, "pexelsvaleriaboltneva1860200"),
            new ItemData("00026", "00002", "Mixed Salad", "Mixed Salad with chicken", 13.9, "pexelscottonbro3297367"),
            new ItemData("00027", "00002", "Ramen Special", "Chef's special ramen", 10.9, "pexelsquanganhhanguyen884600"),

            new ItemData("00031", "00003", "Nigiri Set", "Nigiri set with sushi and wasabi", 34.5, "pexelsrajeshtp2098085"),
            new ItemData("00032", "00003", "Sashimi Deluxe Set", "Sashimi set with deluxe variety", 53.5, "pexels2871756"),
            new ItemData("00033", "00003", "Sashimi Special", "Tuna Sashimi with house sushi", 25.9, "pexelscottonbro3298180"),
            new ItemData("00034", "00003", "Salmon Sashimi", "Salmon Sashimi with salad", 20.9, "pexelsfotografjylland2995275"),
            new ItemData("00035", "00003", "Wagyu Sushi", "A5 Wagyu Beef Sushi", 30.5, "pexelsjhoon13345325"),
            new ItemData("00036", "00003", "House Special Sushi", "Assorted house special sushi", 7.9, "pexelsmelikebenli13233519"),

            new ItemData("00041", "00004", "Original 3 Piece", "3 Piece chicken deal", 11.9, "pexelskaichiehchan5652264"),
            new ItemData("00042", "00004", "Hot Chips", "Hot chips with chicken salt", 5.9, "pexelsalenashekhovtcova6941042"),
            new ItemData("00043", "00004", "Sprite", "Fizzy Sprite", 4.9, "pexelsalinafezarefi4161715"),
            new ItemData("00044", "00004", "Chicken Burger", "Original chicken deal medium", 13.9, "pexelsalleksana7497221"),
            new ItemData("00045", "00004", "Small Chicken Box", "Original chicken deal", 11.9, "pexelsgonzaloruiz12118977"),
            new ItemData("00046", "00004", "Large Chicken Box", "Original chicken deal large", 15.9, "pexelsgonzaloruiz12118979"),


            new ItemData("00051", "00005", "Margherita Pizza", "Neapolitan Margherita Pizza", 20, "pexelsvincentrivaud2147491"),
            new ItemData("00052", "00005", "Meat Lovers Pizza", "Pizza with italian meat", 20, "pexelsbrettjordan825661"),
            new ItemData("00053", "00005", "Original Pizza", "Chicken and olives pizza", 20, "pexelspixabay315755"),
            new ItemData("00054", "00005", "Pepperoni Pizza", "Pepperoni on pizza", 20, "pexelspolinatankilevitch4109083"),
            new ItemData("00055", "00005", "Bolognese Penne", "Penne with bolognese sauce", 16, "pexelsenginakyurt1437267"),
            new ItemData("00056", "00005", "Chicken Carbonara", "Original carbonara with chicken", 17, "pexelsenginakyurt1438672"),
            new ItemData("00057", "00005", "Seafood Pasta", "Original seafood pasta", 19, "pexelscottonbro4253128"),
            new ItemData("00058", "00005", "Shrimp Pasta", "Shrimp Aglio e Olio", 23, "pexelsanthonyleong2092906"),

            new ItemData("00061", "00006", "Bimbibap", "Rice with meat, egg and vegetables", 16, "bibimbap"),
            new ItemData("00062", "00006", "Jajangmyeon", "Black bean chinese noodles", 15.9, "koreanfoodjajangmyeonnoodlewithfermentedblackbeanssauce"),
            new ItemData("00063", "00006", "Grilled Beef", "Grilled wagyu beef", 30, "pexelsvaleriaboltneva1251208"),
            new ItemData("00064", "00006", "Bulgolgi Pork", "Spicy bulgolgi pork slice", 35, "pexelsbecerragoveaphoto5774151"),
            new ItemData("00065", "00006", "Marinated Beef", "Beef marinated in special sauce", 44, "pexelsbecerragoveaphoto5774093")

    ));

    public static void getPreloadContent(DatabaseManager databaseManager) {

        for ( RestaurantData restaurantData : preloadedRestaurants ) {
            databaseManager.add_Restaurant(restaurantData);
        }

        for ( ItemData itemData : preloadedItems ) {
            databaseManager.add_Item(itemData);
        }
    }

}