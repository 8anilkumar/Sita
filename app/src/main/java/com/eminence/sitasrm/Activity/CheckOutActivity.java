package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.Profile.Address;
import com.eminence.sitasrm.Adapters.ProductAdapter;
import com.eminence.sitasrm.Interface.BadgingInterface;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.Models.CartModel;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.ProductModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.Fragments.CartFragment.totalprice;
import static com.eminence.sitasrm.MainActivity.badgecountmain;
import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class CheckOutActivity extends AppCompatActivity implements PaymentResultListener, BadgingInterface {

    RecyclerView product_recycle;
    ArrayList<ProductModel> offerlist = new ArrayList<>();
    DatabaseHandler databaseHandler;
    LinearLayout calculation_layout, checkOutLayout, addressFoundLayout, addressNotFoundLayout, walletAmountLayout,discountLayout;
    List<CartResponse> alldata;
    TextView address_name, address, state_pincode, mobile, txt_totalprice,
            dis_amt_txt, payble_amt_txt, net_amt_long_txt, payble_amt_main, txt_userWalletAmount,
            txt_walletDiductAmount, txt_rupeeSign, txt_wallet_amount,txtDiscountAmount;
    String email, hfnum, addresss, landmark, state, pincode, mobile1, mobile2, defaults, created_at, name, default_address_id, cart_id = "";
    boolean isaddressavailable = false;
    String totalAmount,discountAmount,payableAmount;
    CheckBox cb_userWallet;
    ArrayList<CartModel> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        product_recycle = findViewById(R.id.cartrecycleview);
        calculation_layout = findViewById(R.id.calculation_layout);
        checkOutLayout = findViewById(R.id.checkOutLayout);
        addressFoundLayout = findViewById(R.id.addressFoundLayout);
        addressNotFoundLayout = findViewById(R.id.addressNotFoundLayout);
        address_name = findViewById(R.id.address_name);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);
        txt_totalprice = findViewById(R.id.totalprice);
        dis_amt_txt = findViewById(R.id.dis_amt_txt);
        payble_amt_main = findViewById(R.id.payble_amt_main);
        payble_amt_txt = findViewById(R.id.payble_amt_txt);
        state_pincode = findViewById(R.id.state_pincode);
        net_amt_long_txt = findViewById(R.id.net_amt_long_txt);
        txt_userWalletAmount = findViewById(R.id.txt_userWalletAmount);
        txt_walletDiductAmount = findViewById(R.id.txt_walletDiductAmount);
        cb_userWallet = findViewById(R.id.cb_userWallet);
        txt_rupeeSign = findViewById(R.id.txt_rupeeSign);
        txt_wallet_amount = findViewById(R.id.txt_wallet_amount);
        walletAmountLayout = findViewById(R.id.walletAmountLayout);
        txtDiscountAmount = findViewById(R.id.txtDiscountAmount);
        discountLayout = findViewById(R.id.discountLayout);

        setUpDB();

        Intent intent = getIntent();
        totalAmount = String.valueOf(intent.getIntExtra("total_amount",0));
        discountAmount = String.valueOf(intent.getIntExtra("discount_amount",0));
        payableAmount = String.valueOf(intent.getIntExtra("payable_amount",0));
        payble_amt_txt.setText("₹" + payableAmount);
        payble_amt_main.setText(payableAmount);
        txt_totalprice.setText("₹" + totalAmount);
        if(discountAmount.equalsIgnoreCase("0") || discountAmount.equalsIgnoreCase("null")){

        } else {
            discountLayout.setVisibility(View.VISIBLE);
            txtDiscountAmount.setText("₹" + discountAmount);
        }

        alldata = databaseHandler.cartInterface().getallcartdata();
        if (Helper.INSTANCE.isNetworkAvailable(CheckOutActivity.this)) {

            // addtocart();
            getamount();
            // getUserProfile();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   CartList();
                }
            }, 1000);

        } else {
            Helper.INSTANCE.Error(CheckOutActivity.this, getString(R.string.NOCONN));
        }

        checkOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isaddressavailable) {
                    startPayment(payble_amt_main.getText().toString());
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please add address", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cb_userWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_userWallet.isChecked()) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String walletAmount = txt_userWalletAmount.getText().toString();
                    String nextAmount = payble_amt_main.getText().toString();
                    walletAmountLayout.setVisibility(View.VISIBLE);

                    if (Integer.parseInt(walletAmount) > Integer.parseInt(nextAmount)) {
                        int safaAmount = Integer.parseInt(walletAmount) - Integer.parseInt(nextAmount);
                        txt_walletDiductAmount.setText("" + nextAmount);
                        txt_rupeeSign.setVisibility(View.VISIBLE);
                        txt_userWalletAmount.setText("" + safaAmount);
                        payble_amt_main.setText("0");
                        yourPrefrence.saveData("wallet_diduct_amount", nextAmount);
                        txt_wallet_amount.setText("₹" + nextAmount);

                    } else {
                        int safaAmount = Integer.parseInt(nextAmount) - Integer.parseInt(walletAmount);
                        txt_walletDiductAmount.setText("" + walletAmount);
                        txt_rupeeSign.setVisibility(View.VISIBLE);
                        payble_amt_main.setText("" + safaAmount);
                        txt_userWalletAmount.setText("0");
                        yourPrefrence.saveData("wallet_diduct_amount", walletAmount);
                        txt_wallet_amount.setText("₹" + walletAmount);
                    }

                } else {
                    walletAmountLayout.setVisibility(View.GONE);
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String nextAmount = payble_amt_main.getText().toString();
                    String walletDiductAmount = yourPrefrence.getData("wallet_diduct_amount");
                    int safaAmount = Integer.parseInt(nextAmount) + Integer.parseInt(walletDiductAmount);
                    int updateWallet = Integer.parseInt(txt_userWalletAmount.getText().toString());
                    int newUpdateAmount = updateWallet + Integer.parseInt(walletDiductAmount);
                    txt_userWalletAmount.setText("" + newUpdateAmount);
                    txt_walletDiductAmount.setText("");
                    txt_wallet_amount.setText("");
                    txt_rupeeSign.setVisibility(View.GONE);
                    payble_amt_main.setText("" + safaAmount);
                    yourPrefrence.saveData("wallet_diduct_amount", nextAmount);

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        address();

    }

    public void getamount() {
        YourPreference yourPrefrence = YourPreference.getInstance(CheckOutActivity.this);
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "wallet_amount";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(CheckOutActivity.this);

        Map<String, String> params = new HashMap();
        params.put("user_id", user_id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        String amount = obj.getString("balance");
                        txt_userWalletAmount.setText(amount);
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


    private void bindadapter() {
        if(offerlist.size()  != 0 ){
            ProductAdapter offerlistAdapter = new ProductAdapter(offerlist, getApplicationContext(), this, "cart");
            product_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            product_recycle.setAdapter(offerlistAdapter);
            offerlistAdapter.notifyDataSetChanged();
        }
    }

    public void address() {
        String url = baseurl + "user_address_list";
        RequestQueue requestQueue = Volley.newRequestQueue(CheckOutActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        addressFoundLayout.setVisibility(View.VISIBLE);
                        addressNotFoundLayout.setVisibility(View.GONE);
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AddressModel addressModel = new AddressModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String type = jsonObject2.getString("type");

                            hfnum = jsonObject2.getString("hf_number");
                            addresss = jsonObject2.getString("address");
                            landmark = jsonObject2.getString("landmark");
                            state = jsonObject2.getString("state");
                            pincode = jsonObject2.getString("pincode");
                            created_at = jsonObject2.getString("created_at");
                            name = jsonObject2.getString("name");
                            mobile1 = jsonObject2.getString("mobile1");
                            mobile2 = jsonObject2.getString("mobile2");
                            defaults = jsonObject2.getString("defaults");

                            if (defaults.equalsIgnoreCase("1")) {

                                isaddressavailable = true;
                                address_name.setText(name);
                                address.setText(hfnum + " " + addresss + " near " + landmark);
                                state_pincode.setText(state + " " + pincode);
                                mobile.setText(mobile1 + "," + mobile2);
                                default_address_id = id;
                                if (type.equalsIgnoreCase("Home")) {
                                } else if (type.equalsIgnoreCase("Work")) {
                                    address_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_work_with_background, 0);

                                } else {
                                    address_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_other_with_background, 0);
                                }
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CheckOutActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(CheckOutActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        stringRequest.setShouldCache(false);

        requestQueue.add(stringRequest);

    }

    public void startPayment(String amount) {

        if (cb_userWallet.isChecked() && payble_amt_main.getText().toString().equalsIgnoreCase("0")) {
            make_order_request("");
        } else {
            final Activity activity = this;
            final Checkout co = new Checkout();
            try {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String email = yourPrefrence.getData("email");
                String mobile = yourPrefrence.getData("mobile");
                JSONObject options = new JSONObject();
                options.put("name", "SITA SRM");
                options.put("description", "Wallet recharge");
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
                options.put("currency", "INR");
                String payment = amount;
                double total = Double.parseDouble(payment);
                total = total * 100;
                options.put("amount", total);
                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", mobile);
                options.put("prefill", preFill);
                co.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }


    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(CheckOutActivity.this, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onPaymentSuccess(String s) {
        make_order_request(s);
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    private void make_order_request(String Transaction_id) {
        JSONArray jsonArray = null;
        String json = String.valueOf(new Gson().toJsonTree(alldata));
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("id");
        String url = baseurl + "place_order";
        RequestQueue requestQueue = Volley.newRequestQueue(CheckOutActivity.this);

        String walletAmount = "0";

        if (txt_walletDiductAmount.getText().toString().equalsIgnoreCase("") || txt_walletDiductAmount.getText().toString().equalsIgnoreCase("null")) {
            walletAmount = "0";
        } else {
            walletAmount = txt_walletDiductAmount.getText().toString();
        }

        JSONObject params = new JSONObject();

        try {
            params.put("user_id", user_id);
            params.put("address_id", default_address_id);
            params.put("transaction_id", Transaction_id);
            params.put("total_amount", totalAmount);
            params.put("wallet_amount", walletAmount);
            params.put("discount_amount", discountAmount);
            params.put("net_amount", payble_amt_main.getText().toString());
            // params.put("products", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // JsonObject jsonObject=new JsonObject(params);

        Log.i("order paramenter", String.valueOf(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("order response", String.valueOf(response));

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    Toast.makeText(CheckOutActivity.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        databaseHandler.cartInterface().deleteall();
                        totalprice = 0;
                        Intent intent = new Intent(CheckOutActivity.this, PaymentSuccessfullActivity.class);
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
                error.printStackTrace();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void changeaddress(View view) {
        Intent intent = new Intent(CheckOutActivity.this, Address.class);
        startActivity(intent);
    }

    @Override
    public void badgecount() {
        getamount_from_adapter(CheckOutActivity.this);
    }

    public void getUserProfile() {
        String url = baseurl + "user_profile";
        RequestQueue requestQueue = Volley.newRequestQueue(CheckOutActivity.this);
        YourPreference yourPrefrence = YourPreference.getInstance(CheckOutActivity.this);
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            email = jsonObject2.getString("email");
                            if (email.equalsIgnoreCase("") || email.equalsIgnoreCase("null")) {
                                Toast.makeText(CheckOutActivity.this, "Please update your profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

        };
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

    public void getamount_from_adapter(Context context) {

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

                        int total_cart_item = Integer.parseInt(total_products);
                        badgecountmain(total_cart_item);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //   addtocart();
    }

}