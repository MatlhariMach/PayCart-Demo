package com.example.paycartdemo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("DeliveryInformation")
public class DeliveryInformation extends ParseObject {
    // Add a pointer to Order (one-to-one relationship)
    public void setOrders(List<Order> Orders) {
        put("Orders", Orders);
    }

    public List<Order> geOrders() {
        return getList("Orders");
    }

    public String getDeliveryAddress() {
        return getString("deliveryAddress");
    }

    public void setDeliveryAddress(String deliveryAddress) {
        put("deliveryAddress", deliveryAddress);
    }

    public String getDeliveryStatus() {
        return getString("deliveryStatus");
    }

    public void setDeliveryStatus(String deliveryStatus) {
        put("deliveryStatus", deliveryStatus);
    }

    public String getDeliveryPerson() {
        return getString("deliveryPerson");
    }
    public void setcustomernum(String deliveryStatus) {
        put("Contacts", deliveryStatus);
    }

    public String getcustomernum() {
        return getString("Contacts");
    }
    public void setDeliveryPerson(String deliveryPerson) {
        put("deliveryPerson", deliveryPerson);
    }

    public String getEstimatedDeliveryTime() {
        return getString("estimatedDeliveryTime");
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        put("estimatedDeliveryTime", estimatedDeliveryTime);
    }

}
