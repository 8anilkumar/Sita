package com.eminence.sitasrm.Adapters;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Activity.ProductDetails;
import com.eminence.sitasrm.Fragments.CartFragment;
import com.eminence.sitasrm.Interface.BadgingInterface;
import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.Models.ProductModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModel> subcat;
    String from;
    DatabaseHandler databaseHandler;
    BadgingInterface badgingInterface;
    ProgressBar progressBar;
    int lastposition = -1;


    public ProductListAdapter(ArrayList<ProductModel> subcat, Context context, BadgingInterface badgingInterface, String from) {
        this.subcat = subcat;
        this.context = context;
        this.from = from;
        this.badgingInterface = badgingInterface;
        setUpDB();

    }

    public ProductListAdapter(ArrayList<ProductModel> subcat, Context context, BadgingInterface badgingInterface, String from, ProgressBar progressBar) {
        this.subcat = subcat;
        this.context = context;
        this.from = from;
        this.badgingInterface = badgingInterface;
        this.progressBar = progressBar;
        setUpDB();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            holder.txt_mrp.setText("एमआरपी: ₹" + subcat.get(position).getPrice());
            holder.title.setText(subcat.get(position).getP_name_hindi() + " (" + subcat.get(position).getCaption_eng()+")");
            holder.Discription.setText(subcat.get(position).getSingle_description_hindi());
            holder.txt_packofPouch.setText(subcat.get(position).getPouch_quantity() + "\nपाउच \nका पैक");
        } else {
            holder.txt_mrp.setText("MRP: ₹" + subcat.get(position).getPrice());
            holder.title.setText(subcat.get(position).getProduct_name() + " (" + subcat.get(position).getCaption_eng()+")");
            holder.Discription.setText(subcat.get(position).getSingle_description_english());
            holder.txt_packofPouch.setText("Pack of" + "\n" + subcat.get(position).getPouch_quantity() + "\nPouches");
        }

        if (from.equalsIgnoreCase("cart")){
            holder.txt_plus.setVisibility(View.INVISIBLE);
            holder.txt_minus.setVisibility(View.INVISIBLE);
        }

        if (subcat.get(position).getCart_availability().equalsIgnoreCase("1")) {
            holder.inc_layout.setVisibility(View.GONE);
            holder.addedTOCartLayout.setVisibility(View.VISIBLE);
            holder.addlayout.setVisibility(View.GONE);
            holder.txt_itemCount.setText(subcat.get(position).getQuantity());
        }

        holder.product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equalsIgnoreCase("cart")) {

                } else {
                    if(subcat.size() != 0) {
                        String productId = subcat.get(position).getProduct_id()+"";
                        if(!productId.equalsIgnoreCase("") || !productId.equalsIgnoreCase("null")){
                            Intent intent=new Intent(context, ProductDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            intent.putExtra("productid", productId);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });

        Glide.with(context).load(imagebaseurl + subcat.get(position).getProduct_image())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading))
                .into(holder.product_image);


        holder.addlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.inc_layout.setVisibility(View.GONE);

                holder.addedTOCartLayout.setVisibility(View.VISIBLE);
                holder.addlayout.setVisibility(View.GONE);
                holder.txt_itemCount.setText("1");

                lastposition = position;
                notifyDataSetChanged();

                try {
                    addtocart(subcat.get(position).getProduct_id(),subcat.get(position).getPrice(),"1");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty = holder.txt_itemCount.getText().toString();
                String logintype = yourPrefrence.getData("logintype");

                int qty2 = Integer.parseInt(qty) + 1;
                YourPreference yourPrefrence = YourPreference.getInstance(context);

                if(logintype.equalsIgnoreCase("signup_retail")){
                    int maxLimit = Integer.parseInt(yourPrefrence.getData("retailerProductCountLimit"));
                    if(qty2 <= maxLimit){
                        holder.txt_itemCount.setText("" + qty2);
                        updateCart(subcat.get(position).getProduct_id(),""+qty2);
                    } else {
                        Toast.makeText(context, "Max product limit is "+maxLimit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int maxLimit = Integer.parseInt(yourPrefrence.getData("userProductCountLimit"));
                    if(qty2 <= maxLimit){
                        holder.txt_itemCount.setText("" + qty2);
                        updateCart(subcat.get(position).getProduct_id(),""+qty2);
                    } else {
                        Toast.makeText(context, "Max product limit is "+maxLimit, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty = holder.txt_itemCount.getText().toString();
                if (qty.equals("0")) {
                    holder.addlayout.setVisibility(View.VISIBLE);
                    holder.inc_layout.setVisibility(View.GONE);
                    holder.addedTOCartLayout.setVisibility(View.GONE);
                } else {
                    int qty2 = Integer.parseInt(qty) - 1;
                    holder.txt_itemCount.setText("" + qty2);

                    try {
                        updateCart(subcat.get(position).getProduct_id(),""+qty2);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    if (qty2 == 0) {
                        if (from.equalsIgnoreCase("cartFragment")) {
                            holder.addlayout.setVisibility(View.VISIBLE);
                            holder.inc_layout.setVisibility(View.GONE);
                            holder.addedTOCartLayout.setVisibility(View.GONE);

                            removecart(subcat.get(position).getProduct_id());
                            if (subcat.size() != 0) {
                                subcat.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, subcat.size());
                            }
                        } else {
                            removecart(subcat.get(position).getProduct_id());
                            holder.addlayout.setVisibility(View.VISIBLE);
                            holder.inc_layout.setVisibility(View.GONE);
                            holder.addedTOCartLayout.setVisibility(View.GONE);
                            databaseHandler.cartInterface().deletebyid(subcat.get(position).getProduct_id());
                        }
                    }
                }
            }
        });

        holder.goToCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("goto","cart");
                context.startActivity(intent);
            }
        });


        holder.proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment.proceedTOPay(context);
            }
        });

        if( lastposition == position ) {
            holder.proceedToCheckBackground.setVisibility(View.VISIBLE);
        } else {
            holder.proceedToCheckBackground.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, title, Discription, txt_itemCount, txt_minus, txt_plus, txt_mrp, txt_packofPouch;
        ImageView product_image;
        LinearLayout product_layout, addlayout, inc_layout, contentLayout, proceedToCheckBackground,addedTOCartLayout,goToCartLayout,proceedToPay;

        public MyViewHolder(View view) {
            super(view);
            product_image = view.findViewById(R.id.product_image);
            title = view.findViewById(R.id.title);
            Discription = view.findViewById(R.id.Discription);
            product_layout = view.findViewById(R.id.product_layout);
            addlayout = view.findViewById(R.id.addlayout);
            inc_layout = view.findViewById(R.id.inc_layout);
            txt_itemCount = view.findViewById(R.id.txt_itemCount);
            txt_minus = view.findViewById(R.id.txt_minus);
            txt_plus = view.findViewById(R.id.txt_plus);
            contentLayout = view.findViewById(R.id.contentLayout);
            txt_mrp = view.findViewById(R.id.txt_mrp);
            txt_packofPouch = view.findViewById(R.id.txt_packofPouch);
            proceedToCheckBackground = view.findViewById(R.id.proceedToCheckBackground);
            addedTOCartLayout = view.findViewById(R.id.addedTOCartLayout);
            goToCartLayout = view.findViewById(R.id.goToCartLayout);
            proceedToPay = view.findViewById(R.id.proceedToPay);

        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(context, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }


    private void addtocart(String product_id, String price, String qty) {

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "add_to_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
            params.put("product_id", product_id);
            params.put("qty",qty );
            params.put("price",price );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("My Cart Parameter",""+params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");

                    if (status_code.equalsIgnoreCase("1")) {
                        badgingInterface.badgecount();
                        if (from.equalsIgnoreCase("cartFragment")) {
                            CartFragment.getamount_from_adapter(context,progressBar);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", String.valueOf(error));
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }


    private void removecart(String product_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "remove_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
            params.put("product_id", product_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");

                    if (status_code.equalsIgnoreCase("1")) {
                        badgingInterface.badgecount();
                        if (from.equalsIgnoreCase("cartFragment")) {
                            CartFragment.getamount_from_adapter(context,progressBar);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", String.valueOf(error));
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }


    private void updateCart(String product_id,String qty) {

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "update_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
            params.put("product_id", product_id);
            params.put("quantity", qty);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("update_cart Parameter",""+params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        if (from.equalsIgnoreCase("cartFragment")) {
                            CartFragment.getamount_from_adapter(context,progressBar);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", String.valueOf(error));
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

}

