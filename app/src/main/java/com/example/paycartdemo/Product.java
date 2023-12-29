package com.example.paycartdemo;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Product")
public class Product extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public double getPrice() {
        return getDouble("price");
    }

    public void setPrice(double price) {
        put("price", price);
    }
    public int getQuantity() {
        return getInt("quantity");
    }

    public void setQuantity(int quantity) {
        put("quantity", quantity);
    }
    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(ParseFile image) {
        put("image", image);
    }
    public static ParseQuery<Product> getQuery() {
        return ParseQuery.getQuery(Product.class);
    }
}
