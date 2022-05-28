package com.eminence.sitasrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.RewardPointActivity;
import com.eminence.sitasrm.Fragments.CartFragment;
import com.eminence.sitasrm.Fragments.HomeFragment;
import com.eminence.sitasrm.Fragments.MyOrdersFragment;
import com.eminence.sitasrm.Fragments.ProfileFragment;
import com.eminence.sitasrm.Fragments.TransactionsFragment;
import com.eminence.sitasrm.Models.CartModel;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;
import com.razorpay.PaymentResultListener;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    boolean doubleBackToExitPressedOnce = false;
    boolean isHome = true;
    LinearLayout profile_Layout, transaction_Layout, myOrder_Layout, cart_Layout;
    RelativeLayout home_img_Layout;
    CardView cart_homeLayout;
    ImageView img_profile, img_transaction, img_myOrder, img_cart;
    TextView txt_profile,txt_transaction,txt_myOrder,txt_cart;
    static TextView lead_badge;
    DatabaseHandler databaseHandler;
    View footer;
    String flag = "";

    UpdateManager mUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_Layout = findViewById(R.id.profile_Layout);
        transaction_Layout = findViewById(R.id.transaction_Layout);
        myOrder_Layout = findViewById(R.id.myOrder_Layout);
        cart_Layout = findViewById(R.id.cart_Layout);
        cart_homeLayout = findViewById(R.id.cart_homeLayout);
        home_img_Layout = findViewById(R.id.home_img_Layout);
        img_profile = findViewById(R.id.img_profile);
        img_transaction = findViewById(R.id.img_transaction);
        img_myOrder = findViewById(R.id.img_myOrder);
        img_cart = findViewById(R.id.img_cart);
        txt_profile = findViewById(R.id.txt_profile);
        txt_transaction = findViewById(R.id.txt_transaction);
        txt_myOrder = findViewById(R.id.txt_myOrder);
        txt_cart = findViewById(R.id.txt_cart);
        footer = findViewById(R.id.footer);
        lead_badge = footer.findViewById(R.id.lead_badge);
        getamount_from_adapter(MainActivity.this);

        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE);
        mUpdateManager.start();

        img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
        img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
        home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.home_background));
        img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
        img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

        txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
        txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
        txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
        txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

        try {
            String type = getIntent().getExtras().getString("type");
            //  Welcomedialog();
            if ((type.equalsIgnoreCase("signup")) || (type.equalsIgnoreCase("signup_retail"))) {
                  welcomeDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();
        String go_to = intent.getStringExtra("goto");

        if (go_to.equalsIgnoreCase("cart")) {
            flag  = "1";

            img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
            img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
            home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
            img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
            img_cart.setColorFilter(getResources().getColor(R.color.theme));

            txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
            txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
            txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
            txt_cart.setTextColor(getResources().getColor(R.color.theme));

            Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);

            txt_profile.setTypeface(profile);
            txt_transaction.setTypeface(transaction);
            txt_myOrder.setTypeface(orders);
            txt_cart.setTypeface(cart);

            isHome = false;
            Fragment fragment = new CartFragment();
            changeFragment(fragment, false);

        }
        else if (go_to.equalsIgnoreCase("wallet")) {
            img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
            img_transaction.setColorFilter(getResources().getColor(R.color.theme));
            home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
            img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
            img_cart.setColorFilter(Color.argb(255, 158, 157, 157));
            txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
            txt_transaction.setTextColor(getResources().getColor(R.color.theme));
            txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
            txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

            Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);
            Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

            txt_profile.setTypeface(profile);
            txt_transaction.setTypeface(transaction);
            txt_myOrder.setTypeface(orders);
            txt_cart.setTypeface(cart);

            isHome = false;
            Fragment fragment = new TransactionsFragment();
            changeFragment(fragment, false);
        }
        else if (go_to.equalsIgnoreCase("myorder")) {
            img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
            img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
            home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
            img_myOrder.setColorFilter(getResources().getColor(R.color.theme));
            img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

            txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
            txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
            txt_myOrder.setTextColor(getResources().getColor(R.color.theme));
            txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

            Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
            Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);
            Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

            txt_profile.setTypeface(profile);
            txt_transaction.setTypeface(transaction);
            txt_myOrder.setTypeface(orders);
            txt_cart.setTypeface(cart);

            isHome = false;
            Fragment fragment = new MyOrdersFragment();
            changeFragment(fragment, false);
        }
        else { Fragment fragment = new HomeFragment();
            changeFragment(fragment, false);
        }

        setUpDB();

        profile_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    img_profile.setColorFilter(getResources().getColor(R.color.theme));
                    img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
                    home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
                    img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

                    txt_profile.setTextColor(getResources().getColor(R.color.theme));
                    txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

                    Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);
                    Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

                    txt_profile.setTypeface(profile);
                    txt_transaction.setTypeface(transaction);
                    txt_myOrder.setTypeface(orders);
                    txt_cart.setTypeface(cart);

                    isHome = false;
                    Fragment fragment = new ProfileFragment();
                    changeFragment(fragment, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        transaction_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   try {
                    img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_transaction.setColorFilter(getResources().getColor(R.color.theme));
                    home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
                    img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

                    txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_transaction.setTextColor(getResources().getColor(R.color.theme));
                    txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

                    Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);
                    Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

                    txt_profile.setTypeface(profile);
                    txt_transaction.setTypeface(transaction);
                    txt_myOrder.setTypeface(orders);
                    txt_cart.setTypeface(cart);

                    isHome = false;
                    Fragment fragment = new TransactionsFragment();
                    changeFragment(fragment, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

         myOrder_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
                    home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
                    img_myOrder.setColorFilter(getResources().getColor(R.color.theme));
                    img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

                    txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_myOrder.setTextColor(getResources().getColor(R.color.theme));
                    txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

                    Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);
                    Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

                    txt_profile.setTypeface(profile);
                    txt_transaction.setTypeface(transaction);
                    txt_myOrder.setTypeface(orders);
                    txt_cart.setTypeface(cart);

                    isHome = false;
                    Fragment fragment = new MyOrdersFragment();
                    changeFragment(fragment, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cart_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
                    home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.ic_home_inactive));
                    img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_cart.setColorFilter(getResources().getColor(R.color.theme));

                    txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_cart.setTextColor(getResources().getColor(R.color.theme));

                    Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_extrabold);

                    txt_profile.setTypeface(profile);
                    txt_transaction.setTypeface(transaction);
                    txt_myOrder.setTypeface(orders);
                    txt_cart.setTypeface(cart);

                    isHome = false;
                    Fragment fragment = new CartFragment();
                    changeFragment(fragment, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cart_homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
                    home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.home_background));
                    img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
                    img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

                    txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
                    txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

                    Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                    Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

                    txt_profile.setTypeface(profile);
                    txt_transaction.setTypeface(transaction);
                    txt_myOrder.setTypeface(orders);
                    txt_cart.setTypeface(cart);

                    isHome = true;
                    Fragment fragment = new HomeFragment();
                    changeFragment(fragment, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void welcomeDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View convertView = inflater.inflate(R.layout.welcomedialog, null);
        CardView mainCartLayout = convertView.findViewById(R.id.mainCartLayout);
        mainCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RewardPointActivity.class));
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(convertView);
        alertDialog.show();

    }

    public  void getamount_from_adapter(Context context) {

        String url = baseurl + "cart_calculation";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        String total_products = obj.getString("total_products");
                        String totalpricee = obj.getString("total_amount");

                        int total_cart_item=  Integer.parseInt(total_products);
                        if (total_cart_item == 0) {
                            lead_badge.setVisibility(View.GONE);
                        } else {
                            lead_badge.setVisibility(View.VISIBLE);
                            lead_badge.setText("" + total_cart_item);
                        }                    }
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


    public void changeFragment(Fragment fragment, Boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_nav_host, fragment);
        if (addToBackStack) {
            ft.addToBackStack("addToBackStack");
        } else {
            ft.addToBackStack(null);
        }
        ft.commit();
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        addtowallet(razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    public void addtowallet(String t_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("id");
        String wallet_recharge_amount = yourPrefrence.getData("wallet_recharge_amount");
        String url = baseurl + "add_wallet_amount";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        params.put("transaction_id", t_id);
        params.put("amount", wallet_recharge_amount);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("goto", "wallet");
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
            }
        }) {

        };
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public static void badgecountmain(int size) {
        if (size == 0) {
            lead_badge.setVisibility(View.GONE);
        } else {
            lead_badge.setVisibility(View.VISIBLE);
        }
        lead_badge.setText("" + size);
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getApplicationContext(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    @Override
    public void onBackPressed() {
        if (isHome == true) {
            if (doubleBackToExitPressedOnce) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            YourPreference yourPrefrence = YourPreference.getInstance(MainActivity.this);
            String language = yourPrefrence.getData("language");
            if (language.equalsIgnoreCase("en") || language.equalsIgnoreCase("")) {
                Toast.makeText(this, "Press again to Close Sita", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "बंद करने के लिए फिर से दबाएं", Toast.LENGTH_SHORT).show();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {

            try {
                img_profile.setColorFilter(Color.argb(255, 158, 157, 157));
                img_transaction.setColorFilter(Color.argb(255, 158, 157, 157));
                home_img_Layout.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.home_background));
                img_myOrder.setColorFilter(Color.argb(255, 158, 157, 157));
                img_cart.setColorFilter(Color.argb(255, 158, 157, 157));

                txt_profile.setTextColor(Color.parseColor("#9e9d9d"));
                txt_transaction.setTextColor(Color.parseColor("#9e9d9d"));
                txt_myOrder.setTextColor(Color.parseColor("#9e9d9d"));
                txt_cart.setTextColor(Color.parseColor("#9e9d9d"));

                Typeface profile = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                Typeface transaction = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                Typeface orders = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);
                Typeface cart = ResourcesCompat.getFont(MainActivity.this, R.font.metropolis_regular);

                txt_profile.setTypeface(profile);
                txt_transaction.setTypeface(transaction);
                txt_myOrder.setTypeface(orders);
                txt_cart.setTypeface(cart);

                isHome = true;
                Fragment fragment = new HomeFragment();
                changeFragment(fragment, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}