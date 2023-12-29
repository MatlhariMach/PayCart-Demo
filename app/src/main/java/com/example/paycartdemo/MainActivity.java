package com.example.paycartdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView empty_text;
    TextView cartCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerView =  findViewById(R.id.recyclerView);
        empty_text = findViewById(R.id.empty_text);

        cartCount = findViewById(R.id.cartcount);

        ImageView cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddTocartActivity.class);
                startActivity(intent);
            }
        });
        countitems();
        loadproducts();
    }


    public void loadproducts(){

        ParseQuery<Product> query = Product.getQuery();
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            //   progressDialog.dismiss();
            if (e == null) {
                //We are initializing
                loadObjects(objects);
            } else {
                Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
                //      showAlert("Error", e.getMessage());
            }
        });
    }
    private void loadObjects(List<Product> list) {

        if (list == null || list.isEmpty()) {
         //   empty_text.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.VISIBLE); // Show the empty_text TextView
            empty_text.setText("No products found");
            return;
        }
    //    empty_text.setVisibility(View.GONE);
        int numberOfColumns = 2; // Change this as needed


        ProductAdapter adapter = new ProductAdapter(list,this);
     //   RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), numberOfColumns);
           RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
    public void countitems(){
        // Query to find the count of items in the Order class for the current user
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    String itemCount = String.valueOf(count);

                    // Ensure that cartCount is not null
                    if (cartCount != null) {

                        if (count > 0){
                            // Set the count to the TextView
                            cartCount.setBackgroundResource(R.drawable.badge_shape2);
                            cartCount.setText(itemCount);
                        }

                    }
                    //  cartCount.setText(count);
                } else {
                    // Handle any errors that occurred during the count query
                    // Log.e("ItemCount", "Error: " + e.getMessage());
                }
            }
        });

    }

}