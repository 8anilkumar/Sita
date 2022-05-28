package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Fragments.CartFragment;
import com.eminence.sitasrm.MainActivity;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class ProductDetails extends AppCompatActivity {


    TextView title, Discription, discription_long, totalprize, pouchqty, txt_BuyNow,txt_amount,txt_packofPouch,titleMoney;
    ImageView product_imagee;
    String productid;
    DatabaseHandler databaseHandler;
    String price;
    List<CartResponse> alldata;
    String r_code;
    int productQuentity = 0;
    LinearLayout txtCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        intializeview();
        setUpDB();

        productid = getIntent().getExtras().getString("productid");

        if (Helper.INSTANCE.isNetworkAvailable(ProductDetails.this)) {
            getcart_availablity(productid);
            submit(productid);
            getCartList();
        } else {
            Helper.INSTANCE.Error(ProductDetails.this, getString(R.string.NOCONN));
        }

        txt_BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
                String logintype = yourPrefrence.getData("logintype");

                if(logintype.equalsIgnoreCase("signup_retail")){
                    int maxLimit = Integer.parseInt(yourPrefrence.getData("retailerProductCountLimit"));
                    if(productQuentity < maxLimit) {
                        addtocart(productid,price,"1");
                    } else {
                        Toast.makeText(ProductDetails.this, "Max product limit is "+maxLimit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int maxLimit = Integer.parseInt(yourPrefrence.getData("userProductCountLimit"));
                    if(productQuentity < maxLimit){
                        addtocart(productid,price,"1");
                    } else {
                        Toast.makeText(ProductDetails.this, "Max product limit is "+maxLimit, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, MainActivity.class);
                intent.putExtra("goto","cart");
                startActivity(intent);
            }
        });
    }

    private void intializeview() {
        title = findViewById(R.id.title);
        Discription = findViewById(R.id.Discription);
        discription_long = findViewById(R.id.discription_long);
        product_imagee = findViewById(R.id.product_image);
        txt_BuyNow = findViewById(R.id.txt_BuyNow);
        txt_amount = findViewById(R.id.txt_amount);
        txt_packofPouch = findViewById(R.id.txt_packofPouch);
        titleMoney = findViewById(R.id.titleMoney);
        txtCart = findViewById(R.id.txtCart);



    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(ProductDetails.this, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    public void submit(String productid) {
        String url = baseurl + "product_details";
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);
        Map<String, String> params = new HashMap();
        params.put("product_id", productid);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        String product_id = obj.getString("product_id");
                        String product_name = obj.getString("product_name");
                        String description = obj.getString("description");
                        String product_image = obj.getString("product_image");
                        String pouch_quantity = obj.getString("pouch_quantity");
                        price = obj.getString("price");
                        String p_name_hindi = obj.getString("product_name_hindi");
                        String description_hindi = obj.getString("description_hindi");
                        String caption_eng = obj.getString("caption_eng");
                        String caption_hindi = obj.getString("caption_hindi");

                        YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
                        String language = yourPrefrence.getData("language");

                        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
                            title.setText(p_name_hindi);
                            Discription.setText("न्यूनतम। युक्त 1 पैक का आदेश "+caption_hindi);
                            txt_amount.setText("MRP: \u20B9 "+price);
                            titleMoney.setText(caption_hindi);
                            discription_long.setText(description_hindi);
                            txt_packofPouch.setText(pouch_quantity+"\nपाउच \nका पैक");
                        } else {
                            title.setText(product_name);
                            titleMoney.setText(caption_eng);
                            Discription.setText("Min. Order of 1 pack containing "+caption_eng);
                            txt_amount.setText("MRP: \u20B9 "+price);
                            discription_long.setText(description);
                            txt_packofPouch.setText("Pack of"+"\n"+pouch_quantity+"\nPouches");
                        }

                        Glide.with(ProductDetails.this).load(imagebaseurl + product_image)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.loading))
                                .into(product_imagee);
                        totalprize.setText(price);
                        pouchqty.setText(pouch_quantity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void getcart_availablity(String productid) {
        String url = baseurl + "cart_check";
         YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
        String id = yourPrefrence.getData("id");

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);

        Map<String, String> params = new HashMap();
        params.put("user", id);
        params.put("product_id", productid);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                      r_code = obj.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    public void back(View view) {
        onBackPressed();
    }

    public void cart(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartList();
    }

    private void addtocart(String product_id, String price, String qty) {
        YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "add_to_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);
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
                        Intent intent = new Intent(ProductDetails.this, MainActivity.class);
                        intent.putExtra("goto", "cart");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //      Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("ABC", String.valueOf(error));
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    private void updateCart(String product_id,String qty) {
        YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "update_cart";
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);
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

                    } else {

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

    private void getCartList() {
        YourPreference yourPrefrence = YourPreference.getInstance(ProductDetails.this);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "cart_list";
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);
        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
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
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            int product_id = Integer.parseInt(jsonObject2.getString("product_id"));
                            int pId = Integer.parseInt(productid);
                            if (product_id == pId) {
                                productQuentity = Integer.parseInt(jsonObject2.getString("quantity"));
                            }
                        }

                    } else {

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