package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.ProductOrderAdapter;
import com.eminence.sitasrm.Models.ProductOrderModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class OrderDetails extends AppCompatActivity {

    TextView cust_name, houseno, landmark, state, mobile, totalprice, discount, billamount, onlinepay;
    TextView txt_orderId, wallet_amount_txt;
    ShimmerRecyclerView product_recycle;
    ArrayList<ProductOrderModel> orderlist = new ArrayList<>();
    LinearLayout helpLayout, cancelLayout, helpNeedLayout, mainHelpLayout;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        txt_orderId = findViewById(R.id.txt_orderId);
        wallet_amount_txt = findViewById(R.id.wallet_amount_txt);
        cust_name = findViewById(R.id.cust_name);
        houseno = findViewById(R.id.houseno);
        landmark = findViewById(R.id.landmark);
        state = findViewById(R.id.state);
        product_recycle = findViewById(R.id.product_recycle);
        mobile = findViewById(R.id.mobile);
        totalprice = findViewById(R.id.totalprice);
        discount = findViewById(R.id.discount);
        billamount = findViewById(R.id.billamount);
        onlinepay = findViewById(R.id.onlinepay);
        helpLayout = findViewById(R.id.helpLayout);
        cancelLayout = findViewById(R.id.cancelLayout);
        helpNeedLayout = findViewById(R.id.helpNeedLayout);
        mainHelpLayout = findViewById(R.id.mainHelpLayout);

        Intent intent = getIntent();
        String od_id = intent.getStringExtra("order_id");

        if (Helper.INSTANCE.isNetworkAvailable(OrderDetails.this)) {
            getOrderDetails(od_id);
        } else {
            Helper.INSTANCE.Error(OrderDetails.this, getString(R.string.NOCONN));
        }


        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetails.this, Help.class);
                startActivity(intent);
            }
        });

        helpNeedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(OrderDetails.this, Help.class);
                startActivity(intent);
            }
        });

        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OrderDetails.this, RequestCancelActivity.class);
                intent1.putExtra("order_id", od_id);
                intent1.putExtra("product_id", "");
                intent1.putExtra("cashback", "");
                intent1.putExtra("orderDetails", "orderDetails");

                startActivity(intent1);
            }
        });

    }

    private void getOrderDetails(String od_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(OrderDetails.this);
        String id = yourPrefrence.getData("id");

        String url = baseurl + "order_details";
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetails.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("order_id", od_id);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        orderlist.clear();

                        String total_amount = obj.getString("total_amount");
                        String net_amount = obj.getString("net_amount");
                        String order_status = obj.getString("order_status");
                        String order_address = obj.getString("order_address");
                        String address_type = obj.getString("address_type");
                        String order_pincode = obj.getString("order_pincode");
                        String order_state = obj.getString("order_state");
                        String wallet_amount = obj.getString("wallet_amount");
                        String name = obj.getString("order_user_name");
                        String number1 = obj.getString("order_user_mobile");
                        address = order_address;

                        JSONArray jsonArray = obj.getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            ProductOrderModel orderModel = new ProductOrderModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            String productname = jsonObject2.getString("product_name");
                            String productnamehindi = jsonObject2.getString("product_name_hindi");
                            String id = jsonObject2.getString("id");
                            String productimage = jsonObject2.getString("product_image");
                            String created_at = jsonObject2.getString("created_at");
                            String price = jsonObject2.getString("price");
                            String quantity = jsonObject2.getString("quantity");
                            String odr_status = jsonObject2.getString("odr_status");
                            String productCaption = jsonObject2.getString("product_caption");
                            String p_caption_hindi = jsonObject2.getString("p_caption_hindi");

                            orderModel.setProductId(id);
                            orderModel.setProductName(productname);
                            orderModel.setProductNameHindi(productnamehindi);
                            orderModel.setCreatedAt(created_at);
                            orderModel.setPrice(price);
                            orderModel.setQuantity(quantity);
                            orderModel.setProductImage(productimage);
                            orderModel.setOdrStatus(odr_status);
                            orderModel.setProduct_caption(productCaption);
                            orderModel.setP_caption_hindi(p_caption_hindi);
                            orderlist.add(orderModel);
                        }

                        if (!order_status.equalsIgnoreCase("Order Placed")) {
                            helpNeedLayout.setVisibility(View.VISIBLE);
                            mainHelpLayout.setVisibility(View.GONE);
                        }

                        YourPreference yourPrefrence = YourPreference.getInstance(OrderDetails.this);
                        String language = yourPrefrence.getData("language");

                        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
                            txt_orderId.setText("ऑर्डर आईडी: " + "" + od_id);
                            onlinepay.setText("ऑनलाइन भुगतान " + "\u20B9" + net_amount);
                            houseno.setText(Html.fromHtml("<font color='black'> पता: </font>" + address), TextView.BufferType.SPANNABLE);
                            state.setText(Html.fromHtml("<font color='black'> राज्य:  </font>" + order_state), TextView.BufferType.SPANNABLE);
                            cust_name.setText(Html.fromHtml("<font color='black'> नाम:  </font>" + name), TextView.BufferType.SPANNABLE);
                            //mobile.setText(Html.fromHtml("<font color='black'> मोबाइल नंबर:  </font>" + number1 + " , " + number2), TextView.BufferType.SPANNABLE);
                            mobile.setText(Html.fromHtml("<font color='black'> मोबाइल नंबर:  </font>" + number1), TextView.BufferType.SPANNABLE);

                        } else {
                            txt_orderId.setText("Order Id: " + "" + od_id);
                            onlinepay.setText("Online Pay " + "\u20B9" + net_amount);
                            houseno.setText(Html.fromHtml("<font color='black'> Address: </font>" + address), TextView.BufferType.SPANNABLE);
                            state.setText(Html.fromHtml("<font color='black'> State:  </font>" + order_state), TextView.BufferType.SPANNABLE);
                            cust_name.setText(Html.fromHtml("<font color='black'> Name:  </font>" + name), TextView.BufferType.SPANNABLE);
                            //mobile.setText(Html.fromHtml("<font color='black'> Mobile Number:  </font>" + number1 + " , " + number2), TextView.BufferType.SPANNABLE);
                            mobile.setText(Html.fromHtml("<font color='black'> Mobile Number:  </font>" + number1), TextView.BufferType.SPANNABLE);
                        }

                        totalprice.setText("\u20B9" + total_amount);
                        wallet_amount_txt.setText("\u20B9" + wallet_amount);
                        discount.setText("\u20B9" + "");
                        billamount.setText("\u20B9" + net_amount);

                        ProductOrderAdapter orderAdapter = new ProductOrderAdapter(orderlist, OrderDetails.this, od_id, "OrderDetails");
                        product_recycle.setLayoutManager(new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false));
                        product_recycle.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();

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

    public void back(View view) {
        onBackPressed();
    }
}