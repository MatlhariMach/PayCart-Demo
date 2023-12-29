package com.example.paycartdemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.widget.ParseImageView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.PostHolder> {

    private final List<Product> Prolist;
    private final Context context;


    public ProductAdapter(List<Product> Prolist, Context context) {
        this.Prolist = Prolist;
        this.context = context;
    }
    @NonNull
    @Override
    public ProductAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
      //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testlayout, parent, false);
        return new PostHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.PostHolder holder, int position) {
        Product product = Prolist.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(Double.toString(product.getPrice()));
        holder.productImage.setParseFile(product.getImage());
        holder.productDescription.setText(product.getDescription());
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("products", product);
                query.findInBackground(new FindCallback<Order>() {
                    @Override
                    public void done(List<Order> orders, ParseException e) {

                        if (e == null) {
                            if (!orders.isEmpty()) {
                                // User has an existing order, show toast
                                Toast.makeText(view.getContext(), "You already added to cart", Toast.LENGTH_LONG).show();
                            } else {
                                // No existing order for the user, create a new one
                                Order shoppingCart = new Order();
                                List<Product> cartItems = new ArrayList<>();
                                cartItems.add(product);
                                shoppingCart.setUser(ParseUser.getCurrentUser());
                                shoppingCart.setName(product.getName());
                                shoppingCart.setProducts(cartItems);
                                shoppingCart.setTotalPrice(product.getPrice());
                                shoppingCart.setImage(product.getImage());

                                shoppingCart.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(view.getContext(), "Successfully saved to the shopping cart", Toast.LENGTH_LONG).show();
                                        } else {
                                            // Handle the error while saving
                                        }
                                    }
                                });
                            }
                        } else {
                            // Handle query errors
                        }
                    }
                });

            }
        });

        ParseFile file = product.getImage();;
        if (file != null){

            holder.productImage.setParseFile(file);
            holder.productImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        // Image loaded successfully
                    } else {
                        // Handle error while loading image
                        e.printStackTrace();
                    }
                }

            });
            holder.productImage.setVisibility(TextView.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return Prolist.size();
    }
    static class PostHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice,productDescription;
        public Button addToCartButton;
        public ParseImageView productImage;

        public PostHolder(@NonNull View view) {
            super(view);
            productName =view.findViewById(R.id.productName);

            productPrice =view.findViewById(R.id.productPrice);

            productImage = view.findViewById(R.id.productImage);

            addToCartButton = view.findViewById(R.id.addToCartButton);

            productDescription = view.findViewById(R.id.productDescription);

        }
    }
}
