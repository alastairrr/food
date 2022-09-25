package com.food.app.data_class;

import java.util.HashMap;

public class OrderData {
    public static final String TABLE_NAME = "OrderData"; // name


    // ideas -
    // use random(), regenerate until key can't be found - possible O(n^2)
    // increment +1 per order_id_pk, call sqlite max to find biggest id. O(n)
    //

    public static final String ORDER_ID_PK = "orderID_PK";
    public static final String ORDER_DATE = "orderDate";
    public static final String CUSTOMER_ID_FK = "customerID_FK";
    public static final String ITEM_ID_FK = "itemID_FK";
    public static final String ITEM_QUANTITY = "itemQuantity";

    private int orderID_PK;
    private String orderDate;
    private int customerID_FK;
    private String itemID_FK;
    private int itemQuantity;

    public HashMap<ItemData, Integer> cart = new HashMap<>();



    public OrderData(int orderID_PK, String orderDate, int customerID_FK, String itemID_FK, int itemQuantity) {
        this.orderID_PK = orderID_PK;
        this.orderDate = orderDate;
        this.customerID_FK = customerID_FK;
        this.itemID_FK = itemID_FK;
        this.itemQuantity = itemQuantity;
    }
    public int getOrderId_PK() {
        return orderID_PK;
    }

    public void setOrderID_PK(int orderID_PK) {
        this.orderID_PK = orderID_PK;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerIdFk() {
        return customerID_FK;
    }

    public void setCustomerIdFk(int customerID_FK) {
        this.customerID_FK = customerID_FK;
    }


    public String getItemID_FK() {
        return itemID_FK;
    }

    public void setItemID_FK(String itemID_FK) {
        this.itemID_FK = itemID_FK;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public HashMap<ItemData, Integer> getCart() {
        return cart;
    }
}

