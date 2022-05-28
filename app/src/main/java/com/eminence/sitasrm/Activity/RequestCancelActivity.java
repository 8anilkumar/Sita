package com.eminence.sitasrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.eminence.sitasrm.Adapters.ProductOrderAdapter;
import com.eminence.sitasrm.Models.ProductOrderModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eminence.sitasrm.Utils.Baseurl.baseurl;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class RequestCancelActivity extends AppCompatActivity {

    TextView txt_orderId, txt_dontCancel, txtcancelOrder, txt_discountOffer, txt_submitRequest;
    ImageView product_image;
    LinearLayout submitRequestLayout, OrderListRowLayout, singleRowLayout;
    BottomSheetDialog bottomSheetDialog;
    RadioGroup radioGroup;
    EditText ed_comment;
    String selectedReason = "", cartid = "";
    ShimmerRecyclerView product_recycle;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    ArrayList<ProductOrderModel> orderlist = new ArrayList<>();
    TextView txt_createdAt, txt_productName, txt_discount, txt_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cancel);

        txt_createdAt = findViewById(R.id.txt_createdAt);
        txt_productName = findViewById(R.id.txt_productName);
        txt_discount = findViewById(R.id.txt_discount);
        txt_price = findViewById(R.id.txt_price);
        txt_orderId = findViewById(R.id.txt_orderId);
        product_image = findViewById(R.id.product_image);
        submitRequestLayout = findViewById(R.id.submitRequestLayout);
        txt_submitRequest = findViewById(R.id.txt_submitRequest);
        product_recycle = findViewById(R.id.product_recycle);
        OrderListRowLayout = findViewById(R.id.OrderListRowLayout);
        singleRowLayout = findViewById(R.id.singleRowLayout);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        ed_comment = findViewById(R.id.ed_comment);

        Intent intent = getIntent();
        String od_id = intent.getStringExtra("order_id");
         String product_id = intent.getStringExtra("product_id");
        String orderDetails = intent.getStringExtra("orderDetails");

        if (Helper.INSTANCE.isNetworkAvailable(RequestCancelActivity.this)) {

            if (orderDetails.equalsIgnoreCase("CancelSingleProduct")) {

                String created_at = intent.getStringExtra("created_at");
                String discount = intent.getStringExtra("quantity");
                String name = intent.getStringExtra("name");
                String price = intent.getStringExtra("price");
                String productimage = intent.getStringExtra("productimage");
                String name_hindi = intent.getStringExtra("name_hindi");

                singleRowLayout.setVisibility(View.VISIBLE);
                txt_productName.setText(name);
                txt_discount.setText(discount);
                txt_price.setText(price);

                YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
                String language = yourPrefrence.getData("language");
                if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
                    txt_discount.setText("मात्रा: " + name_hindi);
                    txt_createdAt.setText("ऑर्डर किया गया " + created_at);
                } else {
                    txt_discount.setText("Ouantity: " + discount);
                    txt_createdAt.setText("Order Place On " + created_at);
                }

                Glide.with(RequestCancelActivity.this).load(imagebaseurl + productimage)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.app_logo)
                                .error(R.drawable.app_logo))
                        .into(product_image);
            } else {
                OrderListRowLayout.setVisibility(View.VISIBLE);
                getOrderDetails(od_id);
            }

        } else {
            Helper.INSTANCE.Error(RequestCancelActivity.this, getString(R.string.NOCONN));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioButton1.isChecked()) {
                    selectedReason = radioButton1.getText().toString();
                    submitRequestLayout.setBackgroundResource(R.drawable.theem_corner_bg);
                    txt_submitRequest.setTextColor(getResources().getColor(R.color.white));
                } else if (radioButton2.isChecked()) {
                    selectedReason = radioButton2.getText().toString();
                    submitRequestLayout.setBackgroundResource(R.drawable.theem_corner_bg);
                    txt_submitRequest.setTextColor(getResources().getColor(R.color.white));
                } else if (radioButton3.isChecked()) {
                    selectedReason = radioButton3.getText().toString();
                    submitRequestLayout.setBackgroundResource(R.drawable.theem_corner_bg);
                    txt_submitRequest.setTextColor(getResources().getColor(R.color.white));
                } else if (radioButton4.isChecked()) {
                    selectedReason = radioButton4.getText().toString();
                    submitRequestLayout.setBackgroundResource(R.drawable.theem_corner_bg);
                    txt_submitRequest.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            txt_orderId.setText("ऑर्डर आईडी: " + "" + od_id);
        } else {
            txt_orderId.setText("Order Id: " + "" + od_id);
        }

        submitRequestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(RequestCancelActivity.this);
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout, findViewById(R.id.bottom_sheet));
                txt_dontCancel = sheetView.findViewById(R.id.txt_dontCancel);
                txtcancelOrder = sheetView.findViewById(R.id.txtcancelOrder);
                txt_discountOffer = sheetView.findViewById(R.id.txt_discountOffer);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();

                YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
                String language = yourPrefrence.getData("language");

                txtcancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedReason.equalsIgnoreCase("")) {
                            Toast.makeText(RequestCancelActivity.this, "Please select reason ", Toast.LENGTH_SHORT).show();
                        } else {
                            if (product_id.equalsIgnoreCase("")) {
                                if (Helper.INSTANCE.isNetworkAvailable(RequestCancelActivity.this)) {
                                    cancelOrder(od_id, selectedReason, ed_comment.getText().toString(), cartid, bottomSheetDialog);
                                } else {
                                    Helper.INSTANCE.Error(RequestCancelActivity.this, getString(R.string.NOCONN));
                                }
                            } else {
                                if (Helper.INSTANCE.isNetworkAvailable(RequestCancelActivity.this)) {
                                    cancelProduct(product_id, selectedReason, ed_comment.getText().toString(), bottomSheetDialog);
                                } else {
                                    Helper.INSTANCE.Error(RequestCancelActivity.this, getString(R.string.NOCONN));
                                }
                            }
                        }
                    }
                });

                txt_dontCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RequestCancelActivity.this, OrderDetails.class);
                        intent.putExtra("order_id", od_id);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

    }


    private void cancelOrder(String od_id, String reason, String comment, String cartid, BottomSheetDialog bottomSheetDialog) {
        YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
        String id = yourPrefrence.getData("id");
        String url = baseurl + "cancel_order";
        RequestQueue requestQueue = Volley.newRequestQueue(RequestCancelActivity.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("order_id", od_id);
        params.put("reason", reason);
         params.put("comments", comment);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent1 = new Intent(RequestCancelActivity.this, CancellationConfirmedActivity.class);
                        startActivity(intent1);
                        bottomSheetDialog.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(RequestCancelActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    private void cancelProduct(String c_id, String reason, String comment, BottomSheetDialog bottomSheetDialog) {

        YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
        String id = yourPrefrence.getData("id");
        String url = baseurl + "cancel_product";
        RequestQueue requestQueue = Volley.newRequestQueue(RequestCancelActivity.this);
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        params.put("c_id", c_id);
        params.put("reason", reason);
        params.put("comments", comment);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        Intent intent1 = new Intent(RequestCancelActivity.this, CancellationConfirmedActivity.class);
                        startActivity(intent1);
                        bottomSheetDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(RequestCancelActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    private void getOrderDetails(String od_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(RequestCancelActivity.this);
        String id = yourPrefrence.getData("id");
        String url = baseurl + "order_details";
        RequestQueue requestQueue = Volley.newRequestQueue(RequestCancelActivity.this);
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

                        ProductOrderAdapter orderAdapter = new ProductOrderAdapter(orderlist, RequestCancelActivity.this, od_id, "RequestCancel");
                        product_recycle.setLayoutManager(new LinearLayoutManager(RequestCancelActivity.this, LinearLayoutManager.VERTICAL, false));
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
                //Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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