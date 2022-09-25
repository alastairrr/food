package com.food.app.data_class;

public class ItemData {
    public static final String TABLE_NAME = "ItemData"; // name

    public static final String ITEM_ID_PK = "itemID_PK";
    public static final String RESTAURANT_ID_FK = "restaurantID_FK";
    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_DESCRIPTION = "itemDescription";
    public static final String ITEM_PRICE = "itemPrice";
    public static final String IMAGE_REF = "imageRef";



    private String itemID_PK;
    private String restaurantID_FK;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private String imageRef;


    public ItemData(String itemID_PK, String restaurantID_FK, String itemName, String itemDescription, double itemPrice, String imageRef) {
        this.itemID_PK = itemID_PK;
        this.restaurantID_FK = restaurantID_FK;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.imageRef = imageRef;
    }

    public String getRestaurantID() {
        return restaurantID_FK;
    }

    public void setRestaurantID(String restaurantID_FK) { this.restaurantID_FK = restaurantID_FK; }

    public String getID() {
        return itemID_PK;
    }

    public void setID(String itemID_PK) {
        this.itemID_PK = itemID_PK;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return itemDescription;
    }

    public void setDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getPrice() {
        return itemPrice;
    }

    public void setPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }
}
