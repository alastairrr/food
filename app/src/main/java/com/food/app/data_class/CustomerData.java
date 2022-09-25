package com.food.app.data_class;

public class CustomerData {
    public static final String TABLE_NAME = "CustomerData"; // name

    public static final String CUSTOMER_ID_PK = "customer_ID_PK";
    public static final String CUSTOMER_FIRSTNAME = "customerFirstName";
    public static final String CUSTOMER_LASTNAME = "customerLastName";
    public static final String CUSTOMER_USERNAME = "customerUsername";
    public static final String CUSTOMER_PASSWORD = "customerPassword";
    public static final String CUSTOMER_EMAIL = "customerEmail";

    private String customerFirstName;
    private String customerLastName;
    private String customerUsername;
    private String customerPassword;
    private String customerEmail;


    public CustomerData(String customerFirstName, String customerLastName, String customerUsername, String customerPassword, String customerEmail) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerUsername = customerUsername;
        this.customerPassword = customerPassword;
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstname() {
        return customerFirstName;
    }

    public void setCustomerFirstname(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastname() {
        return customerLastName;
    }

    public void setCustomerLastname(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
