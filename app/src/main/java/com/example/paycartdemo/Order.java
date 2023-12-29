package com.example.paycartdemo;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Order")
public class Order extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }
    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public void setProducts(List<Product> products) {
        put("products", products);
    }

    public List<Product> getProducts() {
        return getList("products");
    }

    public double getTotalPrice() {
        return getDouble("totalPrice");
    }

    public void setTotalPrice(double totalPrice) {
        put("totalPrice", totalPrice);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String status) {
        put("status", status);
    }

    // Add pointers to User, Products, and DeliveryInformation classes
    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(ParseFile image) {
        put("image", image);
    }
    public DeliveryInformation getDeliveryInformation() {
        return (DeliveryInformation) getParseObject("deliveryInformation");
    }

    public int getQuantity() {
        return getInt("quantity");
    }

    public void setQuantity(int quantity) {
        put("quantity", quantity);
    }
    public void setDeliveryInformation(DeliveryInformation deliveryInformation) {
        put("deliveryInformation", deliveryInformation);
    }
    public static ParseQuery<Order> getQuery() {
        return ParseQuery.getQuery(Order.class);
    }
}
