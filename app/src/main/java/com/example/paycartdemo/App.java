package com.example.paycartdemo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

public class App  extends Application {

    public App() {
    }
    @Override
    public void onCreate() {
        super.onCreate();

        ///ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Product.class);
        ParseObject.registerSubclass(Order.class);
        ParseObject.registerSubclass(DeliveryInformation.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        ParseACL defauld = new ParseACL();
        ParseACL.setDefaultACL(defauld,true);


    }
}
