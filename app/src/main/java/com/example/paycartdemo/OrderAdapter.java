package com.example.paycartdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.ui.widget.ParseImageView;

import java.util.Arrays;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.PostHolder>{

    private final List<Order> Ordlist;
    private final Context context;
    private int[] prices;
    private double totalPriceSum = 0;
    private TotalPriceListener totalPriceListener;

    public OrderAdapter(List<Order> Ordlist,Context context, TotalPriceListener totalPriceListener) {
        this.Ordlist = Ordlist;
        this.context = context;
        this.prices = new int[Ordlist.size()];
        Arrays.fill(prices, 1);
        this.totalPriceListener = totalPriceListener;
        calculateTotalPriceSum();

    }
    @NonNull
    @Override
    public OrderAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tchart, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item, parent, false);
        return new OrderAdapter.PostHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.PostHolder holder, @SuppressLint("RecyclerView") int position) {
        Order product = Ordlist.get(position);

        holder.Description.setText(product.getName());
        holder.Price.setText(Double.toString(product.getTotalPrice()));
        holder.addedprice.setText(Integer.toString(prices[position]));
        totalPriceListener.onTotalPriceUpdated(totalPriceSum);
        holder.productImage.setParseFile(product.getImage());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(product, position);
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /*    int currentQuantity = prices[position] - 1;
                if (currentQuantity >= 0) {
                    prices[position] = currentQuantity;
                    holder.addedprice.setText(Integer.toString(prices[position]));

                    double newTotalPrice = prices[position] * product.getTotalPrice();
                    holder.Price.setText(Double.toString(newTotalPrice));

                    updateBack4AppQuantity(product, currentQuantity); // Update quantity in Back4App

                    calculateTotalPriceSum();
                    totalPriceListener.onTotalPriceUpdated(totalPriceSum);
                } */

               if (prices[position] <= 0) {
                    // holder.addedprice.setText("0"); // Or any other default value you want to set
                    showDeleteConfirmationDialog(product, position);
                } else {

                   int currentQuantity = prices[position] - 1;
                   if (currentQuantity >= 0) {
                       prices[position] = currentQuantity;
                       holder.addedprice.setText(Integer.toString(prices[position]));

                       double newTotalPrice = prices[position] * product.getTotalPrice();
                       holder.Price.setText(Double.toString(newTotalPrice));

                       updateBack4AppQuantity(product, currentQuantity); // Update quantity in Back4App

                       calculateTotalPriceSum();
                       totalPriceListener.onTotalPriceUpdated(totalPriceSum);
                   }
                  /*  prices[position]--;
                    holder.addedprice.setText(Integer.toString(prices[position]));

                    double newTotalPrice = prices[position] * product.getTotalPrice();
                    holder.Price.setText(Double.toString(newTotalPrice));

                    calculateTotalPriceSum();
                    // Notify the listener about the change
                    totalPriceListener.onTotalPriceUpdated(totalPriceSum); */

                }


            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  prices[position]++;
                holder.addedprice.setText(Integer.toString(prices[position]));

                double newTotalPrice = prices[position] * product.getTotalPrice();
                holder.Price.setText(Double.toString(newTotalPrice));

                calculateTotalPriceSum();
                // Notify the listener about the change
                totalPriceListener.onTotalPriceUpdated(totalPriceSum); */

                int currentQuantity = prices[position] + 1;
                if (currentQuantity >= 0) {
                    prices[position] = currentQuantity;
                    holder.addedprice.setText(Integer.toString(prices[position]));

                    double newTotalPrice = prices[position] * product.getTotalPrice();
                    holder.Price.setText(Double.toString(newTotalPrice));

                    updateBack4AppQuantity(product, currentQuantity); // Update quantity in Back4App

                    calculateTotalPriceSum();
                    totalPriceListener.onTotalPriceUpdated(totalPriceSum);
                }


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
    private void showDeleteConfirmationDialog(final Order order, final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Order");
        builder.setMessage("Are you sure you want to delete this Order?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete the post
                order.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Post deleted successfully, remove it from the RecyclerView
                            order.remove(String.valueOf(position));
                            notifyDataSetChanged();
                        } else {
                            // Handle the error
                            // You can show an error message or perform other error handling
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
    private void calculateTotalPriceSum() {
        totalPriceSum = 0;
        for (int i = 0; i < Ordlist.size(); i++) {
            totalPriceSum += prices[i] * Ordlist.get(i).getTotalPrice();
        }
    }

    @Override
    public int getItemCount() {
        return Ordlist.size();
    }
    static class PostHolder extends RecyclerView.ViewHolder{

        public TextView Description, Price,addedprice;
        public ImageButton minus, plus ,delete;
        public ParseImageView productImage;
        public PostHolder(@NonNull View view){

            super(view);

            Description =view.findViewById(R.id.Description);
            Price =view.findViewById(R.id.Price);
            addedprice=view.findViewById(R.id.addedprice);
            minus =view.findViewById(R.id.minus);
            plus =view.findViewById(R.id.plus);
            delete =view.findViewById(R.id.Delete);
            productImage = view.findViewById(R.id.productImage);
        }
    }
    public interface TotalPriceListener {
        void onTotalPriceUpdated(double totalPriceSum);
    }
    public void addOrder(Order order) {
        Ordlist.add(order);
        notifyItemInserted(Ordlist.size() - 1);
    }
    public Order getOrderAtPosition(int position) {
        if (Ordlist != null && position >= 0 && position < Ordlist.size()) {
            return Ordlist.get(position);
        }
        return null; // Return null or handle the case when the position is out of bounds
    }
    private void updateBack4AppQuantity(Order order, int quantity) {
        // Perform the necessary update in your Back4App database

        // Use order.getObjectId() to identify the order and update its quantity to 'quantity'

        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.getInBackground(order.getObjectId(), new GetCallback<Order>() {
            @Override
            public void done(Order fetchedOrder, ParseException e) {
                if (e == null) {
                    // Update the quantity of the fetched order
                    fetchedOrder.setQuantity(quantity);
                    fetchedOrder.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Quantity updated successfully
                            } else {
                                // Handle error while updating quantity
                            }
                        }
                    });
                } else {
                    // Handle error fetching the order
                }
            }
        });
    }

}
