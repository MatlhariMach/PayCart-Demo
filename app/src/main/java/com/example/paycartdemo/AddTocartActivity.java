package com.example.paycartdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class AddTocartActivity extends AppCompatActivity implements OrderAdapter.TotalPriceListener{
    private RecyclerView recyclerView;
    public TextView totalsum;

    private static final int REQUEST_CODE_PAYPAL_PAYMENT = 1;
    private static final String YOUR_PAYPAL_CLIENT_ID = "Your ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tocart);


        // Forcefully enable the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

                Spannable text = new SpannableString(actionBar.getTitle());
                text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                getSupportActionBar().setTitle(text);


            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));
            actionBar.setDisplayHomeAsUpEnabled(true);

            ;

        }
        recyclerView =  findViewById(R.id.recyclerView);
        totalsum  = findViewById(R.id.priceTextView);

        // Enable the action bar


        Button Procced  = findViewById(R.id.Procced);
        Procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPayPalPayment();

            }
        });
        loadproducts();
    }
    public void loadproducts(){

        // Query the user's shopping cart from Parse.com
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
       // query.whereEqualTo("completed", false);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> shoppingCarts, ParseException e) {
                if (e == null && !shoppingCarts.isEmpty()) {

                    //  List<Product> cartItems = shoppingCarts.get(0).getProducts();

                    loadObjects(shoppingCarts);

                } else {
                    // Handle the error or indicate that the cart is empty
                }
            }
        });

    }
    private void loadObjects(List<Order> list) {

        if (list == null || list.isEmpty()) {
            //     empty_text.setVisibility(View.VISIBLE);
            return;
        }
        //   empty_text.setVisibility(View.GONE);
        int numberOfColumns = 2; // Change this as needed


        OrderAdapter adapter = new OrderAdapter(list,this,this);
        //  RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), numberOfColumns);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onTotalPriceUpdated(double totalPriceSum) {
        // Update your TextView with the new total sum
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String roundedTotal = decimalFormat.format(totalPriceSum);
        totalsum.setText(roundedTotal);
        // totalsum.setText(String.valueOf(totalPriceSum));
    }
    private void launchPayPalPayment() {

        // Initialize PayPal configuration
        PayPalConfiguration config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Use sandbox for testing
                .clientId(YOUR_PAYPAL_CLIENT_ID);


        String amountString = totalsum.getText().toString();

        String amountfull = amountString.replace(",", ".");
      // Parse the string to a BigDecimal
        BigDecimal amount = new BigDecimal(amountfull);


// Set up the PayPal instance
        //  PayPalPayment payment = new PayPalPayment(new BigDecimal("10.00"), "USD", "Order", PayPalPayment.PAYMENT_INTENT_SALE);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "Order", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_CODE_PAYPAL_PAYMENT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // Payment successful, insert order data into DeliveryInformation class
                insertOrderData();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Payment cancelled by user
                // Handle accordingly
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Invalid payment or configuration
                // Handle accordingly
            }
        }
    }
    private void insertOrderData() {

        // Query for orders belonging to the current user with completed status true
         ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
          query.whereEqualTo("user", ParseUser.getCurrentUser()); //
          query.whereEqualTo("completed", true);
        query.findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> completedOrders, ParseException e) {
                if (e == null) {
                    // Create a new DeliveryInformation object
                    DeliveryInformation deliveryInformation = new DeliveryInformation();

                    // Set order details
                    deliveryInformation.setOrders(completedOrders);
                    deliveryInformation.setDeliveryAddress(ParseUser.getCurrentUser().getString("Address"));
                    deliveryInformation.setcustomernum(ParseUser.getCurrentUser().getString("PhoneNumber"));
                     deliveryInformation.setDeliveryStatus("Pending");
                     deliveryInformation.setDeliveryPerson("Ron");
                     deliveryInformation.setEstimatedDeliveryTime("3 days");
                    // Set other delivery information

                    // Save the DeliveryInformation object to the backend
                    deliveryInformation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Order data inserted successfully
                                // Handle success
                                Toast.makeText(AddTocartActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                // Error occurred while inserting order data
                                // Handle error
                            }
                        }
                    });
                } else {
                    // Error occurred while fetching orders
                    // Handle the error
                }
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_tocart, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel){
            Intent intent = new Intent(AddTocartActivity .this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}