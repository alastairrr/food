package com.food.app.data_class;

public class RestaurantData {
    public static final String TABLE_NAME = "RestaurantData"; // restaurantName
    public static final String RESTAURANT_ID_PK = "restaurantID_PK";
    public static final String RESTAURANT_NAME = "restaurantName";
    public static final String RESTAURANT_DESC = "restaurantDesc";
    public static final String IMAGE_REF = "imageRef";

    private String restaurantID_PK;
    private String restaurantName;
    private String restaurantDesc;
    private String imageRef;



    public RestaurantData(String restaurantID_PK, String restaurantName, String restaurantDesc, String imageRef) {
        this.restaurantID_PK = restaurantID_PK;
        this.restaurantName = restaurantName;
        this.restaurantDesc = restaurantDesc;
        this.imageRef = imageRef;
    }

    public String getRestaurantID() {
        return restaurantID_PK;
    }

    public void setRestaurantID(String restaurantID_PK) {
        this.restaurantID_PK = restaurantID_PK;
    }

    public String getName() {
        return restaurantName;
    }

    public void setName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return restaurantDesc;
    }

    public void setDescription(String restaurantDesc) {
        this.restaurantDesc = restaurantDesc;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }
}
